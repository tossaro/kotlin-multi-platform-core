package multi.platform.example_lib.shared.app.onboarding

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import multi.platform.core.shared.app.common.BaseViewModel

class OnBoardingViewModel : BaseViewModel() {
    val onNext = MutableStateFlow(false)

    fun next() {
        scope.launch {
            loadingIndicator.value = true
            delay(750)
            loadingIndicator.value = false
            onNext.value = true
        }
    }
}