package com.domain.example.data

import com.domain.example.data.cache.storage.dao.StockDao
import com.domain.example.domain.stock.entity.Stock
import com.domain.example.data.network.StockServiceV1
import com.domain.example.domain.stock.StockRepository

class StockRepositoryImpl(
    private val stockServiceV1: StockServiceV1,
    private val stockDao: StockDao
) : StockRepository {
    override suspend fun getStocks(limit: Int, page: Int) = stockServiceV1.stocks(limit, page, "USD")
    override suspend fun getStocksLocal(offset: Int, limit: Int): List<Stock> = stockDao.load(offset, limit)
    override suspend fun setStocksLocal(stocks: MutableList<Stock>) = stockDao.save(stocks)
}