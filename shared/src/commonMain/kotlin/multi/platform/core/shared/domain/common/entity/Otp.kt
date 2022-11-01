package multi.platform.core.shared.domain.common.entity

import kotlinx.serialization.Serializable

@Serializable
data class Otp(
    var duration: Long? = null,
    var msisdn: String? = null,
)