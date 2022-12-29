package multi.platform.example_lib.shared.app.profile

import io.ktor.client.call.*
import io.ktor.client.plugins.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import multi.platform.core.shared.app.common.BaseViewModel
import multi.platform.example_lib.shared.domain.profile.entity.Profile
import multi.platform.example_lib.shared.domain.profile.usecase.GetProfileUseCase

@Suppress("kotlin:S6305")
class ProfileViewModel(
    private val getProfileUseCase: GetProfileUseCase,
) : BaseViewModel() {
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
                onServerError(e)
            }
        }
    }
}