package android.core.modules

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import android.core.R
import android.core.databinding.BaseFragmentBinding
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.view.*
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope

abstract class BaseFragment<B: ViewDataBinding>(
    @LayoutRes val layoutResId: Int
) : Fragment() {

    var fragmentView: View? = null
    private var _baseBinding: BaseFragmentBinding? = null
    val baseBinding get() = _baseBinding!!
    lateinit var binding: B

    protected open fun getRootLayoutRes(): Int = R.layout.base_fragment
    protected open fun isActionBarShown() = true
    protected open fun getActionToolbar(): Toolbar? = baseBinding.toolbar
    protected open fun actionBarTitle(): String? = null
    protected open fun doesFitSystemWindows() = false
    protected open fun isStatusBarTranslucent() = false
    protected open fun isNightMode() = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
    protected open fun isNavigationBarTranslucent() = false
    protected open fun isFullScreen() = false
    protected open fun isResize() = true
    protected open fun bind() {}

    protected open fun loadingIndicator(visibility: Boolean = true) {
        baseBinding.progressView.visibility = if (visibility) View.VISIBLE else View.GONE
    }

    protected open fun showAlert(messageString: String) {
        Toast.makeText(context, messageString, Toast.LENGTH_LONG).show()
    }

    @Suppress("DEPRECATION")
    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity?.lifecycleScope?.launchWhenCreated {
            activity?.window?.run {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    if (isFullScreen()) insetsController?.hide(WindowInsets.Type.statusBars())
                    else insetsController?.show(WindowInsets.Type.statusBars())

                    statusBarColor = ContextCompat.getColor(requireContext(), if (isStatusBarTranslucent()) android.R.color.transparent else R.color.statusBarColor)
                    WindowCompat.getInsetsController(this, decorView).isAppearanceLightStatusBars = isNightMode() == Configuration.UI_MODE_NIGHT_NO

                    navigationBarColor = ContextCompat.getColor(requireContext(), if (isNavigationBarTranslucent()) android.R.color.transparent else R.color.navBarColor)

                    setDecorFitsSystemWindows(!isResize())
                } else {
                    if (isFullScreen()) setFlags(
                        WindowManager.LayoutParams.FLAG_FULLSCREEN,
                        WindowManager.LayoutParams.FLAG_FULLSCREEN
                    )
                    else clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)

                    if (isStatusBarTranslucent()) addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                    else clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)

                    if (isNavigationBarTranslucent()) addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
                    else clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)

                    setSoftInputMode(if (isResize()) WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE else WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN )
                }
            }
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    override fun onDestroy() {
        super.onDestroy()
        _baseBinding = null
    }

    @SuppressLint("RestrictedApi")
    protected open fun configureActionBar(actionBar: ActionBar) {
        actionBar.setDisplayShowTitleEnabled(false)
        actionBar.setDisplayHomeAsUpEnabled(false)
    }

    @CallSuper
    protected open fun init() {
        takeIf { isActionBarShown() }?.run { getActionToolbar() }?.run {
            (activity as? AppCompatActivity)?.setSupportActionBar(this)
            (activity as? AppCompatActivity)?.let { NavigationUI.setupActionBarWithNavController(it, findNavController()) }
            (activity as? AppCompatActivity)?.supportActionBar?.let(this@BaseFragment::configureActionBar)
        }

        baseBinding.fragmentContent.fitsSystemWindows = doesFitSystemWindows()
        baseBinding.toolbarContainer.isVisible = isActionBarShown()
        takeIf { !actionBarTitle().isNullOrEmpty() }?.run {
            baseBinding.toolbar.apply { baseBinding.tvTitle.text = actionBarTitle() }
        }

        bind()
    }

    @Suppress("DEPRECATION")
    fun isInternetAvailable(): Boolean {
        var result = false
        val cm = requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
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
        } else {
            cm?.run {
                cm.activeNetworkInfo?.run {
                    if (type == ConnectivityManager.TYPE_WIFI) {
                        result = true
                    } else if (type == ConnectivityManager.TYPE_MOBILE) {
                        result = true
                    }
                }
            }
        }
        return result
    }
}