package com.domain.example

import android.app.Application
import android.core.CoreApplication
import android.core.external.utils.NetworkUtil
import androidx.room.Room
import com.domain.example.modules.splash.SplashViewModel
import com.domain.example.modules.stocklist.StockListViewModel
import com.domain.example.data.cache.storage.StockDatabase
import com.domain.example.data.cache.storage.dao.StockDao
import com.domain.example.data.network.StockServiceV1
import com.domain.example.data.StockRepositoryImpl
import com.domain.example.domain.stock.StockRepository
import com.domain.example.domain.stock.usecase.GetStocksUseCase
import com.domain.example.domain.stock.usecase.GetStocksLocalUseCase
import com.domain.example.domain.stock.usecase.SetStocksLocalUseCase
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

class ExampleApplication : CoreApplication() {

    private fun provideStockDb(application: Application): StockDatabase {
        return Room.databaseBuilder(application, StockDatabase::class.java, "stocks")
            .fallbackToDestructiveMigration()
            .build()
    }

    private fun provideStockDao(database: StockDatabase): StockDao {
        return  database.stockDao()
    }

    override fun module() = module {
        singleOf(::provideStockDb)
        singleOf(::provideStockDao)

        single { NetworkUtil.buildClient(get()) }
        single { NetworkUtil.buildService<StockServiceV1>(BuildConfig.SERVER, get()) }

        singleOf(::StockRepositoryImpl) { bind<StockRepository>() }
        singleOf(::GetStocksUseCase)
        singleOf(::GetStocksLocalUseCase)
        singleOf(::SetStocksLocalUseCase)

        viewModelOf(::SplashViewModel)
        viewModelOf(::StockListViewModel)
    }
}