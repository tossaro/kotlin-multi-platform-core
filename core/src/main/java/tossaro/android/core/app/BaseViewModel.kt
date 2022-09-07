package tossaro.android.core.app

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

abstract class BaseViewModel : ViewModel() {
    val loadingIndicator = MutableLiveData<Boolean>()

    @Suppress("UNUSED")
    val alertMessage = MutableLiveData<String>()

    @Suppress("UNUSED")
    val onSignedIn = MutableLiveData<String>()

    @Suppress("UNUSED")
    val sharedPrefs = MutableLiveData<SharedPreferences>()

    init {
        loadingIndicator.value = false
    }
}