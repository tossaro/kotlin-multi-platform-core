package com.domain.example.modules.splash

import android.content.res.Configuration
import com.domain.example.R
import com.domain.example.databinding.SplashFragmentBinding
import android.core.modules.BaseFragment
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.navigation.Navigation
import org.koin.androidx.viewmodel.ext.android.viewModel

class SplashFragment: BaseFragment<SplashFragmentBinding>(
    R.layout.splash_fragment
) {

    override fun isActionBarShown(): Boolean = false
    override fun isStatusBarTranslucent(): Boolean = true
    override fun isNavigationBarTranslucent(): Boolean = true
    override fun isNightMode(): Int = Configuration.UI_MODE_NIGHT_NO
    val viewModel: SplashViewModel by viewModel()

    override fun bind() {
        super.bind()
        binding.viewModel = viewModel.also {
            it.loadingIndicator.observe(this, ::loadingIndicator)
            it.alertMessage.observe(this, ::showAlert)
            it.nextPage.observe(this, ::nextPage)
        }
    }

    private fun nextPage(next: Boolean) {
        if (next) view?.let { v -> Navigation.findNavController(v).navigate(R.id.actionToStockListFragment) }
    }

}