package android.core.modules

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

abstract class BaseViewModel: ViewModel() {
    val loadingIndicator = MutableLiveData<Boolean>()
    val alertMessage = MutableLiveData<String>()

    init {
        loadingIndicator.value = false
    }
}