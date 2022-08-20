package android.core.modules

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.FragmentContainerView
import androidx.navigation.findNavController

abstract class BaseActivity : AppCompatActivity() {

    protected open fun init() = Unit
    abstract fun getNavGraphResource(): Int?
    abstract fun navHostFragment(): FragmentContainerView?
    protected open fun orientation() = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = orientation()
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        init()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        navHostFragment()?.findNavController()?.handleDeepLink(intent)
    }
}