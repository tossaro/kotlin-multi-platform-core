package multi.platform.core.shared.app.common

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import multi.platform.core.shared.domain.common.entity.Ticket

@Suppress("kotlin:S6305")
expect abstract class BaseViewModel() {
    var errorEmptyField: String?
    var errorMinChar: String?
    var errorPhoneFormat: String?
    var errorEmailFormat: String?
    var accessToken: String?
    val isEmpty: MutableStateFlow<Boolean>

    val loadingIndicator: MutableStateFlow<Boolean?>
    val toastMessage: MutableStateFlow<String?>
    val successMessage: MutableStateFlow<String?>
    val errorMessage: MutableStateFlow<String?>
    val onSignedIn: MutableStateFlow<Ticket?>
    val forceSignout: MutableStateFlow<Boolean>
    val onServerError: MutableStateFlow<Boolean>
    val scope: CoroutineScope

    fun onServerError(e: Exception)

    fun validateBlank(field: MutableStateFlow<String?>, error: MutableStateFlow<String?>): Boolean
    fun validateMinChar(
        min: Int,
        field: MutableStateFlow<String?>,
        error: MutableStateFlow<String?>
    ): Boolean

    fun validatePhoneFormat(
        field: MutableStateFlow<String?>,
        error: MutableStateFlow<String?>
    ): Boolean

    fun validateEmailFormat(
        field: MutableStateFlow<String?>,
        error: MutableStateFlow<String?>
    ): Boolean
}