package tossaro.android.core.example.app.profile

import android.content.Intent
import android.os.Bundle
import android.view.View
import tossaro.android.core.app.BaseFragment
import tossaro.android.core.example.R
import tossaro.android.core.example.app.MainActivity
import tossaro.android.core.example.databinding.ProfileFragmentBinding
import tossaro.android.core.external.constant.AppConstant

class ProfileFragment : BaseFragment<ProfileFragmentBinding>(R.layout.profile_fragment) {
    override fun isActionBarShown() = false
    override fun statusBarColor() = R.color.primary

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnSignOut.setOnClickListener {
            sharedPreferences.edit().remove(AppConstant.ACCESS_TOKEN).remove(AppConstant.REFRESH_TOKEN)
                .apply()
            requireActivity().finish()
            startActivity(Intent(requireContext(), MainActivity::class.java))
        }
    }
}