package multi.platform.example_lib.shared.domain.stock.usecase

import multi.platform.example_lib.shared.domain.stock.StockRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GetStocksUseCase : KoinComponent {
    private val stockRepository: StockRepository by inject()
    suspend operator fun invoke(offset: Int, limit: Int) = stockRepository.getStocks(offset, limit)
}