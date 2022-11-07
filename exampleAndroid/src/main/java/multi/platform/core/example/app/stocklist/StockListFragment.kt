package multi.platform.core.example.app.stocklist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.badge.BadgeUtils
import multi.platform.core.example.BuildConfig
import multi.platform.core.example.R
import multi.platform.core.example.data.stock.network.response.StockSocket
import multi.platform.core.example.databinding.StockListFragmentBinding
import multi.platform.core.example.domain.stock.entity.Stock
import multi.platform.core.shared.app.common.BaseActivity
import multi.platform.core.shared.app.common.BaseFragment
import multi.platform.core.shared.external.extension.dpToPx
import multi.platform.core.shared.external.extension.goTo
import multi.platform.core.shared.external.extension.showErrorSnackbar
import multi.platform.core.shared.external.extension.showSuccessSnackbar
import multi.platform.core.shared.external.extension.showToast
import multi.platform.core.shared.external.utility.LocaleUtil
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import multi.platform.core.shared.external.extension.launchAndCollectIn
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber
import java.net.URI
import javax.net.ssl.SSLSocketFactory
import multi.platform.core.shared.R as cR

class StockListFragment : BaseFragment<StockListFragmentBinding>(
    R.layout.stock_list_fragment
), MenuProvider {
    private lateinit var webSocketClient: WebSocketClient
    private val vm: StockListViewModel by viewModel()
    var mAdapter: StockAdapter? = null
    var page = 1
    private var badgeNotification: BadgeDrawable? = null
    private var badgePromo: BadgeDrawable? = null
    private var promoContainer: View? = null
    private val json = Json { ignoreUnknownKeys = true }

    override fun showBottomNavBar() = true
    override fun isFullScreen() = true
    override fun addBottomInsets() = true
    override fun expandActionBar() = true
    override fun actionBarHeight() = 170
    override fun actionBarTopMargin() = 30
    override fun showActionBarSearch() = true
    override fun actionBarContentScrim() = cR.drawable.bg_gradient_primary

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    @Suppress("UnsafeOptInUsageError")
    override fun onCreateMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_home, menu)
        promoContainer = menu.findItem(R.id.promo).actionView
        promoContainer?.setOnClickListener {
            LocaleUtil.setLocale(requireContext(), LocaleUtil.EN)
            activity?.finish()
            startActivity(activity?.intent)
        }
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.notif -> {
                LocaleUtil.setLocale(requireContext(), LocaleUtil.ID)
                activity?.finish()
                startActivity(activity?.intent)
            }
        }
        return false
    }

    @Suppress("UnsafeOptInUsageError")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        actionBar().apply {
            setPadding(dpToPx(24f), 0, 0, 0)
        }
        actionBarSearch().apply {
            val dp7 = dpToPx(7f)
            val dp10 = dpToPx(10f)
            setPadding(dp10, dp7, dp10, dp7)
            height = dpToPx(32f)
            isFocusable = false
            textSize = 12f
            setOnClickListener {
                vm.toastMessage.value = "search screen"
            }
            val icSearch = ContextCompat.getDrawable(requireContext(), cR.drawable.ic_search_12)
            setCompoundDrawablesWithIntrinsicBounds(icSearch, null, null, null)
        }
        binding.lifecycleOwner = viewLifecycleOwner
        binding.vm = vm.also {
            it.stocks.observe(viewLifecycleOwner, ::notifyStocksAdapter)
            it.notif.observe(viewLifecycleOwner, ::showNotifBadge)
            it.promo.observe(viewLifecycleOwner, ::showPromoBadge)
            it.loadingIndicator.launchAndCollectIn(
                this,
                Lifecycle.State.STARTED
            ) { l -> showFullLoading(l) }
            it.successMessage.launchAndCollectIn(
                this,
                Lifecycle.State.STARTED
            ) { m -> showSuccessSnackbar(m) }
            it.errorMessage.launchAndCollectIn(
                this,
                Lifecycle.State.STARTED
            ) { m -> showErrorSnackbar(m) }
            it.toastMessage.launchAndCollectIn(
                this,
                Lifecycle.State.STARTED
            ) { m -> showToast(m) }
        }

        binding.swipeContainer.setOnRefreshListener {
            vm.stocks.value = mutableListOf()
            getStocks(1)
        }
        mAdapter = StockAdapter()
        mAdapter?.onClick = { c, v ->
            goTo(getString(R.string.route_stock_detail_sheet).replace("{coin}", c).replace("{value}", v))
        }
        val mLayoutManager = LinearLayoutManager(activity)
        binding.rvStock.layoutManager = mLayoutManager
        binding.rvStock.adapter = mAdapter
        binding.rvStock.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                Timber.d("scroll ${recyclerView.canScrollVertically(1)}")
                if (!recyclerView.canScrollVertically(1)) {
                    page++
                    getStocks(page)
                }
            }
        })

        createWebSocketClient()
        getStocks(1)
    }

    @Suppress("UnsafeOptInUsageError")
    override fun onPause() {
        super.onPause()
        webSocketClient.close()
        actionBar().let { BadgeUtils.detachBadgeDrawable(badgeNotification, it, R.id.notif) }
    }

    override fun onResume() {
        super.onResume()
        actionBarViewPager().adapter = HeaderAdapter()
        val isFromNetwork = (requireActivity() as BaseActivity).isInternetAvailable()
        if (webSocketClient.isClosed && isFromNetwork) webSocketClient.reconnect()
        badgeNotification?.isVisible = true
        badgePromo?.isVisible = true
    }

    private fun getStocks(p: Int) {
        page = p
        val isFromNetwork = (requireActivity() as BaseActivity).isInternetAvailable()
        if (vm.isFromNetwork != isFromNetwork) {
            page = 1
            mAdapter?.stocks?.let { ss ->
                ss.forEach { s -> unsubscribe(s.name.toString()) }
                mAdapter?.notifyItemRangeRemoved(0, ss.size)
                ss.clear()
            }
        }
        vm.isFromNetwork = isFromNetwork
        if (webSocketClient.isClosed && isFromNetwork) webSocketClient.reconnect()
        vm.getStocks(page)
    }

    private fun notifyStocksAdapter(stocks: List<Stock>?) {
        stocks?.let { ss ->
            mAdapter?.stocks?.let { s ->
                val sizeBefore = s.size
                s.addAll(ss)
                val sizeAfter = s.size - 1
                mAdapter?.notifyItemRangeChanged(sizeBefore, sizeAfter)
            }
            binding.swipeContainer.isRefreshing = false
            ss.forEach { s -> if (webSocketClient.isOpen) subscribe(s.name.toString()) }
        }
    }

    @Suppress("kotlin:S1186")
    private fun createWebSocketClient() {
        webSocketClient = object : WebSocketClient(URI(BuildConfig.SOCKET)) {
            override fun onOpen(handshakedata: ServerHandshake?) {}

            override fun onMessage(message: String?) {
                onMessageSocket(message)
            }

            override fun onClose(code: Int, reason: String?, remote: Boolean) {
                mAdapter?.stocks?.let { ss ->
                    ss.forEach { s -> unsubscribe(s.name.toString()) }
                }
            }

            override fun onError(ex: java.lang.Exception?) {}

        }
        val socketFactory: SSLSocketFactory = SSLSocketFactory.getDefault() as SSLSocketFactory
        webSocketClient.setSocketFactory(socketFactory)
        webSocketClient.connect()
    }

    private fun onMessageSocket(message: String?) {
        activity?.runOnUiThread {
            val usd = json.decodeFromString<StockSocket?>(message.toString())
            if (usd?.topTierFullVolume != null && vm.loadingIndicator.value == false) {
                mAdapter?.stocks?.let { ss ->
                    ss.forEach { s ->
                        if (s.name == usd.symbol) {
                            s.price = String.format("%.5f", usd.topTierFullVolume.toDouble())
                            val i = ss.indexOf(s)
                            try {
                                mAdapter?.notifyItemChanged(i)
                            } catch (e: Exception) {
                                Timber.d("Notify $i Exception", e)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun subscribe(coin: String) {
        if (webSocketClient.isOpen) webSocketClient.send(
            "{\n" +
                    "    \"action\": \"SubAdd\",\n" +
                    "    \"subs\": [\"21~$coin\"]\n" +
                    "}"
        )
    }

    private fun unsubscribe(coin: String) {
        if (webSocketClient.isOpen) webSocketClient.send(
            "{\n" +
                    "    \"action\": \"SubRemove\",\n" +
                    "    \"subs\": [\"2~Coinbase~$coin~USD\"]\n" +
                    "}"
        )
    }

    @Suppress("UnsafeOptInUsageError")
    private fun showNotifBadge(size: Int) {
        if (size > 0) {
            badgeNotification = BadgeDrawable.create(requireContext())
            badgeNotification?.apply {
                number = size
                maxCharacterCount = 2
                verticalOffset = -10
                horizontalOffset = 10
                BadgeUtils.attachBadgeDrawable(this, actionBar(), R.id.notif)
            }
        }
    }

    @Suppress("UnsafeOptInUsageError")
    private fun showPromoBadge(size: Int) {
        if (size > 0) {
            val promoView = promoContainer?.findViewById<TextView>(cR.id.tv_icon_text_menu)
            promoView?.viewTreeObserver?.addOnGlobalLayoutListener(object :
                ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    badgePromo = BadgeDrawable.create(requireContext())
                    badgePromo?.apply {
                        number = 2
                        maxCharacterCount = 2
                        horizontalOffset = if (number > 9) 15 else 10
                        BadgeUtils.attachBadgeDrawable(
                            this,
                            promoView,
                            promoView.findViewById(cR.id.fl_icon_text_menu)
                        )
                    }
                    promoView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                }
            })
        }
    }
}