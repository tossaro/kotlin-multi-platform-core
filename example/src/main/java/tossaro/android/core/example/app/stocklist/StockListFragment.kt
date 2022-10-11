package tossaro.android.core.example.app.stocklist

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
import com.google.gson.Gson
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber
import tossaro.android.core.app.common.BaseActivity
import tossaro.android.core.app.common.BaseFragment
import tossaro.android.core.example.BuildConfig
import tossaro.android.core.example.ExampleRouters
import tossaro.android.core.example.R
import tossaro.android.core.example.data.network.response.StockSocket
import tossaro.android.core.example.databinding.StockListFragmentBinding
import tossaro.android.core.example.domain.stock.entity.Stock
import tossaro.android.core.external.extension.dpToPx
import tossaro.android.core.external.extension.goTo
import tossaro.android.core.external.extension.showErrorSnackbar
import tossaro.android.core.external.extension.showSuccessSnackbar
import tossaro.android.core.external.extension.showToast
import tossaro.android.core.external.utility.LocaleUtil
import java.net.URI
import javax.net.ssl.SSLSocketFactory

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

    override fun showBottomNavBar() = true
    override fun isFullScreen() = true
    override fun addBottomInsets() = true
    override fun expandActionBar() = true
    override fun actionBarHeight() = 170
    override fun actionBarTopMargin() = 30
    override fun showActionBarSearch() = true
    override fun actionBarContentScrim() = R.drawable.bg_gradient_primary

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
            val icSearch = ContextCompat.getDrawable(requireContext(), R.drawable.ic_search_12)
            setCompoundDrawablesWithIntrinsicBounds(icSearch, null, null, null)
        }
        binding.lifecycleOwner = viewLifecycleOwner
        binding.vm = vm.also {
            it.loadingIndicator.observe(viewLifecycleOwner, ::showFullLoading)
            it.toastMessage.observe(viewLifecycleOwner, ::showToast)
            it.stocks.observe(viewLifecycleOwner, ::notifyStocksAdapter)
            it.successMessage.observe(viewLifecycleOwner, ::showSuccessSnackbar)
            it.errorMessage.observe(viewLifecycleOwner, ::showErrorSnackbar)
            it.notif.observe(viewLifecycleOwner, ::showNotifBadge)
            it.promo.observe(viewLifecycleOwner, ::showPromoBadge)
        }
        binding.swipeContainer.setOnRefreshListener {
            vm.stocks.value = mutableListOf()
            getStocks(1)
        }
        mAdapter = StockAdapter()
        mAdapter?.onClick = { c, v ->
            goTo(ExampleRouters.STOCK_DETAIL_DIALOG.replace("{coin}", c).replace("{value}", v))
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
            val usd = Gson().fromJson(message, StockSocket::class.java)
            if (usd?.TOPTIERFULLVOLUME != null && vm.loadingIndicator.value == false) {
                mAdapter?.stocks?.let { ss ->
                    ss.forEach { s ->
                        if (s.name == usd.SYMBOL) {
                            s.price = String.format("%.5f", usd.TOPTIERFULLVOLUME.toDouble())
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
            val promoView = promoContainer?.findViewById<TextView>(R.id.tv_icon_text_menu)
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
                            promoView.findViewById(R.id.fl_icon_text_menu)
                        )
                    }
                    promoView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                }
            })
        }
    }
}