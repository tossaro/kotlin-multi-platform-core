package tossaro.android.core.app

import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.updateLayoutParams
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import tossaro.android.core.R
import tossaro.android.core.databinding.BaseSheetFragmentBinding
import tossaro.android.core.external.extension.hideKeyboard
import tossaro.android.core.external.utility.LocaleUtil


open class BaseSheetFragment<B : ViewDataBinding>(
    @LayoutRes val layoutResId: Int
) : BottomSheetDialogFragment(), KoinComponent {

    lateinit var binding: B
    protected var fragmentView: View? = null
    private var _baseBinding: BaseSheetFragmentBinding? = null
    protected val baseBinding get() = _baseBinding!!
    protected val sharedPreferences: SharedPreferences by inject()
    private var bottomSheetCallback: BottomSheetCallback? = null

    /**
     * Open function for override root layout resource
     * Default: R.layout.base_fragment
     */
    protected open fun getRootLayoutRes(): Int = R.layout.base_sheet_fragment

    /**
     * Open function for override visibility loading binding
     */
    protected open fun showFullLoading(isShow: Boolean = true) {
        hideKeyboard()
        baseBinding.loadingCircle.visibility = if (isShow) View.VISIBLE else View.GONE
    }

    /**
     * Open function for override visibility close button
     */
    protected open fun showCloseButton() = true

    /**
     * Open function for override title
     */
    protected open fun title() = ""

    /**
     * Open function for override initial state
     */
    protected open fun initialState() = BottomSheetBehavior.STATE_COLLAPSED

    /**
     * Open function for override max height
     */
    protected open fun forceFullHeight() = false

    /**
     * Open function for override min height
     */
    protected open fun minHeight() = 0.0

    /**
     * Open function for override max height
     */
    protected open fun maxHeight() = 0.9

    protected open fun setFullHeight(frameLayout: FrameLayout) {
        val lpBs = frameLayout.layoutParams
        lpBs.height = WindowManager.LayoutParams.MATCH_PARENT
        frameLayout.layoutParams = lpBs
        val lpRoot = baseBinding.clRoot.layoutParams
        lpRoot.height = WindowManager.LayoutParams.MATCH_PARENT
        baseBinding.clRoot.layoutParams = lpRoot
        baseBinding.fragmentContent.updateLayoutParams<ConstraintLayout.LayoutParams> {
            bottomToBottom = R.id.cl_root
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(isCancelable)
        dialog.setCanceledOnTouchOutside(isCancelable)
        dialog.setOnShowListener { d ->
            val bd = d as BottomSheetDialog
            val bottomSheet =
                bd.findViewById<FrameLayout?>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet.let {
                val behaviour = BottomSheetBehavior.from(it as FrameLayout)
                if (minHeight() > 0) {
                    behaviour.peekHeight =
                        (resources.displayMetrics.heightPixels * minHeight()).toInt()
                    setFullHeight(it)
                }
                behaviour.maxHeight = (resources.displayMetrics.heightPixels * maxHeight()).toInt()
                behaviour.state = initialState()
                if (forceFullHeight()) {
                    behaviour.peekHeight = behaviour.maxHeight
                    behaviour.state = BottomSheetBehavior.STATE_EXPANDED
                    setFullHeight(it)
                }
            }
        }
        dialog.setOnDismissListener { d ->
            val bd = d as BottomSheetDialog
            val bottomSheet =
                bd.findViewById<FrameLayout?>(com.google.android.material.R.id.design_bottom_sheet)
            val behavior = BottomSheetBehavior.from(bottomSheet as FrameLayout)
            bottomSheetCallback?.let { behavior.removeBottomSheetCallback(it) }
        }
        return dialog
    }

    @Suppress("RestrictedApi")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _baseBinding = BaseSheetFragmentBinding.inflate(inflater, container, false)
        (fragmentView?.parent as? ViewGroup)?.removeAllViews()
        fragmentView = inflater.inflate(getRootLayoutRes(), container, false).apply {
            binding = DataBindingUtil.inflate(inflater, layoutResId, container, false)
            baseBinding.fragmentContent.removeAllViews()
            baseBinding.fragmentContent.addView(binding.root)
        }
        baseBinding.tvTitle.text = title()
        baseBinding.ivClose.visibility = if (showCloseButton()) View.VISIBLE else View.GONE
        baseBinding.ivClose.setOnClickListener { dismiss() }
        return binding.root.rootView
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _baseBinding = null
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        LocaleUtil.onAttach(context)
    }

}