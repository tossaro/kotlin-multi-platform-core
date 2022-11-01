package multi.platform.core.shared.app.splash

import android.os.Bundle
import android.view.View
import multi.platform.core.shared.R
import multi.platform.core.shared.app.common.BaseActivity
import multi.platform.core.shared.app.common.BaseFragment
import multi.platform.core.shared.databinding.SplashFragmentBinding

class SplashFragment : BaseFragment<SplashFragmentBinding>(R.layout.splash_fragment) {
    override fun showActionBar() = false
    override fun isFullScreen() = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvVersionValue.text = (requireActivity() as BaseActivity).appVersion()
    }
}