package tossaro.android.core.example.app.stockdetail

import androidx.lifecycle.MutableLiveData
import tossaro.android.core.app.BaseViewModel

class StockDetailDialogViewModel : BaseViewModel() {
    val coin = MutableLiveData<String>()
    val vall = MutableLiveData<String>()
}