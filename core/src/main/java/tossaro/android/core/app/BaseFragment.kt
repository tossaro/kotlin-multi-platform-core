package tossaro.android.core.app

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.activity.OnBackPressedCallback
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import tossaro.android.core.R
import tossaro.android.core.databinding.BaseFragmentBinding
import tossaro.android.core.external.utility.LocaleUtil


open class BaseFragment<B : ViewDataBinding>(
    @LayoutRes val layoutResId: Int
) : Fragment(), KoinComponent {

    lateinit var binding: B
    protected var fragmentView: View? = null
    private var _baseBinding: BaseFragmentBinding? = null
    protected val baseBinding get() = _baseBinding!!
    protected val sharedPreferences: SharedPreferences by inject()

    /**
     * Open function for override root layout resource
     * Default: R.layout.base_fragment
     */
    protected open fun getRootLayoutRes() = R.layout.base_fragment

    /**
     * Open function for override visibility action bar
     * Default: true
     */
    protected open fun isActionBarShown() = true

    /**
     * Open function for override visibility bottom navigation bar
     * Default: true
     */
    protected open fun isBottomNavBarShown() = true

    /**
     * Open function for override toolbar binding
     * Default: baseBinding.toolbar
     */
    protected open fun actionBar() = (requireActivity() as BaseActivity<*>).actionBar()

    /**
     * Open function for override bottom navigation bar binding
     * Default: baseBinding.bottomNav
     */
    protected open fun bottomNavBar() = (requireActivity() as BaseActivity<*>).bottomNavBar()

    /**
     * Open function for override toolbar title
     */
    protected open fun actionBarTitle() = ""

    /**
     * Open function for override fit system size
     * Default: false
     */
    protected open fun doesFitSystemWindows() = false

    /**
     * Open function for override status bar color
     * Default: false
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
        baseBinding.loadingLinear.visibility = if (visibility) View.VISIBLE else View.GONE
    }

    @Suppress("DEPRECATION", "kotlin:S1874")
    protected open fun setFullScreen(window: Window) {
        if (isFullScreen()) {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            )
        } else {
            window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
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

    protected open fun onBackPressed() = (context as AppCompatActivity).onSupportNavigateUp()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        LocaleUtil.onAttach(context)
        activity?.lifecycleScope?.launchWhenCreated {
            activity?.window?.run {
                setFullScreen(this)
                setNavigationBar(this)
                setResize(this)
                statusBarColor = ContextCompat.getColor(requireContext(), statusBarColor())
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity()
            .onBackPressedDispatcher
            .addCallback(this, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    onBackPressed()
                }
            })
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
        binding.root.setOnApplyWindowInsetsListener { _, windowInsets ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                val imeHeight = windowInsets.getInsets(WindowInsets.Type.ime()).bottom
                binding.root.setPadding(0, 0, 0, imeHeight)
            }
            windowInsets
        }
        return binding.root.rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        baseBinding.fragmentContent.fitsSystemWindows = doesFitSystemWindows()
        actionBar()?.title = actionBarTitle()
        actionBar()?.isVisible = isActionBarShown()
        bottomNavBar()?.isVisible = isBottomNavBarShown()
    }

    override fun onDestroy() {
        super.onDestroy()
        _baseBinding = null
    }
}