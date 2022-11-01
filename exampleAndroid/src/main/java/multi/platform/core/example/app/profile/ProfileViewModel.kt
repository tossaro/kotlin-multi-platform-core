package multi.platform.core.example.app.profile

import multi.platform.core.example.domain.profile.entity.Profile
import multi.platform.core.example.domain.profile.usecase.GetProfileUseCase
import multi.platform.core.shared.app.common.BaseViewModel
import multi.platform.core.shared.domain.common.entity.Meta
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

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
                if (e is ClientRequestException && e.response.status.value in arrayOf(
                        400,
                        401,
                        403
                    )
                ) {
                    val meta: Meta? = e.response.body()
                    meta?.let { errorMessage.value = meta.message.toString() }
                } else errorMessage.value = e.message.toString()
                loadingIndicator.value = false
            }
        }
    }
}