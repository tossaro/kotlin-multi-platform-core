package multi.platform.core.shared.domain.common

import multi.platform.core.shared.data.common.network.response.CoreResponse
import multi.platform.core.shared.domain.common.entity.Ticket

@Suppress("kotlin:S6517")
interface CommonRepository {
    suspend fun refreshToken(refreshToken: String, phone: String): CoreResponse<Ticket?>?
}