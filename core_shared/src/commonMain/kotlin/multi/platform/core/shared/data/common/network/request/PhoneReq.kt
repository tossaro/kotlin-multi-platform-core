package multi.platform.core.shared.data.common.network.request

import kotlinx.serialization.Serializable

@Serializable
data class PhoneReq(
    val msisdn: String? = null,
)