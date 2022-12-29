package multi.platform.example_lib.shared.app.stockedit

import kotlinx.coroutines.flow.MutableStateFlow
import multi.platform.core.shared.app.common.BaseViewModel

@Suppress("kotlin:S6305")
class StockEditViewModel : BaseViewModel() {
    val field1 = MutableStateFlow<String?>(null)
    val field1Error = MutableStateFlow<String?>(null)
    val field2 = MutableStateFlow<String?>(null)
    val field2Error = MutableStateFlow<String?>(null)
    val field3 = MutableStateFlow<String?>(null)
    val field3Error = MutableStateFlow<String?>(null)
    val field4 = MutableStateFlow<String?>(null)
    val field4Error = MutableStateFlow<String?>(null)
}