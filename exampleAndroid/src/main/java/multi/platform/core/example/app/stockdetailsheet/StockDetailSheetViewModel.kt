package multi.platform.core.example.app.stockdetailsheet

import androidx.lifecycle.MutableLiveData
import multi.platform.core.shared.app.common.BaseViewModel

class StockDetailSheetViewModel : BaseViewModel() {
    val coin = MutableLiveData<String>()
    val vall = MutableLiveData<String>()
}