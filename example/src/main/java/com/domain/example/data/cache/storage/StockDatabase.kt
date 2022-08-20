package com.domain.example.data.cache.storage

import androidx.room.Database
import androidx.room.RoomDatabase
import com.domain.example.data.cache.storage.dao.StockDao
import com.domain.example.domain.stock.entity.Stock

@Database(entities = [Stock::class], version = 1, exportSchema = false)
abstract class StockDatabase : RoomDatabase() {
    abstract fun stockDao(): StockDao
}