package multi.platform.core.example.data.stock.disk

import androidx.room.Database
import androidx.room.RoomDatabase
import multi.platform.core.example.data.stock.disk.dao.StockDao
import multi.platform.core.example.domain.stock.entity.Stock

@Database(entities = [Stock::class], version = 1, exportSchema = false)
abstract class StockDatabase : RoomDatabase() {
    abstract fun stockDao(): StockDao
}