package tossaro.android.core.app.splash

import tossaro.android.core.R
import tossaro.android.core.app.BaseFragment
import tossaro.android.core.databinding.SplashFragmentBinding

class SplashFragment : BaseFragment<SplashFragmentBinding>(R.layout.splash_fragment) {
    override fun isActionBarShown() = false
    override fun isBottomNavBarShown() = false
    override fun isFullScreen() = true
}