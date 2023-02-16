package multi.platform.example_lib.shared.app.signoutdialog

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import multi.platform.example_lib.shared.R
import multi.platform.example_lib.shared.databinding.SignoutDialogFragmentBinding
import multi.platform.core.shared.app.common.BaseDialogFragment
import multi.platform.core.shared.external.constant.AppConstant
import multi.platform.example_lib.shared.external.constant.ExampleConstant

class SignOutDialogFragment :
    BaseDialogFragment<SignoutDialogFragmentBinding>(R.layout.signout_dialog_fragment) {
    override fun isCancelable() = false
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnSignout.setOnClickListener {
            sharedPreferences.edit().remove(AppConstant.ACCESS_TOKEN_KEY)
                .remove(AppConstant.REFRESH_TOKEN_KEY).apply()
            setFragmentResult(ExampleConstant.SIGN_OUT, bundleOf(ExampleConstant.SIGN_OUT to true))
            findNavController().navigateUp()
        }
        binding.btnCancel.setOnClickListener {
            findNavController().navigateUp()
        }
    }
}