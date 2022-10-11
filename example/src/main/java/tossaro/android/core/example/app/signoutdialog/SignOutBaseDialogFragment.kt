package tossaro.android.core.example.app.signoutdialog

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import tossaro.android.core.app.common.BaseDialogFragment
import tossaro.android.core.example.R
import tossaro.android.core.example.databinding.SignoutDialogFragmentBinding
import tossaro.android.core.example.external.constant.ExampleConstant
import tossaro.android.core.external.constant.AppConstant

class SignOutBaseDialogFragment :
    BaseDialogFragment<SignoutDialogFragmentBinding>(R.layout.signout_dialog_fragment) {
    override fun isCancelable() = false
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnSignout.setOnClickListener {
            sharedPreferences.edit().remove(AppConstant.ACCESS_TOKEN)
                .remove(AppConstant.REFRESH_TOKEN).apply()
            setFragmentResult(ExampleConstant.SIGN_OUT, bundleOf(ExampleConstant.SIGN_OUT to true))
            findNavController().navigateUp()
        }
        binding.btnCancel.setOnClickListener {
            findNavController().navigateUp()
        }
        showFullLoading(true)
    }
}