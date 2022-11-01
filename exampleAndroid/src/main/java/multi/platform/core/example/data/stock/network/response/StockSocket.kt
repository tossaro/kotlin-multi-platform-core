package multi.platform.core.example.data.stock.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StockSocket(
    @SerialName("TYPE") val type: String? = null,
    @SerialName("MESSAGE") val message: String? = null,
    @SerialName("INFO") val info: String? = null,
    @SerialName("SYMBOL") val symbol: String? = null,
    @SerialName("TOPTIERFULLVOLUME") val topTierFullVolume: String? = null
)