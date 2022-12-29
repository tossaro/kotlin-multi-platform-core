package multi.platform.example_lib.shared.domain.profile

import multi.platform.core.shared.data.common.network.response.ApiResponse
import multi.platform.example_lib.shared.domain.profile.entity.Profile

interface ProfileRepository {
    suspend fun getProfile(accessToken: String?): ApiResponse<Profile?>?
}