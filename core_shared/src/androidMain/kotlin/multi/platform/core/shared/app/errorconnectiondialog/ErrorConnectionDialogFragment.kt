package multi.platform.core.shared.app.errorconnectiondialog

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import multi.platform.core.shared.R
import multi.platform.core.shared.app.common.BaseDialogFragment
import multi.platform.core.shared.databinding.ErrorConnectionDialogFragmentBinding
import multi.platform.core.shared.external.constant.AppConstant


class ErrorConnectionDialogFragment :
    BaseDialogFragment<ErrorConnectionDialogFragmentBinding>(R.layout.error_connection_dialog_fragment) {
    override fun showCloseButton() = false
    override fun isCancelable() = false
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.mbErrorConnectionRetry.setOnClickListener {
            setFragmentResult(
                AppConstant.RETRY_REQ,
                bundleOf(AppConstant.RETRY_REQ to arguments?.getString("key", null))
            )
            dismiss()
        }
    }
}