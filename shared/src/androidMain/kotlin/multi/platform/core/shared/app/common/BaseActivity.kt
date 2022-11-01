@file:Suppress("SameReturnValue")

package multi.platform.core.shared.app.common

import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.internal.ViewUtils.dpToPx
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import multi.platform.core.shared.BuildConfig
import multi.platform.core.shared.R
import multi.platform.core.shared.databinding.BaseActivityBinding
import multi.platform.core.shared.external.utility.LocaleUtil
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import timber.log.Timber

abstract class BaseActivity : AppCompatActivity(), KoinComponent {

    private var _binding: BaseActivityBinding? = null
    protected val binding get() = _binding!!
    private var doubleBackToExitPressedOnce = false
    private lateinit var appBarConfiguration: AppBarConfiguration
    private var snackbar: Snackbar? = null

    /**
     * Open function for version of application
     */
    open fun appVersion(): String? = null

    /**
     * Open function for container navigation host binding
     */
    open fun navHostFragment() = binding.contentFragment.getFragment() as NavHostFragment

    /**
     * Open function for application orientation
     * Default: ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
     */
    open fun orientation() = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

    /**
     * Open function for override debug navigation stack
     * Default: false
     */
    open fun isDebugNavStack() = false


    /**
     * Open function for override navigation graph
     * Default: null
     */
    open fun navGraph(): Int? = null

    /**
     * Open function for override default top level app route config
     * Default: no top route
     */
    open fun topLevelDestinations() = setOf<Int>()

    /**
     * Open function for override action bar binding
     * Default: null
     */
    open fun clRoot() = binding.rootView

    /**
     * Open function for override action bar binding
     * Default: null
     */
    open fun actionBar() = binding.toolbar

    /**
     * Open function for override action bar search binding
     * Default: null
     */
    open fun actionBarSearch() = binding.etToolbarSearch

    /**
     * Open function for override action bar layout binding
     * Default: null
     */
    open fun actionBarLayout() = binding.ablToolbar

    /**
     * Open function for override action bar collapsing layout binding
     * Default: null
     */
    open fun actionBarCollapsingLayout() = binding.ctlToolbar

    /**
     * Open function for override action bar viewpager binding
     * Default: null
     */
    open fun actionBarViewPager() = binding.vpToolbar

    /**
     * Open function for override action bar tab layout for dot indicator binding
     * Default: null
     */
    open fun actionBarDotIndicator() = binding.tlToolbar

    /**
     * Open function for override action bar info layout binding
     * Default: null
     */
    open fun actionBarInfoLayout() = binding.clToolbarInfo

    /**
     * Open function for override action bar info icon binding
     * Default: null
     */
    open fun actionBarInfoIcon() = binding.ivToolbarInfo

    /**
     * Open function for override action bar info text binding
     * Default: null
     */
    open fun actionBarInfoText() = binding.tvToolbarInfo

    /**
     * Open function for override bottom navigation bar binding
     * Default: null
     */
    open fun bottomNavBar() = binding.bottomNav

    /**
     * Open function for override night mode
     * Default: MODE_NIGHT_NO
     */
    open fun nightMode() = AppCompatDelegate.MODE_NIGHT_NO

    /**
     * Function for check internet availability
     * @return boolean
     */
    @Suppress("Unused", "MissingPermission", "kotlin:S1874")
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

    /**
     * Open function for snackbar message
     */
    @Suppress("RestrictedApi")
    open fun showSnackbar(messageString: String, isError: Boolean, anchor: View? = null) {
        val anchorView: View = anchor ?: clRoot()
        anchorView.let { a ->
            snackbar?.dismiss()
            snackbar = Snackbar.make(a, messageString, Snackbar.LENGTH_LONG).apply {
                val dp11 = dpToPx(context, 11).toInt()
                val dp24 = dpToPx(context, 24).toInt()
                val dp32 = dpToPx(context, 32).toInt()
                view.background = ContextCompat.getDrawable(
                    context,
                    if (isError) R.drawable.bg_redlight_round8 else R.drawable.bg_greenlight_round8
                )
                if (anchorView is CoordinatorLayout) {
                    view.layoutParams =
                        (view.layoutParams as CoordinatorLayout.LayoutParams).apply {
                            gravity = Gravity.TOP
                            setMargins(dp24, dp32, dp24, 0)
                        }
                } else if (anchorView is FrameLayout) {
                    view.layoutParams =
                        (view.layoutParams as FrameLayout.LayoutParams).apply {
                            gravity = Gravity.TOP
                            setMargins(dp24, dp32, dp24, 0)
                        }
                }
                val textView =
                    view.findViewById<TextView?>(com.google.android.material.R.id.snackbar_text)
                textView?.let { t ->
                    t.setTextAppearance(R.style.Text_Poppins_Body3_Title)
                    t.setCompoundDrawablesWithIntrinsicBounds(
                        if (isError) R.drawable.ic_info else R.drawable.ic_check,
                        0,
                        0,
                        0
                    )
                    t.setBackgroundResource(android.R.color.transparent)
                    t.compoundDrawablePadding = dp11
                }
                animationMode = BaseTransientBottomBar.ANIMATION_MODE_FADE
            }
            snackbar?.show()
        }
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(LocaleUtil.onAttach(newBase))
    }

    @Suppress("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = orientation()
        AppCompatDelegate.setDefaultNightMode(nightMode())

        _binding = DataBindingUtil.setContentView(this, R.layout.base_activity)

        appBarConfiguration = AppBarConfiguration(
            topLevelDestinationIds = topLevelDestinations(),
            fallbackOnNavigateUpListener = ::onSupportNavigateUp
        )

        val navController = navHostFragment().navController
        navController.addOnDestinationChangedListener { controller, destination, _ ->
            if (appBarConfiguration.topLevelDestinations.contains(destination.id)) {
                actionBar().navigationIcon = null
            } else {
                actionBar().setNavigationIcon(R.drawable.ic_caret_left_circle)
            }
            @Suppress("RestrictedApi")
            if (BuildConfig.DEBUG && isDebugNavStack()) {
                val breadcrumb = controller
                    .backQueue
                    .map {
                        it.destination
                    }
                    .joinToString(" > ") {
                        it.displayName.split('/')[1]
                    }
                Timber.d("breadcrumb: $breadcrumb")
                Toast.makeText(this, breadcrumb, Toast.LENGTH_SHORT).show()
            }
        }

        actionBar().apply {
            setSupportActionBar(this)
        }
        bottomNavBar().apply {
            setupWithNavController(navController)
        }

        lifecycleScope.launch {
            delay(1500)
            navGraph()?.let {
                navController.graph.clear()
                navController.setGraph(it)
            }
        }
    }

    override fun onBackPressed() {
        if (navHostFragment().childFragmentManager.backStackEntryCount == 0) {
            if (doubleBackToExitPressedOnce) {
                finish()
            }

            doubleBackToExitPressedOnce = true
            Toast.makeText(
                this,
                getString(R.string.tap_to_minimize),
                Toast.LENGTH_SHORT
            ).show()

            Handler(Looper.getMainLooper()).postDelayed({
                doubleBackToExitPressedOnce = false
            }, 2000)
        } else super.onBackPressed()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        navHostFragment().findNavController().handleDeepLink(intent)
    }
}