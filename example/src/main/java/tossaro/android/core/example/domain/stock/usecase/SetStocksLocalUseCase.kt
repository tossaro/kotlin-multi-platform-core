package tossaro.android.core.example.domain.stock.usecase

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import tossaro.android.core.example.domain.stock.StockRepository
import tossaro.android.core.example.domain.stock.entity.Stock

class SetStocksLocalUseCase : KoinComponent {
    private val stockRepository: StockRepository by inject()
    suspend operator fun invoke(stocks: MutableList<Stock>) = stockRepository.setStocksLocal(stocks)
}