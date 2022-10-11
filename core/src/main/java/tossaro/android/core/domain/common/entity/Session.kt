package tossaro.android.core.domain.common.entity

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class Session(
    @SerializedName("username") var username: String?,
    @SerializedName("msisdn") var msisdn: String?,
    @SerializedName("role") var role: String?,
    @SerializedName("id") var id: String?,
    @SerializedName("email") var email: String?,
    @SerializedName("fullname") var fullname: String?,
    @SerializedName("expired") var expired: Int?,
    @SerializedName("status") var status: Int?,
    @SerializedName("token") var token: String?,
    @SerializedName("refresh_token") var refreshToken: String?,
    @SerializedName("is_msisdn_verified") var isMsisdnVerified: Boolean?,
    @SerializedName("is_email_verified") var isEmailVerified: Boolean?,
    @SerializedName("is_account_verified") var isAccountVerified: Boolean?,
    @SerializedName("permissions") var permissions: List<String>?,
)