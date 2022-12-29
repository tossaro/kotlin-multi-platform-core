package multi.platform.core.shared.data.common.network.response

import kotlinx.serialization.Serializable
import multi.platform.core.shared.domain.common.entity.Meta

@Suppress("Unused")
@Serializable
data class ApiResponse<D>(
    var meta: Meta? = null,
    var data: D? = null,
)