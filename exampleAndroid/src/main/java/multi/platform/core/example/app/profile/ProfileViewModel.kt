package multi.platform.core.example.app.profile

import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import multi.platform.core.example.domain.profile.entity.Profile
import multi.platform.core.example.domain.profile.usecase.GetProfileUseCase
import multi.platform.core.shared.app.common.BaseViewModel
import multi.platform.core.shared.data.common.network.response.ApiResponse

@Suppress("kotlin:S6305")
class ProfileViewModel(
    private val getProfileUseCase: GetProfileUseCase,
) : BaseViewModel() {
    var accessToken: String? = null
    val user = MutableStateFlow<Profile?>(null)

    fun checkToken() {
        if (accessToken != null) getProfile()
    }

    private fun getProfile() {
        scope.launch {
            loadingIndicator.value = true
            try {
                val response = getProfileUseCase(accessToken)
                loadingIndicator.value = false
                user.value = response?.data
            } catch (e: Exception) {
                e.printStackTrace()
                if (e is ClientRequestException) {
                    val resp: ApiResponse<String?>? = e.response.body()
                    resp?.let { errorMessage.value = resp.meta?.message.toString() }
                    if (e.response.status.value == 401) forceSignout.value = true
                } else if (e is ServerResponseException) onServerError.value = true
                else errorMessage.value = e.message.toString()
                loadingIndicator.value = false
            }
        }
    }
}