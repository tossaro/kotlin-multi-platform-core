package multi.platform.example_lib.shared.app.stockdetailsheet

import kotlinx.coroutines.flow.MutableStateFlow
import multi.platform.core.shared.app.common.BaseViewModel

class StockDetailSheetViewModel : BaseViewModel() {
    val coin = MutableStateFlow<String?>(null)
    val vall = MutableStateFlow<String?>(null)
}