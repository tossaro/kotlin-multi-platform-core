package multi.platform.core.example.domain.profile

import multi.platform.core.example.domain.profile.entity.Profile
import multi.platform.core.shared.data.common.network.response.ApiResponse

interface ProfileRepository {
    suspend fun getProfile(accessToken: String?): ApiResponse<Profile?>?
}