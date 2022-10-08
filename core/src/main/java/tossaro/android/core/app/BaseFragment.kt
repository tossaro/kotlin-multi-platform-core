@file:Suppress("EmptyMethod")

package tossaro.android.core.app

import android.content.Context
import android.content.SharedPreferences
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import androidx.annotation.LayoutRes
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.google.android.material.appbar.AppBarLayout
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import tossaro.android.core.R
import tossaro.android.core.databinding.BaseFragmentBinding
import tossaro.android.core.external.constant.AppConstant
import tossaro.android.core.external.extension.dpToPx
import tossaro.android.core.external.extension.hideKeyboard
import tossaro.android.core.external.utility.LocaleUtil


open class BaseFragment<B : ViewDataBinding>(
    @LayoutRes val layoutResId: Int
) : Fragment(), KoinComponent {

    lateinit var binding: B
    private var fragmentView: View? = null
    private var _baseBinding: BaseFragmentBinding? = null
    private val baseBinding get() = _baseBinding!!
    protected val sharedPreferences: SharedPreferences by inject()
    private var isTopInsets = true
    private var isBottomInsets = true
    private var onActionOffsetChanged: AppBarLayout.OnOffsetChangedListener? = null
    private var isActionBarOffsetListenerEnable = false

    /**
     * Open function for override root layout resource
     * Default: R.layout.base_fragment
     */
    protected open fun getRootLayoutRes() = R.layout.base_fragment

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
    protected open fun supportActionBar() = (requireActivity() as BaseActivity).supportActionBar

    /**
     * Open function for override action bar binding
     * Default: baseActivity.actionBar
     */
    protected open fun actionBar() = (requireActivity() as BaseActivity).actionBar()

    /**
     * Open function for override enable action bar offset listener
     * Default: false
     */
    protected open fun isActionBarOffsetListenerEnable() = isActionBarOffsetListenerEnable

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
     * Open function for override action bar search binding
     * Default: baseActivity.actionBarSearch
     */
    protected open fun actionBarSearch() = (requireActivity() as BaseActivity).actionBarSearch()

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
     * Open function for override action bar layout binding
     * Default: baseActivity.actionBarLayout
     */
    protected open fun actionBarLayout() = (requireActivity() as BaseActivity).actionBarLayout()

    /**
     * Open function for override show action bar title on expanded
     * Default: true
     */
    protected open fun showActionBarTitleOnExpanded() = true

    /**
     * Open function for override action bar content scrim
     * Default: R.color.white
     */
    protected open fun actionBarContentScrim() = R.color.white

    /**
     * Open function for override action bar collapsing layout binding
     * Default: baseActivity.actionBarCollapsingLayout
     */
    protected open fun actionBarCollapsingLayout() =
        (requireActivity() as BaseActivity).actionBarCollapsingLayout()

    /**
     * Open function for override action bar viewpager binding
     * Default: baseActivity.actionBarViewPager
     */
    protected open fun actionBarViewPager() =
        (requireActivity() as BaseActivity).actionBarViewPager()

    /**
     * Open function for override action bar tab layout for dot indicator binding
     * Default: baseActivity.actionBarDotIndicator
     */
    protected open fun actionBarDotIndicator() =
        (requireActivity() as BaseActivity).actionBarDotIndicator()

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
     * Open function for override action bar info layout binding
     * Default: baseActivity.actionBarInfoLayout
     */
    protected open fun actionBarInfoLayout() =
        (requireActivity() as BaseActivity).actionBarInfoLayout()

    /**
     * Open function for override action bar info icon binding
     * Default: baseActivity.actionBarInfoIcon
     */
    protected open fun actionBarInfoIcon() =
        (requireActivity() as BaseActivity).actionBarInfoIcon()

    /**
     * Open function for override action bar info text binding
     * Default: baseActivity.actionBarInfoText
     */
    protected open fun actionBarInfoText() =
        (requireActivity() as BaseActivity).actionBarInfoText()

    /**
     * Open function for override bottom navigation bar binding
     * Default: baseActivity.bottomNav
     */
    protected open fun bottomNavBar() = (requireActivity() as BaseActivity).bottomNavBar()

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
     * Open function for override status bar color
     * Default: R.color.statusBar
     */
    protected open fun statusBarColor() = R.color.statusBar

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
     * Open function for override visibility loading binding
     */
    protected open fun showFullLoading(visibility: Boolean = true) {
        hideKeyboard()
        baseBinding.loadingLinear.isVisible = visibility
    }

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
                if (isNavigationBarTranslucent()) android.R.color.transparent else R.color.navBar
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

    protected open fun setActionBarOffsetListener() {
        if (!showActionBarTitleOnExpanded()) isActionBarOffsetListenerEnable = true
        if (isActionBarOffsetListenerEnable()) {
            var isShow = true
            var scrollRange = -1
            onActionOffsetChanged =
                AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
                    if (scrollRange == -1) scrollRange = appBarLayout?.totalScrollRange!!
                    if (scrollRange + verticalOffset == 0) {
                        actionBarCollapsingLayout().title = actionBarTitle()
                        onCollapsedActionBar()
                        isShow = true
                    } else if (isShow) {
                        if (!showActionBarTitleOnExpanded()) actionBarCollapsingLayout().title =
                            " "
                        onExpandedActionBar()
                        isShow = false
                    }
                }
            actionBarLayout().addOnOffsetChangedListener(onActionOffsetChanged)
        }
    }

    protected open fun setActionBarSearchIcon() {
        actionBarSearch().apply {
            val icSearch = ContextCompat.getDrawable(requireContext(), R.drawable.ic_search)
            var icFilter: Drawable? = null
            if (showActionBarSearchFilter()) {
                icFilter = ContextCompat.getDrawable(requireContext(), R.drawable.ic_filter)
                val isFiltered = sharedPreferences.getBoolean(AppConstant.FILTERED, false)
                val tintColor = if (isFiltered) R.color.primary else R.color.blue100
                icFilter?.let {
                    DrawableCompat.setTint(it, ContextCompat.getColor(requireContext(), tintColor))
                }
            }
            setCompoundDrawablesWithIntrinsicBounds(icSearch, null, icFilter, null)
        }
    }

    protected open fun setActionBarVisibility() {
        if (showActionBar()) {
            actionBarLayout().updateLayoutParams<CoordinatorLayout.LayoutParams> {
                height = dpToPx(actionBarHeight().toFloat())
            }
        } else {
            actionBarLayout().updateLayoutParams<CoordinatorLayout.LayoutParams> { height = 0 }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        LocaleUtil.onAttach(context)
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

        actionBarSearch().apply {
            isFocusable = true
            isFocusableInTouchMode = true
            isVisible = showActionBarSearch()
            setOnTouchListener { _, motionEvent ->
                if (motionEvent.action == MotionEvent.ACTION_UP
                    && actionBarSearch().compoundDrawables[2] != null
                    && motionEvent.rawX >= (actionBarSearch().right - actionBarSearch().compoundDrawables[2].bounds.width())
                ) {
                    onTapFilterActionBarSearch()
                    return@setOnTouchListener true
                }
                false
            }
        }
        actionBarInfoLayout().isVisible = showActionBarInfo()
        bottomNavBar().isVisible = showBottomNavBar()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        actionBarLayout().setExpanded(expandActionBar(), expandActionBar())
        val appBarParams = actionBarLayout().layoutParams as CoordinatorLayout.LayoutParams
        if (appBarParams.behavior == null) appBarParams.behavior = AppBarLayout.Behavior()
        val appBarBehaviour = appBarParams.behavior as AppBarLayout.Behavior
        appBarBehaviour.setDragCallback(object : AppBarLayout.Behavior.DragCallback() {
            override fun canDrag(appBarLayout: AppBarLayout): Boolean {
                return expandActionBar()
            }
        })
        setActionBarVisibility()
        setActionBarSearchIcon()
        actionBarCollapsingLayout().title = actionBarTitle()
        actionBarCollapsingLayout().setContentScrimResource(actionBarContentScrim())
        setActionBarOffsetListener()
        actionBar().apply {
            setPadding(dpToPx(16f), 0, dpToPx(16f), 0)
            updateLayoutParams<ViewGroup.MarginLayoutParams> {
                topMargin = dpToPx(actionBarTopMargin().toFloat())
            }
        }
        actionBarSearch().apply {
            val dp11 = dpToPx(11f)
            setPadding(dp11, dp11, dp11, dp11)
            textSize = 16f
            height = dpToPx(40f)
            gravity = Gravity.START or Gravity.CENTER_VERTICAL
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _baseBinding = BaseFragmentBinding.inflate(inflater, container, false)
        (fragmentView?.parent as? ViewGroup)?.removeAllViews()
        fragmentView = inflater.inflate(getRootLayoutRes(), container, false).apply {
            binding = DataBindingUtil.inflate(inflater, layoutResId, container, false)
            baseBinding.fragmentContent.removeAllViews()
            baseBinding.fragmentContent.addView(binding.root)
        }
        return binding.root.rootView
    }

    override fun onDestroy() {
        super.onDestroy()
        _baseBinding = null
    }

    @Suppress("ClickableViewAccessibility")
    override fun onStop() {
        super.onStop()
        actionBarDotIndicator().removeAllTabs()
        actionBarViewPager().adapter = null
        actionBarSearch().setOnTouchListener(null)
        if (!showActionBarTitleOnExpanded()) {
            actionBarLayout().removeOnOffsetChangedListener(onActionOffsetChanged)
        }
    }
}