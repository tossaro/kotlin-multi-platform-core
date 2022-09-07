package tossaro.android.core.example.app.stocklist

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber
import tossaro.android.core.app.BaseActivity
import tossaro.android.core.app.BaseFragment
import tossaro.android.core.example.BuildConfig
import tossaro.android.core.example.ExampleRouters
import tossaro.android.core.example.R
import tossaro.android.core.example.data.network.response.StockSocket
import tossaro.android.core.example.databinding.StockListFragmentBinding
import tossaro.android.core.example.domain.stock.entity.Stock
import tossaro.android.core.external.ext.goTo
import tossaro.android.core.external.ext.showToast
import java.net.URI
import javax.net.ssl.SSLSocketFactory

class StockListFragment : BaseFragment<StockListFragmentBinding>(
    R.layout.stock_list_fragment
) {
    private var doubleBackToExitPressedOnce = false
    private lateinit var webSocketClient: WebSocketClient
    private val viewModel: StockListViewModel by viewModel()
    var mAdapter: StockAdapter? = null
    var page = 1

    override fun actionBarTitle(): String = getString(R.string.app_name)

    override fun onBackPressed(): Boolean {
        if (doubleBackToExitPressedOnce) {
            webSocketClient.close()
            activity?.finish()
            return true
        }

        doubleBackToExitPressedOnce = true
        Toast.makeText(
            context,
            getString(tossaro.android.core.R.string.tap_to_minimize),
            Toast.LENGTH_SHORT
        ).show()

        Handler(Looper.getMainLooper()).postDelayed({
            doubleBackToExitPressedOnce = false
        }, 2000)
        return false
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = this
        binding.vm = viewModel.also {
            it.loadingIndicator.observe(viewLifecycleOwner, ::showFullLoading)
            it.alertMessage.observe(viewLifecycleOwner, ::showToast)
            it.stocks.observe(viewLifecycleOwner, ::notifyStocksAdapter)
        }
        binding.swipeContainer.setOnRefreshListener {
            viewModel.stocks.value = mutableListOf()
            getStocks(1)
        }
        mAdapter = StockAdapter()
        mAdapter?.onClick = { c, v ->
            goTo(ExampleRouters.STOCK_DETAIL.replace("{coin}", c).replace("{value}", v))
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

    private fun getStocks(p: Int) {
        Timber.d("getStocks $p")
        page = p
        val isFromNetwork = (requireActivity() as BaseActivity<*>).isInternetAvailable()
        if (viewModel.isFromNetwork != isFromNetwork) {
            page = 1
            mAdapter?.stocks?.let { ss ->
                ss.forEach { s -> unsubscribe(s.name.toString()) }
                mAdapter?.notifyItemRangeRemoved(0, ss.size)
                ss.clear()
            }
        }
        viewModel.isFromNetwork = isFromNetwork
        if (webSocketClient.isClosed && isFromNetwork) webSocketClient.reconnect()
        viewModel.getStocks(page)
    }

    private fun notifyStocksAdapter(stocks: List<Stock>?) {
        stocks?.let { ss ->
            mAdapter?.stocks?.let { s ->
                val sizeBefore = s.size
                s.addAll(ss)
                val sizeAfter = s.size - 1
                Timber.d("notify stocks adapter $sizeBefore - $sizeAfter")
                mAdapter?.notifyItemRangeChanged(sizeBefore, sizeAfter)
            }
            binding.swipeContainer.isRefreshing = false
            ss.forEach { s -> if (webSocketClient.isOpen) subscribe(s.name.toString()) }
        }
    }

    private fun createWebSocketClient() {
        webSocketClient = object : WebSocketClient(URI(BuildConfig.SOCKET_V1)) {
            override fun onOpen(handshakedata: ServerHandshake?) {
                Timber.d("onOpen")
            }

            override fun onMessage(message: String?) {
                Timber.d("onMessage: $message")
                onMessageSocket(message)
            }

            override fun onClose(code: Int, reason: String?, remote: Boolean) {
                Timber.d("onClose")
                mAdapter?.stocks?.let { ss ->
                    ss.forEach { s -> unsubscribe(s.name.toString()) }
                }
            }

            override fun onError(ex: Exception?) {
                Timber.d("onError: ${ex?.message}")
            }
        }
        val socketFactory: SSLSocketFactory = SSLSocketFactory.getDefault() as SSLSocketFactory
        webSocketClient.setSocketFactory(socketFactory)
        webSocketClient.connect()
    }

    private fun onMessageSocket(message: String?) {
        activity?.runOnUiThread {
            val usd = Gson().fromJson(message, StockSocket::class.java)
            if (usd?.TOPTIERFULLVOLUME != null && viewModel.loadingIndicator.value == false) {
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
}