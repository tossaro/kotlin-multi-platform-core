package com.domain.example.modules.stocklist

import android.core.modules.BaseViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.haroldadmin.cnradapter.NetworkResponse
import com.domain.example.domain.stock.entity.Stock
import com.domain.example.domain.stock.usecase.GetStocksLocalUseCase
import com.domain.example.domain.stock.usecase.GetStocksUseCase
import com.domain.example.domain.stock.usecase.SetStocksLocalUseCase
import kotlinx.coroutines.launch

class StockListViewModel(
    private val getStocksUseCase: GetStocksUseCase,
    private val getStocksLocalUseCase: GetStocksLocalUseCase,
    private val setStocksLocalUseCase: SetStocksLocalUseCase,
) : BaseViewModel() {
    var stocks = MutableLiveData<MutableList<Stock>?>()
    var isFromNetwork = true
    var limit = 50

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
            val stocksLocal = getStocksLocalUseCase(limit * page, limit).toMutableList()
            stocksLocal.forEach { coin -> stocksTemp.add(coin) }
            stocks.value = stocksTemp
            loadingIndicator.value = false
        }
    }

    private fun getStocksNetwork(page: Int) {
        viewModelScope.launch {
            loadingIndicator.value = true
            when (val response = getStocksUseCase(limit, page)) {
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