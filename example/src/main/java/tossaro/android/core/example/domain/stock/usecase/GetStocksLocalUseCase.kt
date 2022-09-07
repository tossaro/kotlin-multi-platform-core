package tossaro.android.core.example.domain.stock.usecase

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import tossaro.android.core.example.domain.stock.StockRepository

class GetStocksLocalUseCase : KoinComponent {
    private val stockRepository: StockRepository by inject()
    suspend operator fun invoke(offset: Int, limit: Int) =
        stockRepository.getStocksLocal(offset, limit)
}