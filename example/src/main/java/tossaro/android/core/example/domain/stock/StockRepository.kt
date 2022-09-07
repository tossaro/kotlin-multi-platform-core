package tossaro.android.core.example.domain.stock

import com.haroldadmin.cnradapter.NetworkResponse
import tossaro.android.core.example.data.network.response.ErrorResponse
import tossaro.android.core.example.data.network.response.StockResponse
import tossaro.android.core.example.domain.stock.entity.Stock

interface StockRepository {
    suspend fun getStocks(limit: Int, page: Int): NetworkResponse<StockResponse, ErrorResponse>
    suspend fun getStocksLocal(offset: Int, limit: Int): List<Stock>
    suspend fun setStocksLocal(stocks: MutableList<Stock>)
}