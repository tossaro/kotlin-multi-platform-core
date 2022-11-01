package multi.platform.core.shared.domain.common.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Session(
    var username: String? = null,
    var msisdn: String? = null,
    var role: String? = null,
    var id: String? = null,
    var email: String? = null,
    var fullname: String? = null,
    var expired: Int? = null,
    var status: Int? = null,
    var token: String? = null,
    @SerialName("refresh_token") var refreshToken: String? = null,
    @SerialName("is_msisdn_verified") var isMsisdnVerified: Boolean? = null,
    @SerialName("is_email_verified") var isEmailVerified: Boolean? = null,
    @SerialName("is_account_verified") var isAccountVerified: Boolean? = null,
    var permissions: List<String>? = null,
)