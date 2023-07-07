package multi.platform.core.shared.app.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import multi.platform.core.shared.BuildConfig
import multi.platform.core.shared.data.common.network.response.CoreResponse
import multi.platform.core.shared.domain.common.entity.Ticket
import multi.platform.core.shared.external.utility.Validation

@Suppress("Unused", "kotlin:S6305")
actual abstract class CoreViewModel : ViewModel() {
    actual var errorEmptyField: String? = null
    actual var errorMinChar: String? = null
    actual var errorPhoneFormat: String? = null
    actual var errorEmailFormat: String? = null
    actual var accessToken: String? = null
    actual val isEmpty = MutableStateFlow(false)

    actual val loadingIndicator = MutableStateFlow<Boolean?>(false)
    actual val toastMessage = MutableStateFlow<String?>(null)
    actual val successMessage = MutableStateFlow<String?>(null)
    actual val errorMessage = MutableStateFlow<String?>(null)
    actual val onSignedIn = MutableStateFlow<Ticket?>(null)
    actual val forceSignout = MutableStateFlow(false)
    actual val onServerError = MutableStateFlow(false)
    actual val scope = viewModelScope

    actual fun onServerError(e: Exception) {
        scope.launch {
            if (BuildConfig.DEBUG) e.printStackTrace()
            try {
                if (e is ClientRequestException) {
                    val resp: CoreResponse<String?>? = e.response.body()
                    resp?.let { errorMessage.value = resp.meta?.message.toString() }
                    if (e.response.status.value == 401) forceSignout.value = true
                } else onServerError.value = true
            } catch (e2: Exception) {
                if (BuildConfig.DEBUG) e2.printStackTrace()
                onServerError.value = true
            }
            loadingIndicator.value = false
            isEmpty.value = true
        }
    }

    actual fun validateBlank(
        field: MutableStateFlow<String?>,
        error: MutableStateFlow<String?>
    ): Boolean {
        val check = Validation.notBlank(field.value) == false
        error.value = if (check) errorEmptyField else null
        return check
    }

    actual fun validateMinChar(
        min: Int,
        field: MutableStateFlow<String?>,
        error: MutableStateFlow<String?>
    ): Boolean {
        val check = Validation.minCharacter(min, field.value) == false
        error.value = if (check) errorMinChar else null
        return check
    }

    actual fun validatePhoneFormat(
        field: MutableStateFlow<String?>,
        error: MutableStateFlow<String?>
    ): Boolean {
        val check = Validation.phoneFormat(field.value) == false
        error.value = if (check) errorPhoneFormat else null
        return check
    }

    actual fun validateEmailFormat(
        field: MutableStateFlow<String?>,
        error: MutableStateFlow<String?>
    ): Boolean {
        val check = Validation.emailFormat(field.value) == false
        error.value = if (check) errorEmailFormat else null
        return check
    }
}