package multi.platform.example_lib.shared.domain.stock

import multi.platform.example_lib.shared.data.stock.network.response.StockResponse
import multi.platform.example_lib.shared.domain.stock.entity.Stock

interface StockRepository {
    suspend fun getStocks(limit: Int, page: Int): StockResponse?
    suspend fun getStocksLocal(offset: Int, limit: Int): List<Stock>
    suspend fun setStocksLocal(stocks: MutableList<Stock>)
}