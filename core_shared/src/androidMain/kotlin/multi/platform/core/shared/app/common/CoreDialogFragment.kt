@file:Suppress("UnUsed")
package multi.platform.core.shared.app.common

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.KeyEvent.KEYCODE_BACK
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import multi.platform.core.shared.external.utility.LocaleUtil
import org.koin.core.component.KoinComponent

open class CoreDialogFragment : DialogFragment(), KoinComponent {

    /**
     * Open function for override root layout resource
     * Default: 0
     */
    protected open fun getRootLayoutRes(): Int = 0

    /**
     * Open function for override visibility close button
     */
    protected open fun showCloseButton() = true

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(isCancelable)
        dialog.setCanceledOnTouchOutside(isCancelable)
        dialog.setOnKeyListener { _, i, _ ->
            i == KEYCODE_BACK && !isCancelable
        }
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

    override fun onAttach(context: Context) {
        super.onAttach(context)
        LocaleUtil.onAttach(context)
    }

}