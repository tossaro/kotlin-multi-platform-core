package tossaro.android.core.example.app.signin

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import tossaro.android.core.app.BaseFragment
import tossaro.android.core.example.R
import tossaro.android.core.example.databinding.SigninFragmentBinding
import tossaro.android.core.external.constant.AppConstant

class SignInFragment : BaseFragment<SigninFragmentBinding>(R.layout.signin_fragment) {
    override fun isActionBarShown() = false
    override fun isBottomNavBarShown() = false
    override fun isFullScreen() = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnSignIn.setOnClickListener {
            sharedPreferences.edit()
                .putString(AppConstant.ACCESS_TOKEN, "Access123")
                .putString(AppConstant.REFRESH_TOKEN, "Refres123")
                .apply()
            findNavController().graph.clear()
            findNavController().setGraph(R.navigation.main_nav_graph)
        }
    }
}