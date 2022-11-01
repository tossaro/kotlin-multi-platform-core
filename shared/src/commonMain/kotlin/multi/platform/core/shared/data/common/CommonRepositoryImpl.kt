package multi.platform.core.shared.data.common

import multi.platform.core.shared.data.common.network.request.PhoneReq
import multi.platform.core.shared.data.common.network.response.ApiResponse
import multi.platform.core.shared.domain.common.CommonRepository
import multi.platform.core.shared.domain.common.entity.Ticket
import multi.platform.core.shared.external.utility.ApiClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

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