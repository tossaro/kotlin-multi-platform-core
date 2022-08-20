package com.domain.example.data.network.response

data class StockSocket(
    val TYPE: String,
    val MESSAGE: String,
    val INFO: String,
    val SYMBOL: String?,
    val TOPTIERFULLVOLUME: String?
)