package com.domain.example.domain.stock

import com.domain.example.domain.stock.entity.Stock
import com.domain.example.data.network.response.ErrorResponse
import com.domain.example.data.network.response.StockResponse
import com.haroldadmin.cnradapter.NetworkResponse

interface StockRepository {
    suspend fun getStocks(limit: Int, page: Int): NetworkResponse<StockResponse, ErrorResponse>
    suspend fun getStocksLocal(offset: Int, limit: Int): List<Stock>
    suspend fun setStocksLocal(stocks: MutableList<Stock>)
}