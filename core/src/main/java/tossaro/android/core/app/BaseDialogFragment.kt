package tossaro.android.core.app

import android.app.Dialog
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import tossaro.android.core.R
import tossaro.android.core.databinding.BaseDialogFragmentBinding

open class BaseDialogFragment<B : ViewDataBinding>(
    @LayoutRes val layoutResId: Int
) : DialogFragment(), KoinComponent {

    lateinit var binding: B
    protected var fragmentView: View? = null
    private var _baseBinding: BaseDialogFragmentBinding? = null
    protected val baseBinding get() = _baseBinding!!
    protected val sharedPreferences: SharedPreferences by inject()

    /**
     * Open function for override root layout resource
     * Default: R.layout.base_fragment
     */
    protected open fun getRootLayoutRes(): Int = R.layout.base_dialog_fragment

    /**
     * Open function for override visibility loading binding
     */
    protected open fun showFullLoading(isShow: Boolean = true) {
        baseBinding.loadingCircle.visibility = if (isShow) View.VISIBLE else View.GONE
    }

    /**
     * Open function for override visibility close button
     */
    protected open fun showCloseButton() = true

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(isCancelable)
        dialog.setCanceledOnTouchOutside(isCancelable)
        return dialog
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _baseBinding = BaseDialogFragmentBinding.inflate(inflater, container, false)
        (fragmentView?.parent as? ViewGroup)?.removeAllViews()
        fragmentView = inflater.inflate(getRootLayoutRes(), container, false).apply {
            binding = DataBindingUtil.inflate(inflater, layoutResId, container, false)
            baseBinding.fragmentContent.removeAllViews()
            baseBinding.fragmentContent.addView(binding.root)
        }
        baseBinding.ivClose.visibility = if (showCloseButton()) View.VISIBLE else View.GONE
        baseBinding.ivClose.setOnClickListener { dismiss() }
        binding.root.setOnApplyWindowInsetsListener { _, windowInsets ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                val imeHeight = windowInsets.getInsets(WindowInsets.Type.ime()).bottom
                binding.root.setPadding(0, 0, 0, imeHeight)
            }
            windowInsets
        }
        return binding.root.rootView
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _baseBinding = null
    }

}