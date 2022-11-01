package multi.platform.core.example.data.stock.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse(
    @SerialName("Response") val response: String? = "",
    @SerialName("HasWarning") val hasWarning: Boolean? = true,
    @SerialName("Message") val message: String? = ""
)