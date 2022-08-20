package com.domain.example.modules.splash

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import android.core.modules.BaseViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashViewModel : BaseViewModel() {
    var nextPage = MutableLiveData<Boolean>()
    init {
        viewModelScope.launch {
            delay(3000)
            nextPage.value = true
        }
    }
}