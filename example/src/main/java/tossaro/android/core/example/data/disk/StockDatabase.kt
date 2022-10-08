package tossaro.android.core.example.data.disk

import androidx.room.Database
import androidx.room.RoomDatabase
import tossaro.android.core.example.data.disk.dao.StockDao
import tossaro.android.core.example.domain.stock.entity.Stock

@Database(entities = [Stock::class], version = 1, exportSchema = false)
abstract class StockDatabase : RoomDatabase() {
    abstract fun stockDao(): StockDao
}