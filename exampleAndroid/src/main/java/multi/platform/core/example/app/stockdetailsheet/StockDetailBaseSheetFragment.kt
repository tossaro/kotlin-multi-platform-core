package multi.platform.core.example.app.stockdetailsheet

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import multi.platform.core.example.R
import multi.platform.core.example.databinding.StockDetailSheetFragmentBinding
import multi.platform.core.shared.app.common.BaseSheetFragment
import multi.platform.core.shared.external.extension.goTo
import multi.platform.core.shared.external.extension.showErrorSnackbar
import multi.platform.core.shared.external.extension.showSuccessSnackbar
import org.koin.androidx.viewmodel.ext.android.viewModel

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
        binding.vm = vm
        val scope = viewLifecycleOwner.lifecycleScope
        scope.launchWhenResumed { vm.successMessage.collect { showSuccessSnackbar(it) } }
        scope.launchWhenResumed { vm.errorMessage.collect { showErrorSnackbar(it) } }

        arguments?.let {
            vm.coin.value = it.getString("coin")
            vm.vall.value = it.getString("value")
        }
        binding.btnDetail.setOnClickListener {
            goTo(getString(R.string.route_stock_edit).replace("{coin}", vm.coin.value ?: ""))
        }
    }
}