package multi.platform.core.shared.data.common

import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import multi.platform.core.shared.data.common.network.request.PhoneReq
import multi.platform.core.shared.data.common.network.response.ApiResponse
import multi.platform.core.shared.domain.common.CommonRepository
import multi.platform.core.shared.domain.common.entity.Ticket
import multi.platform.core.shared.external.utility.ApiClient

class CommonRepositoryImpl(
    private val apiClient: ApiClient,
) : CommonRepository {
    override suspend fun refreshToken(refreshToken: String, phone: String): ApiResponse<Ticket?>? =
        apiClient.client.post("/api/traveller/v1/token") {
            headers.append("x-client-refresh-token", refreshToken)
            contentType(ContentType.Application.Json)
            setBody(PhoneReq(phone.replace("+", "")))
        }.body()
}