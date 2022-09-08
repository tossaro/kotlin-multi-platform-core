package tossaro.android.core.app

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.ActivityInfo
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import timber.log.Timber
import tossaro.android.core.BuildConfig
import tossaro.android.core.external.constant.AppConstant
import tossaro.android.core.external.utility.LocaleUtil

abstract class BaseActivity<B : ViewDataBinding>(
    @LayoutRes val layoutResId: Int
) : AppCompatActivity(), KoinComponent {

    private var _binding: B? = null
    protected val binding get() = _binding!!

    private lateinit var appBarConfiguration: AppBarConfiguration
    protected val sharedPreferences: SharedPreferences by inject()

    /**
     * Open function for version of application
     */
    open fun appVersion(): String? = null

    /**
     * Abstract function for container navigation host binding
     */
    abstract fun navHostFragment(): NavHostFragment

    /**
     * Open function for application orientation
     * Default: ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
     */
    protected open fun orientation() = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

    /**
     * Open function for override default app locales
     * Default: null (follow system)
     */
    protected open fun defaultLocale() = LocaleUtil.IN

    /**
     * Open function for override debug navigation stack
     * Default: false
     */
    protected open fun isDebugNavStack() = false

    /**
     * Open function for override main navigation graph
     * Default: null
     */
    open fun mainNavGraph(): Int? = null

    /**
     * Open function for override auth navigation graph
     * Default: null
     */
    open fun authNavGraph(): Int? = null

    /**
     * Open function for override default top level app route config
     * Default: no top route
     */
    open fun topLevelDestinations() = setOf<Int>()

    /**
     * Open function for override action bar binding
     * Default: null
     */
    open fun actionBar(): Toolbar? = null

    /**
     * Open function for override action bar configuration
     */
    @SuppressLint("RestrictedApi")
    open fun configureActionBar(actionBar: ActionBar) {
        actionBar.setDisplayShowTitleEnabled(false)
        actionBar.setDisplayHomeAsUpEnabled(false)
    }

    /**
     * Open function for override bottom navigation bar binding
     * Default: null
     */
    open fun bottomNavBar(): BottomNavigationView? = null

    /**
     * Open function for override night mode
     * Default: MODE_NIGHT_NO
     */
    open fun nightMode() = AppCompatDelegate.MODE_NIGHT_NO

    /**
     * Function for check internet availability
     * @return boolean
     */
    @Suppress("UNUSED", "DEPRECATION", "kotlin:S1874")
    fun isInternetAvailable(): Boolean {
        var result = false
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        cm?.run {
            cm.getNetworkCapabilities(cm.activeNetwork)?.run {
                result = when {
                    hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                    hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                    hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                    else -> false
                }
            }
        }
        return result
    }

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = orientation()
        AppCompatDelegate.setDefaultNightMode(nightMode())

        val appLanguage = LocaleUtil.retrieveAppLanguage(applicationContext, defaultLocale())
        LocaleUtil.setLocale(applicationContext, appLanguage)

        _binding = DataBindingUtil.setContentView(this, layoutResId)

        val navController = navHostFragment().navController
        @SuppressLint("RestrictedApi")
        if (BuildConfig.DEBUG && isDebugNavStack()) {
            navController.addOnDestinationChangedListener { controller, _, _ ->
                val breadcrumb = controller
                    .backQueue
                    .map {
                        it.destination
                    }
                    .joinToString(" > ") {
                        it.displayName.split('/')[1]
                    }
                Timber.d("breadcrumb: $breadcrumb")
                Toast.makeText(this, breadcrumb, Toast.LENGTH_LONG).show()
            }
        }

        appBarConfiguration = AppBarConfiguration(
            topLevelDestinationIds = topLevelDestinations(),
            fallbackOnNavigateUpListener = ::onSupportNavigateUp
        )
        actionBar()?.let {
            setSupportActionBar(it)
            supportActionBar?.let(::configureActionBar)
            NavigationUI.setupActionBarWithNavController(
                this,
                navController,
                appBarConfiguration
            )
        }

        bottomNavBar()?.setupWithNavController(navController)

        if (mainNavGraph() != null && authNavGraph() != null) {
            lifecycleScope.launch {
                delay(1500)
                val accessToken = sharedPreferences.getString(AppConstant.ACCESS_TOKEN, null)
                navController.graph.clear()
                navController.setGraph(if (!accessToken.isNullOrEmpty()) mainNavGraph()!! else authNavGraph()!!)
            }
        }
    }

    override fun onSupportNavigateUp() =
        navHostFragment().navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        navHostFragment().findNavController().handleDeepLink(intent)
    }
}