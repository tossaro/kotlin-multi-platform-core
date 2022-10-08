package tossaro.android.core.app.splash

import android.os.Bundle
import android.view.View
import tossaro.android.core.R
import tossaro.android.core.app.BaseActivity
import tossaro.android.core.app.BaseFragment
import tossaro.android.core.databinding.SplashFragmentBinding

class SplashFragment : BaseFragment<SplashFragmentBinding>(R.layout.splash_fragment) {
    override fun showActionBar() = false
    override fun isFullScreen() = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvVersionValue.text = (requireActivity() as BaseActivity).appVersion()
    }
}