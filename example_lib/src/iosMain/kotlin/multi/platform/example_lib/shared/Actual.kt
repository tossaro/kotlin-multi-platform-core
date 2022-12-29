package multi.platform.example_lib.shared

import multi.platform.example_lib.shared.app.onboarding.OnBoardingViewModel
import multi.platform.example_lib.shared.app.profile.ProfileViewModel
import multi.platform.example_lib.shared.app.stockdetailsheet.StockDetailSheetViewModel
import multi.platform.example_lib.shared.app.stockedit.StockEditViewModel
import multi.platform.example_lib.shared.app.stocklist.StockListViewModel
import multi.platform.example_lib.shared.data.profile.ProfileRepositoryImpl
import multi.platform.example_lib.shared.data.stock.StockRepositoryImpl
import multi.platform.example_lib.shared.domain.profile.ProfileRepository
import multi.platform.example_lib.shared.domain.profile.usecase.*
import multi.platform.example_lib.shared.domain.stock.StockRepository
import multi.platform.example_lib.shared.domain.stock.usecase.*
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

actual fun libModule() = module {
    factoryOf(::OnBoardingViewModel)
    singleOf(::provideStockDb)

    single<StockRepository> { StockRepositoryImpl() }
    singleOf(::GetStocksUseCase)
    singleOf(::GetStocksLocalUseCase)
    singleOf(::SetStocksLocalUseCase)

    factoryOf(::StockListViewModel)
    factoryOf(::StockDetailSheetViewModel)
    factoryOf(::StockEditViewModel)

    //profile
    single<ProfileRepository> { ProfileRepositoryImpl(get()) }
    singleOf(::GetProfileUseCase)
    factoryOf(::ProfileViewModel)
}