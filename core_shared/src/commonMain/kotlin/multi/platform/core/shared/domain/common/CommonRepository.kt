package multi.platform.core.shared.domain.common

import multi.platform.core.shared.data.common.network.response.ApiResponse
import multi.platform.core.shared.domain.common.entity.Ticket

interface CommonRepository {
    suspend fun refreshToken(refreshToken: String, phone: String): ApiResponse<Ticket?>?
}