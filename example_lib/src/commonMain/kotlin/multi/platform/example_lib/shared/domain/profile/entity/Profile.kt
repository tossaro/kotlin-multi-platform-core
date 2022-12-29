package multi.platform.example_lib.shared.domain.profile.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Profile(
    val id: Int? = null,
    val fullname: String? = null,
    val email: String? = null,
    val msisdn: String? = null,
    val bio: String? = null,
    val picture: String? = null,
    @SerialName("is_email_verified") val isVerified: Boolean?,
)