@file:Suppress("UnUsed")

package multi.platform.core.shared.app.common

import android.content.Context
import android.content.pm.ActivityInfo
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.ui.AppBarConfiguration
import multi.platform.core.shared.external.utility.LocaleUtil
import org.koin.core.component.KoinComponent

abstract class CoreActivity : AppCompatActivity(), KoinComponent {

    lateinit var appBarConfiguration: AppBarConfiguration

    /**
     * Open function for version of application
     */
    open fun appVersion(): String? = null

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
     * Open function for override action bar search hint
     * Default: null
     */
    open fun actionBarSearchHint() = 0

    /**
     * Open function for override action bar auto complete hint
     * Default: null
     */
    open fun actionBarExpandedAutoCompleteHint() = 0

    /**
     * Open function for override bottom navigation menu
     * Default: 0
     */
    open fun bottomNavBarMenu() = 0

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

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(LocaleUtil.onAttach(newBase))
    }

    @Suppress("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = orientation()
        AppCompatDelegate.setDefaultNightMode(nightMode())

        appBarConfiguration = AppBarConfiguration(
            topLevelDestinationIds = topLevelDestinations(),
            fallbackOnNavigateUpListener = ::onSupportNavigateUp
        )
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}