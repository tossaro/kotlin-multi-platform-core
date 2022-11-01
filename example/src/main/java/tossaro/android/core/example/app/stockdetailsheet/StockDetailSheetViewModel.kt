package tossaro.android.core.example.app.stockdetailsheet

import androidx.lifecycle.MutableLiveData
import tossaro.android.core.app.common.BaseViewModel

class StockDetailSheetViewModel : BaseViewModel() {
    val coin = MutableLiveData<String>()
    val vall = MutableLiveData<String>()
}