package tossaro.android.core.example.app.stocklist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.haroldadmin.cnradapter.NetworkResponse
import kotlinx.coroutines.launch
import tossaro.android.core.app.BaseViewModel
import tossaro.android.core.example.domain.stock.entity.Stock
import tossaro.android.core.example.domain.stock.usecase.GetStocksLocalUseCase
import tossaro.android.core.example.domain.stock.usecase.GetStocksUseCase
import tossaro.android.core.example.domain.stock.usecase.SetStocksLocalUseCase
import tossaro.android.core.external.constant.AppConstant

class StockListViewModel(
    private val getStocksUseCase: GetStocksUseCase,
    private val getStocksLocalUseCase: GetStocksLocalUseCase,
    private val setStocksLocalUseCase: SetStocksLocalUseCase,
) : BaseViewModel() {
    var stocks = MutableLiveData<MutableList<Stock>?>()
    var isFromNetwork = true

    init {
        loadingIndicator.value = true
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
            when (val response = getStocksUseCase(AppConstant.LIST_LIMIT, page)) {
                is NetworkResponse.Success -> {
                    response.body.data.let {
                        val stocksTemp = mutableListOf<Stock>()
                        it.forEach { coin ->
                            val mCoin = coin.coin_info
                            mCoin.price = String.format("%.5f", coin.raw?.USD?.TOPTIERVOLUME24HOUR)
                            mCoin.status = String.format("%.2f", coin.raw?.USD?.CHANGE24HOUR)
                            val m = String.format("%.2f", coin.raw?.USD?.CHANGEPCTHOUR)
                            mCoin.status += " ($m%)"
                            stocksTemp.add(mCoin)
                        }
                        stocks.value = stocksTemp
                        setStocksLocalUseCase(stocksTemp)
                        loadingIndicator.value = false
                    }
                }
                is NetworkResponse.ServerError -> {
                    isFromNetwork = true
                    stocks.value = null
                    getStocksLocal(1)
                    loadingIndicator.value = false
                    alertMessage.value = response.body?.message.orEmpty()
                }
                is NetworkResponse.NetworkError -> {
                    isFromNetwork = true
                    stocks.value = null
                    getStocksLocal(1)
                    loadingIndicator.value = false
                    alertMessage.value = response.error.message.orEmpty()
                }
            }
        }
    }
}