package tossaro.android.core.domain.entity

import com.google.gson.annotations.SerializedName

data class Token(
    @SerializedName("token") var token: String,
    @SerializedName("refresh_token") var refresh_token: String,
    @SerializedName("channel_id") var channel_id: String,
    @SerializedName("expire") var expire: Int,
)