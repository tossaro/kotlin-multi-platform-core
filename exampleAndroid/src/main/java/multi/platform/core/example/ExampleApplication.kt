package multi.platform.core.example

import android.app.Application
import android.provider.Settings
import androidx.room.Room
import multi.platform.core.example.app.profile.ProfileViewModel
import multi.platform.core.example.app.stockdetailsheet.StockDetailSheetViewModel
import multi.platform.core.example.app.stocklist.StockListViewModel
import multi.platform.core.example.data.profile.ProfileRepositoryImpl
import multi.platform.core.example.data.stock.StockRepositoryImpl
import multi.platform.core.example.data.stock.disk.StockDatabase
import multi.platform.core.example.data.stock.disk.dao.StockDao
import multi.platform.core.example.domain.profile.ProfileRepository
import multi.platform.core.example.domain.profile.usecase.GetProfileUseCase
import multi.platform.core.example.domain.stock.StockRepository
import multi.platform.core.example.domain.stock.usecase.GetStocksLocalUseCase
import multi.platform.core.example.domain.stock.usecase.GetStocksUseCase
import multi.platform.core.example.domain.stock.usecase.SetStocksLocalUseCase
import multi.platform.core.shared.CoreApplication
import multi.platform.core.shared.external.utility.ApiClient
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

class ExampleApplication : CoreApplication() {

    private fun provideStockDb(application: Application): StockDatabase {
        return Room.databaseBuilder(application, StockDatabase::class.java, "stocks")
            .fallbackToDestructiveMigration()
            .build()
    }

    private fun provideStockDao(database: StockDatabase): StockDao {
        return database.stockDao()
    }

    @Suppress("HardwareIds")
    override fun koinModule(): Module {
        return module {
            single {
                ApiClient(
                    get(),
                    get(),
                    sharedPrefsName(),
                    BuildConfig.SERVER,
                    Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID),
                    getString(R.string.app_version),
                )
            }

            singleOf(::provideStockDb)
            singleOf(::provideStockDao)

            single<StockRepository> { StockRepositoryImpl(get(), get()) }
            singleOf(::GetStocksUseCase)
            singleOf(::GetStocksLocalUseCase)
            singleOf(::SetStocksLocalUseCase)

            viewModelOf(::StockListViewModel)
            viewModelOf(::StockDetailSheetViewModel)

            //profile
            single<ProfileRepository> { ProfileRepositoryImpl(get()) }
            singleOf(::GetProfileUseCase)
            viewModelOf(::ProfileViewModel)
        }
    }
}