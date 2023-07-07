@file:Suppress("UnUsed")
package multi.platform.core.shared.app.common

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Window
import android.widget.FrameLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import multi.platform.core.shared.external.utility.LocaleUtil
import org.koin.core.component.KoinComponent


open class CoreSheetFragment : BottomSheetDialogFragment(), KoinComponent {

    private var bottomSheetCallback: BottomSheetCallback? = null

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

    @Suppress("kotlin:S1186")
    protected open fun setFullHeight(frameLayout: FrameLayout) {}

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

    override fun onAttach(context: Context) {
        super.onAttach(context)
        LocaleUtil.onAttach(context)
    }
}