package tossaro.android.core.example

import android.app.Application
import androidx.room.Room
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.Module
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import tossaro.android.core.CoreApplication
import tossaro.android.core.example.app.stockdetail.StockDetailDialogViewModel
import tossaro.android.core.example.app.stocklist.StockListViewModel
import tossaro.android.core.example.data.StockRepositoryImpl
import tossaro.android.core.example.data.disk.StockDatabase
import tossaro.android.core.example.data.disk.dao.StockDao
import tossaro.android.core.example.data.network.StockServiceV1
import tossaro.android.core.example.domain.stock.StockRepository
import tossaro.android.core.example.domain.stock.usecase.GetStocksLocalUseCase
import tossaro.android.core.example.domain.stock.usecase.GetStocksUseCase
import tossaro.android.core.example.domain.stock.usecase.SetStocksLocalUseCase
import tossaro.android.core.external.utility.NetworkUtil

class ExampleApplication : CoreApplication() {

    private fun provideStockDb(application: Application): StockDatabase {
        return Room.databaseBuilder(application, StockDatabase::class.java, "stocks")
            .fallbackToDestructiveMigration()
            .build()
    }

    private fun provideStockDao(database: StockDatabase): StockDao {
        return database.stockDao()
    }

    override fun koinModule(): Module {
        return module {
            singleOf(::provideStockDb)
            singleOf(::provideStockDao)

            single { NetworkUtil.buildService<StockServiceV1>(BuildConfig.SERVER_V1, get()) }

            singleOf(::StockRepositoryImpl) { bind<StockRepository>() }
            singleOf(::GetStocksUseCase)
            singleOf(::GetStocksLocalUseCase)
            singleOf(::SetStocksLocalUseCase)

            viewModelOf(::StockListViewModel)
            viewModelOf(::StockDetailDialogViewModel)
        }
    }
}