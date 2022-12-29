package multi.platform.example_lib.shared.data.stock.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import multi.platform.example_lib.shared.domain.stock.entity.Stock

@Serializable
data class StockResponse(
    @SerialName("Message") var message: String? = null,
    @SerialName("Type") var userId: Int,
    @SerialName("MetaData") var metaData: MetaData,
    @SerialName("Data") var data: List<Coin>
)

@Serializable
data class MetaData(
    @SerialName("Count") var count: Int,
)

@Serializable
data class Coin(
    @SerialName("CoinInfo") var coinInfo: Stock,
    @SerialName("RAW") var raw: RAW?,
)

@Serializable
data class RAW(
    @SerialName("USD") val usd: USD
)

@Serializable
data class USD(
    @SerialName("TOPTIERVOLUME24HOUR") val topTierVolume24Hour: Double,
    @SerialName("CHANGE24HOUR") val change24Hour: Double,
    @SerialName("CHANGEPCTHOUR") val changePCTHour: Double
)