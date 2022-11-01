package multi.platform.core.example.domain.stock.usecase

import multi.platform.core.example.domain.stock.StockRepository
import multi.platform.core.example.domain.stock.entity.Stock
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SetStocksLocalUseCase : KoinComponent {
    private val stockRepository: StockRepository by inject()
    suspend operator fun invoke(stocks: MutableList<Stock>) = stockRepository.setStocksLocal(stocks)
}