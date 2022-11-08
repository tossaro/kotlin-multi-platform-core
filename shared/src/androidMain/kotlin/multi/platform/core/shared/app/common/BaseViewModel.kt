package multi.platform.core.shared.app.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import multi.platform.core.shared.domain.common.entity.Ticket
import kotlinx.coroutines.flow.MutableStateFlow

@Suppress("Unused", "kotlin:S6305")
actual abstract class BaseViewModel : ViewModel() {
    actual val loadingIndicator = MutableStateFlow<Boolean?>(false)
    actual val toastMessage = MutableStateFlow<String?>(null)
    actual val successMessage = MutableStateFlow<String?>(null)
    actual val errorMessage = MutableStateFlow<String?>(null)
    actual val onSignedIn = MutableStateFlow<Ticket?>(null)
    actual val forceSignout = MutableStateFlow(false)
    actual val onServerError = MutableStateFlow(false)
    actual val scope = viewModelScope
}