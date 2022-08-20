package com.domain.example.modules

import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentContainerView
import android.core.modules.BaseActivity
import com.domain.example.R
import com.domain.example.databinding.SplashActivityBinding

class MainActivity : BaseActivity() {
    private var _binding: SplashActivityBinding? = null
    private val binding get() = _binding!!

    override fun navHostFragment(): FragmentContainerView = binding.contentFragment
    override fun getNavGraphResource(): Int = R.navigation.navigation
    override fun init() {
        _binding = DataBindingUtil.setContentView(this, R.layout.splash_activity)
    }
}