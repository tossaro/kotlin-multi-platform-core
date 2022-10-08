package tossaro.android.core.example.data.network.response

import com.google.gson.annotations.SerializedName
import tossaro.android.core.example.domain.stock.entity.Stock

data class StockResponse(
    @SerializedName("Message") var message: String,
    @SerializedName("Type") var userId: Int,
    @SerializedName("MetaData") var metaData: MetaData,
    @SerializedName("Data") var data: List<Coin>
)

data class MetaData(
    @SerializedName("Count") var count: Int,
)

data class Coin(
    @SerializedName("CoinInfo") var coinInfo: Stock,
    @SerializedName("RAW") var raw: RAW?,
)

data class RAW(
    @SerializedName("USD") val usd: USD
)

data class USD(
    @SerializedName("TOPTIERVOLUME24HOUR") val topTierVolume24Hour: Double,
    @SerializedName("CHANGE24HOUR") val change24Hour: Double,
    @SerializedName("CHANGEPCTHOUR") val changePCTHour: Double
)