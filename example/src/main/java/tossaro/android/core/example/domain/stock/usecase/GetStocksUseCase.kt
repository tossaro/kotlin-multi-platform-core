package tossaro.android.core.example.domain.stock.usecase

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import tossaro.android.core.example.domain.stock.StockRepository

class GetStocksUseCase : KoinComponent {
    private val stockRepository: StockRepository by inject()
    suspend operator fun invoke(offset: Int, limit: Int) = stockRepository.getStocks(offset, limit)
}