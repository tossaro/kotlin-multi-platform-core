package multi.platform.core.shared.data.common.network.response

import multi.platform.core.shared.domain.common.entity.Meta
import kotlinx.serialization.Serializable

@Suppress("Unused")
@Serializable
data class ApiResponse<D>(
    var meta: Meta? = null,
    var data: D? = null,
)