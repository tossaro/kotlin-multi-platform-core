package multi.platform.core.example.data.stock

import multi.platform.core.example.data.stock.disk.dao.StockDao
import multi.platform.core.example.data.stock.network.response.StockResponse
import multi.platform.core.example.domain.stock.StockRepository
import multi.platform.core.example.domain.stock.entity.Stock
import multi.platform.core.shared.external.utility.ApiClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class StockRepositoryImpl(
    private val apiClient: ApiClient,
    private val stockDao: StockDao
) : StockRepository {
    override suspend fun getStocks(limit: Int, page: Int): StockResponse? =
        apiClient.client.get("/data/top/totaltoptiervolfull?limit=$limit&page=$page&tsym=USD") {
            url { host = "min-api.cryptocompare.com" }
        }.body()

    override suspend fun getStocksLocal(offset: Int, limit: Int): List<Stock> =
        stockDao.load(offset, limit)

    override suspend fun setStocksLocal(stocks: MutableList<Stock>) = stockDao.save(stocks)
}