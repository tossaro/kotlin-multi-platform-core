package multi.platform.example_lib.shared.app.stocklist

import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.badge.BadgeUtils
import multi.platform.core.shared.app.common.BaseActivity
import multi.platform.core.shared.app.common.BaseFragment
import multi.platform.core.shared.app.common.GenericAdapter
import multi.platform.core.shared.domain.common.entity.GenericItem
import multi.platform.core.shared.external.constant.AppConstant
import multi.platform.core.shared.external.extension.*
import multi.platform.core.shared.external.utility.LocaleUtil
import multi.platform.example_lib.shared.BuildConfig
import multi.platform.example_lib.shared.R
import multi.platform.example_lib.shared.data.stock.network.response.StockSocket
import multi.platform.example_lib.shared.databinding.StockListFragmentBinding
import multi.platform.example_lib.shared.domain.stock.entity.Stock
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
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
    var stockAdapter: GenericAdapter? = null
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

        actionBar()?.apply {
            setPadding(dpToPx(24f), 0, 0, 0)
        }
        actionBarSearch()?.apply {
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
            it.stocks.launchAndCollectIn(this, Lifecycle.State.STARTED) { ss ->
                notifyStocksAdapter(ss)
            }
            it.notif.launchAndCollectIn(this, Lifecycle.State.STARTED) { c -> showNotifBadge(c) }
            it.promo.launchAndCollectIn(this, Lifecycle.State.STARTED) { c -> showPromoBadge(c) }
            it.loadingIndicator.launchAndCollectIn(this, Lifecycle.State.STARTED) { l ->
                onLoading(l)
            }
            it.successMessage.launchAndCollectIn(this, Lifecycle.State.STARTED) { m ->
                showSuccessSnackbar(m)
                it.successMessage.value = null
            }
            it.errorMessage.launchAndCollectIn(this, Lifecycle.State.STARTED) { m ->
                showErrorSnackbar(m)
                it.errorMessage.value = null
            }
            it.toastMessage.launchAndCollectIn(this, Lifecycle.State.STARTED) { m ->
                showToast(m)
                it.toastMessage.value = null
            }
        }

        binding.swipeContainer.setOnRefreshListener {
            load(1)
        }
        stockAdapter = GenericAdapter(1.0, 90, 8, 4)
        stockAdapter?.skeleton = GenericItem(
            0,
            "",
            "",
            "",
            null,
            "",
            "",
        )
        stockAdapter?.onSelected = {
            goTo(
                getString(R.string.route_stock_detail_sheet).replace("{coin}", it.title.toString())
                    .replace("{value}", it.middlePrice.toString().toCurrency(AppConstant.CURRENCY))
            )
        }
        stockAdapter?.fetchData = {
            vm.page++
            load(vm.page)
        }
        binding.rvStock.adapter = stockAdapter
        val listener = stockAdapter?.Listener()
        listener?.let { binding.rvStock.addOnScrollListener(it) }

        createWebSocketClient()
        load(1)
    }

    @Suppress("UnsafeOptInUsageError")
    override fun onPause() {
        super.onPause()
        webSocketClient.close()
        actionBar()?.let { BadgeUtils.detachBadgeDrawable(badgeNotification, it, R.id.notif) }
    }

    override fun onResume() {
        super.onResume()
        actionBarExpandedViewPager()?.apply {
            isVisible = true
            adapter = HeaderAdapter()
        }
        val isFromNetwork = (requireActivity() as BaseActivity).isInternetAvailable()
        if (webSocketClient.isClosed && isFromNetwork) webSocketClient.reconnect()
        badgeNotification?.isVisible = true
        badgePromo?.isVisible = true
    }

    private fun onLoading(isLoading: Boolean?) {
        hideKeyboard()
        stockAdapter?.itemsCache?.let {
            if (it.size == 0 && isLoading == true) stockAdapter?.showSkeletons()
        }
    }

    private fun load(p: Int) {
        vm.page = p
        val isFromNetwork = (requireActivity() as BaseActivity).isInternetAvailable()
        if (vm.isFromNetwork != isFromNetwork) {
            vm.page = 1
            stockAdapter?.items?.let { ss ->
                ss.forEach { s -> unsubscribe(s.name.toString()) }
            }
        }
        vm.isFromNetwork = isFromNetwork
        if (webSocketClient.isClosed && isFromNetwork) webSocketClient.reconnect()
        if (p == 1) stockAdapter?.clear()
        vm.getStocks()
    }

    private fun notifyStocksAdapter(stocks: List<Stock>?) {
        stocks?.takeIf { it.isNotEmpty() }?.let { its ->
            stockAdapter?.addItem {
                for (it in its) {
                    val item = GenericItem(
                        it.id.toInt(),
                        it.imageUrl,
                        it.name,
                        it.fullname,
                        null,
                        it.price,
                        " / ${it.status ?: ""}"
                    )
                    stockAdapter?.items?.add(item)
                    stockAdapter?.itemsCache?.add(item)
                }
            }
            binding.swipeContainer.isRefreshing = false
            its.forEach { if (webSocketClient.isOpen) subscribe(it.name.toString()) }
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
                stockAdapter?.items?.let { ss ->
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
                stockAdapter?.items?.let { ss ->
                    ss.forEach { s ->
                        if (s.name == usd.symbol) {
                            s.middlePrice = String.format("%.5f", usd.topTierFullVolume.toDouble())
                            val i = ss.indexOf(s)
                            try {
                                stockAdapter?.notifyItemChanged(i)
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
    private fun showNotifBadge(size: Int?) {
        if (size != null && size > 0) {
            badgeNotification = BadgeDrawable.create(requireContext())
            badgeNotification?.apply {
                number = size
                maxCharacterCount = 2
                verticalOffset = -10
                horizontalOffset = 10
                actionBar()?.let { t -> BadgeUtils.attachBadgeDrawable(this, t, R.id.notif) }
            }
        }
    }

    @Suppress("UnsafeOptInUsageError")
    private fun showPromoBadge(size: Int?) {
        if (size != null && size > 0) {
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