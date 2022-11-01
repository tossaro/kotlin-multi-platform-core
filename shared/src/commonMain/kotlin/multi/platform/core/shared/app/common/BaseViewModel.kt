package multi.platform.core.shared.app.common

import multi.platform.core.shared.domain.common.entity.Ticket
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow

@Suppress("kotlin:S6305")
expect abstract class BaseViewModel() {
    val loadingIndicator: MutableStateFlow<Boolean?>
    val toastMessage: MutableStateFlow<String?>
    val successMessage: MutableStateFlow<String?>
    val errorMessage: MutableStateFlow<String?>
    val onSignedIn: MutableStateFlow<Ticket?>
    val scope: CoroutineScope
}