@file:Suppress("unused")

package tossaro.android.core.app.common

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

abstract class BaseViewModel : ViewModel() {
    val loadingIndicator = MutableLiveData<Boolean>()
    val toastMessage = MutableLiveData<String>()
    val successMessage = MutableLiveData<String>()
    val errorMessage = MutableLiveData<String>()
    val onSignedIn = MutableLiveData<String>()

    init {
        loadingIndicator.value = false
    }
}