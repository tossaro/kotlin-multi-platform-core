package multi.platform.core.shared.app.common

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow

@Suppress("kotlin:S6305", "kotlin:S1192")
actual abstract class BaseViewModel {
    actual val loadingIndicator = MutableStateFlow<Boolean?>(null)
    actual val toastMessage = MutableStateFlow<String?>(null)
    actual val successMessage = MutableStateFlow<String?>(null)
    actual val errorMessage = MutableStateFlow<String?>(null)
    actual val onSignedIn = MutableStateFlow<Ticket?>(null)
    actual val scope = viewModelScope
}