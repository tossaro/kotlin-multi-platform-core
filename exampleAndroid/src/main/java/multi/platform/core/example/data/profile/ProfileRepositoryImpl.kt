package multi.platform.core.example.data.profile

import multi.platform.core.example.domain.profile.ProfileRepository
import multi.platform.core.example.domain.profile.entity.Profile
import multi.platform.core.shared.data.common.network.response.ApiResponse
import multi.platform.core.shared.external.utility.ApiClient
import io.ktor.client.call.body
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get

class ProfileRepositoryImpl(
    private val apiClient: ApiClient
) : ProfileRepository {
    override suspend fun getProfile(accessToken: String?): ApiResponse<Profile?>? =
        apiClient.client.get("/api/traveller/v1/profile") {
            accessToken?.let { bearerAuth(it) }
        }.body()
}