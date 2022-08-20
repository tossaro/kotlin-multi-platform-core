package android.core.data.network.response

import com.google.gson.annotations.SerializedName

data class GenericErrorResponse(
    @SerializedName("error_code")
    val errorCode: String? = null,
    @SerializedName("localized_message")
    val localizedMessage: LocalizedMessageResponse? = null,
    @SerializedName("message")
    val message: String? = ""
)