package tossaro.android.core.example.app.stockdetailsheet

import android.os.Bundle
import android.view.View
import org.koin.androidx.viewmodel.ext.android.viewModel
import tossaro.android.core.app.BaseSheetFragment
import tossaro.android.core.example.ExampleRouters
import tossaro.android.core.example.R
import tossaro.android.core.example.databinding.StockDetailSheetFragmentBinding
import tossaro.android.core.external.extension.goTo
import tossaro.android.core.external.extension.showErrorSnackbar
import tossaro.android.core.external.extension.showSuccessSnackbar

class StockDetailBaseSheetFragment : BaseSheetFragment<StockDetailSheetFragmentBinding>(
    R.layout.stock_detail_sheet_fragment
) {
    private val vm: StockDetailSheetViewModel by viewModel()
    override fun title() = arguments?.getString("coin") ?: ""
    override fun forceFullHeight() = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable = false
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = this
        binding.vm = vm.also {
            it.successMessage.observe(viewLifecycleOwner, ::showSuccessSnackbar)
            it.errorMessage.observe(viewLifecycleOwner, ::showErrorSnackbar)
        }
        arguments?.let {
            vm.coin.value = it.getString("coin")
            vm.vall.value = it.getString("value")
        }
        binding.btnDetail.setOnClickListener {
            goTo(ExampleRouters.STOCK_EDIT.replace("{coin}", vm.coin.value ?: ""))
        }
    }
}