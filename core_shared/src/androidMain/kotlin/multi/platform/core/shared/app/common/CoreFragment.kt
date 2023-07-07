@file:Suppress("UnUsed")

package multi.platform.core.shared.app.common

import android.os.Build
import android.view.*
import androidx.core.content.ContextCompat
import androidx.core.view.*
import androidx.fragment.app.Fragment
import org.koin.core.component.KoinComponent


open class CoreFragment : Fragment(), KoinComponent {

    private var isTopInsets = true
    private var isBottomInsets = true

    /**
     * Open function for override visibility action bar
     * Default: true
     */
    protected open fun showActionBar() = true

    /**
     * Open function for override action bar expanded binding
     * Default: false
     */
    open fun expandActionBar() = false

    /**
     * Open function for override visibility bottom navigation bar
     * Default: true
     */
    protected open fun showBottomNavBar() = false

    /**
     * Open function for override add top insets
     * Default: true
     */
    protected open fun addTopInsets() = isTopInsets

    /**
     * Open function for override add bottom insets
     * Default: true
     */
    protected open fun addBottomInsets() = isBottomInsets

    /**
     * Open function for override support action bar binding
     * Default: baseActivity.supportActionBar
     */
    protected open fun supportActionBar() = (requireActivity() as CoreActivity).supportActionBar

    /**
     * Open function for override enable action bar offset listener
     * Default: false
     */
    protected open fun isActionBarOffsetListenerEnable() = false

    /**
     * Open function for override action bar on collapsed
     */
    @Suppress("kotlin:S1186")
    protected open fun onCollapsedActionBar() {
    }

    /**
     * Open function for override action bar on expanded
     */
    @Suppress("kotlin:S1186")
    protected open fun onExpandedActionBar() {
    }

    /**
     * Open function for override show action bar search
     * Default: false
     */
    protected open fun showActionBarSearch() = false

    /**
     * Open function for override action bar search filter
     * Default: false
     */
    @Suppress("kotlin:S1186")
    protected open fun onTapFilterActionBarSearch() {
    }

    /**
     * Open function for override action bar height
     * Default: 240
     */
    protected open fun actionBarHeight() = 240

    /**
     * Open function for override action bar top margin
     * Default: 0
     */
    protected open fun actionBarTopMargin() = 0

    /**
     * Open function for override show action bar title on expanded
     * Default: true
     */
    protected open fun showActionBarTitleOnExpanded() = true

    /**
     * Open function for override action bar info visibility
     * Default: false
     */
    protected open fun showActionBarInfo() = false

    /**
     * Open function for override action bar search filter visibility
     * Default: false
     */
    protected open fun showActionBarSearchFilter() = false

    /**
     * Open function for override toolbar title
     */
    protected open fun actionBarTitle() = " "

    /**
     * Open function for override fit system size
     * Default: false
     */
    protected open fun doesFitSystemWindows() = false

    /**
     * Open function for override navigation bar translucent
     * Default: false
     */
    protected open fun isNavigationBarTranslucent() = false

    /**
     * Open function for override full screen
     * Default: false
     */
    protected open fun isFullScreen() = false

    /**
     * Open function for override resize capability
     * Default: true
     */
    protected open fun shouldResize() = true

    /**
     * Open function for override status bar color
     * Default: 0
     */
    protected open fun statusBarColor(): Int = 0

    /**
     * Open function for override status bar color
     * Default: 0
     */
    protected open fun navBarColor(): Int = 0

    protected open fun setInsetsListener(window: Window) {
        ViewCompat.setOnApplyWindowInsetsListener(window.decorView) { v, insets ->
            val statusBarInsets =
                if (addTopInsets()) insets.getInsets(WindowInsetsCompat.Type.statusBars()).top else 0
            val systemBarInsets =
                if (addBottomInsets()) insets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom else 0
            val imeInsets = insets.getInsets(WindowInsetsCompat.Type.ime()).bottom
            v.setPadding(0, statusBarInsets, 0, if (imeInsets != 0) imeInsets else systemBarInsets)
            WindowInsetsCompat.CONSUMED
        }
    }

    @Suppress("DEPRECATION", "kotlin:S1874")
    protected open fun setNavigationBar(window: Window) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.navigationBarColor = ContextCompat.getColor(
                requireContext(),
                if (isNavigationBarTranslucent()) android.R.color.transparent else navBarColor()
            )
        } else {
            if (isNavigationBarTranslucent()) window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
            else window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
        }
    }

    @Suppress("DEPRECATION", "kotlin:S1874")
    protected open fun setResize(window: Window) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(shouldResize())
        } else {
            window.setSoftInputMode(if (shouldResize().not()) WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE else WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        }
    }

    @Suppress("ClickableViewAccessibility")
    override fun onResume() {
        super.onResume()
        activity?.window?.run {
            if (isFullScreen()) {
                isTopInsets = false
                isBottomInsets = false
            }

            setInsetsListener(this)
            setNavigationBar(this)
            setResize(this)
            statusBarColor = ContextCompat.getColor(requireContext(), statusBarColor())
            if (doesFitSystemWindows()) WindowCompat.setDecorFitsSystemWindows(this, true)
        }
    }
}