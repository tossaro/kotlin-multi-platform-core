package tossaro.android.core.example.app.stockdetail

import android.os.Bundle
import android.view.View
import org.koin.androidx.viewmodel.ext.android.viewModel
import tossaro.android.core.app.BaseDialogFragment
import tossaro.android.core.example.R
import tossaro.android.core.example.databinding.StockDetailDialogFragmentBinding

class StockDetailDialogFragment : BaseDialogFragment<StockDetailDialogFragmentBinding>(
    R.layout.stock_detail_dialog_fragment
) {
    private val viewModel: StockDetailDialogViewModel by viewModel()

    override fun isCancelable() = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = this
        binding.vm = viewModel
        arguments?.let {
            viewModel.coin.value = it.getString("coin")
            viewModel.vall.value = it.getString("value")
        }
    }
}