package tossaro.android.core.example.data

import tossaro.android.core.example.data.disk.dao.StockDao
import tossaro.android.core.example.data.network.StockServiceV1
import tossaro.android.core.example.domain.stock.StockRepository
import tossaro.android.core.example.domain.stock.entity.Stock

class StockRepositoryImpl(
    private val stockServiceV1: StockServiceV1,
    private val stockDao: StockDao
) : StockRepository {
    override suspend fun getStocks(limit: Int, page: Int) =
        stockServiceV1.stocks(limit, page, "USD")

    override suspend fun getStocksLocal(offset: Int, limit: Int): List<Stock> =
        stockDao.load(offset, limit)

    override suspend fun setStocksLocal(stocks: MutableList<Stock>) = stockDao.save(stocks)
}