package multi.platform.core.example.app.stocklist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import multi.platform.core.example.data.stock.network.response.ErrorResponse
import multi.platform.core.example.domain.stock.entity.Stock
import multi.platform.core.example.domain.stock.usecase.GetStocksLocalUseCase
import multi.platform.core.example.domain.stock.usecase.GetStocksUseCase
import multi.platform.core.example.domain.stock.usecase.SetStocksLocalUseCase
import multi.platform.core.shared.app.common.BaseViewModel
import multi.platform.core.shared.external.constant.AppConstant
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class StockListViewModel(
    private val getStocksUseCase: GetStocksUseCase,
    private val getStocksLocalUseCase: GetStocksLocalUseCase,
    private val setStocksLocalUseCase: SetStocksLocalUseCase,
) : BaseViewModel() {
    var stocks = MutableLiveData<MutableList<Stock>?>()
    var isFromNetwork = true
    var notif = MutableLiveData<Int>()
    var promo = MutableLiveData<Int>()

    init {
        loadingIndicator.value = true
        getNotif()
        getPromo()
    }

    private fun getNotif() {
        viewModelScope.launch {
            delay(1000)
            notif.value = 12
        }
    }

    private fun getPromo() {
        viewModelScope.launch {
            delay(1000)
            promo.value = 12
        }
    }

    fun getStocks(page: Int) {
        if (isFromNetwork) getStocksNetwork(page)
        else getStocksLocal(page)
    }

    private fun getStocksLocal(page: Int) {
        viewModelScope.launch {
            val stocksTemp = mutableListOf<Stock>()
            val stocksLocal = getStocksLocalUseCase(
                AppConstant.LIST_LIMIT * page,
                AppConstant.LIST_LIMIT
            ).toMutableList()
            stocksLocal.forEach { coin -> stocksTemp.add(coin) }
            stocks.value = stocksTemp
            loadingIndicator.value = false
        }
    }

    private fun getStocksNetwork(page: Int) {
        viewModelScope.launch {
            loadingIndicator.value = true
            try {
                val response = getStocksUseCase(AppConstant.LIST_LIMIT, page)
                val stocksTemp = mutableListOf<Stock>()
                response?.data?.let {
                    it.forEach { coin ->
                        val mCoin = coin.coinInfo
                        mCoin.price = String.format("%.5f", coin.raw?.usd?.topTierVolume24Hour)
                        mCoin.status = String.format("%.2f", coin.raw?.usd?.change24Hour)
                        val m = String.format("%.2f", coin.raw?.usd?.changePCTHour)
                        mCoin.status += " ($m%)"
                        stocksTemp.add(mCoin)
                    }
                    stocks.value = stocksTemp
                    setStocksLocalUseCase(stocksTemp)
                }
                loadingIndicator.value = false
            } catch (e: Exception) {
                if (e is ClientRequestException && e.response.status.value in listOf(
                        400,
                        401,
                        403
                    )
                ) {
                    val error: ErrorResponse? = e.response.body()
                    toastMessage.value = error?.message
                }
                isFromNetwork = true
                stocks.value = null
                getStocksLocal(1)
                loadingIndicator.value = false
            }
        }
    }
}