package multi.platform.example_lib.shared.data.stock

import io.ktor.client.call.*
import io.ktor.client.request.*
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import multi.platform.core.shared.external.utility.ApiClient
import multi.platform.example_lib.shared.data.stock.network.response.StockResponse
import multi.platform.example_lib.shared.domain.stock.StockRepository
import multi.platform.example_lib.shared.domain.stock.entity.Stock
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class StockRepositoryImpl : KoinComponent, StockRepository {
    private val apiClient: ApiClient by inject()
    private val realm: Realm by inject()

    override suspend fun getStocks(limit: Int, page: Int): StockResponse? =
        apiClient.client.get("/data/top/totaltoptiervolfull?limit=$limit&page=$page&tsym=USD") {
            url { host = "min-api.cryptocompare.com" }
        }.body()

    override suspend fun getStocksLocal(offset: Int, limit: Int): List<Stock> =
        realm.query<Stock>().find()

    override suspend fun setStocksLocal(stocks: MutableList<Stock>) = realm.write {
        for (s in stocks) {
            var stock = Stock()
            val exist = query<Stock>("id = '${s.id}'").first().find()
            if (exist != null) stock = exist
            else stock.id = s.id
            stock.fullname = s.fullname
            stock.name = s.name
            stock.price = s.price
            stock.status = s.status
            if (exist == null) copyToRealm(stock)
        }
    }
}