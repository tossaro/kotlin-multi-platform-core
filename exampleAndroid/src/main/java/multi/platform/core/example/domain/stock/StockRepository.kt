package multi.platform.core.example.domain.stock

import multi.platform.core.example.data.stock.network.response.StockResponse
import multi.platform.core.example.domain.stock.entity.Stock

interface StockRepository {
    suspend fun getStocks(limit: Int, page: Int): StockResponse?
    suspend fun getStocksLocal(offset: Int, limit: Int): List<Stock>
    suspend fun setStocksLocal(stocks: MutableList<Stock>)
}