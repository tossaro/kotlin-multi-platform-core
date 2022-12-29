package multi.platform.example_lib.shared.data.profile

import io.ktor.client.call.*
import io.ktor.client.request.*
import multi.platform.core.shared.data.common.network.response.ApiResponse
import multi.platform.core.shared.external.utility.ApiClient
import multi.platform.example_lib.shared.domain.profile.ProfileRepository
import multi.platform.example_lib.shared.domain.profile.entity.Profile

class ProfileRepositoryImpl(
    private val apiClient: ApiClient
) : ProfileRepository {
    override suspend fun getProfile(accessToken: String?): ApiResponse<Profile?>? =
        apiClient.client.get("/api/traveller/v1/profile") {
            accessToken?.let { bearerAuth(it) }
        }.body()
}