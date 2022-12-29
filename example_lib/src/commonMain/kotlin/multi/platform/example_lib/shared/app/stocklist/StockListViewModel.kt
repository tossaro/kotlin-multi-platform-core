package multi.platform.example_lib.shared.app.stocklist

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import multi.platform.core.shared.DecimalFormat
import multi.platform.core.shared.app.common.BaseViewModel
import multi.platform.core.shared.external.constant.AppConstant
import multi.platform.example_lib.shared.domain.stock.entity.Stock
import multi.platform.example_lib.shared.domain.stock.usecase.GetStocksLocalUseCase
import multi.platform.example_lib.shared.domain.stock.usecase.GetStocksUseCase
import multi.platform.example_lib.shared.domain.stock.usecase.SetStocksLocalUseCase
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class StockListViewModel : KoinComponent, BaseViewModel() {
    private val getStocksUseCase: GetStocksUseCase by inject()
    private val getStocksLocalUseCase: GetStocksLocalUseCase by inject()
    private val setStocksLocalUseCase: SetStocksLocalUseCase by inject()

    var page = 1
    var stocks = MutableStateFlow<MutableList<Stock>>(mutableListOf())
    var isFromNetwork = true
    var notif = MutableStateFlow<Int?>(null)
    var promo = MutableStateFlow<Int?>(null)

    init {
        getNotif()
        getPromo()
    }

    private fun getNotif() {
        scope.launch {
            delay(1000)
            notif.value = 12
        }
    }

    private fun getPromo() {
        scope.launch {
            delay(1000)
            promo.value = 12
        }
    }

    fun getStocks() {
        if (isFromNetwork) getStocksNetwork()
        else getStocksLocal()
    }

    private fun getStocksLocal() {
        scope.launch {
            val stocksLocal = getStocksLocalUseCase(
                AppConstant.LIST_LIMIT * page,
                AppConstant.LIST_LIMIT
            ).toMutableList()
            loadingIndicator.value = false
            stocks.value = stocksLocal
        }
    }

    private fun getStocksNetwork() {
        scope.launch {
            loadingIndicator.value = true
            try {
                val response = getStocksUseCase(AppConstant.LIST_LIMIT, page)
                val stocksTemp = mutableListOf<Stock>()
                response?.data?.let {
                    it.forEach { coin ->
                        val mCoin = coin.coinInfo
                        val df = DecimalFormat()
                        coin.raw?.usd?.topTierVolume24Hour?.let { v ->
                            mCoin.price = df.format(v, 2)
                        }
                        coin.raw?.usd?.change24Hour?.let { v ->
                            mCoin.status = df.format(v, 2)
                        }
                        coin.raw?.usd?.changePCTHour?.let { v ->
                            val m = df.format(v, 2)
                            mCoin.status += " ($m%)"
                        }
                        mCoin.imageUrl = "https://www.cryptocompare.com" + mCoin.imageUrl
                        stocksTemp.add(mCoin)
                    }
                    loadingIndicator.value = false
                    stocks.value = stocksTemp
                    setStocksLocalUseCase(stocksTemp)
                }
            } catch (e: Exception) {
                onServerError(e)
                isFromNetwork = true
                stocks.value = mutableListOf()
                page = 1
                getStocksLocal()
            }
        }
    }
}