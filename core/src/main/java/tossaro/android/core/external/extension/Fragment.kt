package tossaro.android.core.external.extension

import android.app.Activity
import android.net.Uri
import android.provider.MediaStore
import android.util.TypedValue
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import tossaro.android.core.BuildConfig
import tossaro.android.core.app.BaseActivity


@Suppress("UNUSED")
fun Fragment.hideKeyboard() {
    val currentFocus = if (this is DialogFragment) dialog?.currentFocus else activity?.currentFocus
    val imm = requireContext().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
}

@Suppress("UNUSED")
fun Fragment.showKeyboard(focusView: View) {
    if (focusView.isEnabled) {
        focusView.requestFocus()
        val imm =
            requireContext().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(focusView.findFocus(), InputMethodManager.SHOW_IMPLICIT)
    }
}

@Suppress("UNUSED")
fun Fragment.isSafe(): Boolean {
    return !(this.isRemoving || this.activity == null || this.isDetached || !this.isAdded || this.view == null)
}

/**
 * Extension for direction to fragment path
 */
fun Fragment.goTo(path: String, navOptions: NavOptions? = null) {
    hideKeyboard()
    findNavController().navigate(
        Uri.parse("android-app://${BuildConfig.DEEP_LINK_HOST}$path"),
        navOptions
    )
}

/**
 * Extension for show toast message
 */
fun Fragment.showToast(messageString: String) {
    hideKeyboard()
    Toast.makeText(context, messageString, Toast.LENGTH_LONG).show()
}

/**
 * Extension for show success snackbar
 */
fun Fragment.showSuccessSnackbar(messageString: String) = showSnackbar(messageString, false)

/**
 * Extension for show error snackbar
 */
fun Fragment.showErrorSnackbar(messageString: String) = showSnackbar(messageString, true)

/**
 * Extension for show snackbar
 */
@Suppress("kotlin:S1871")
fun Fragment.showSnackbar(messageString: String, isError: Boolean) {
    hideKeyboard()
    val anchor: View? = if (this is DialogFragment) dialog?.window?.decorView else null
    (requireActivity() as BaseActivity).showSnackbar(messageString, isError, anchor)
}

/**
 * Extension for show converting dp to px
 */
fun Fragment.dpToPx(dp: Float): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dp,
        context?.resources?.displayMetrics
    ).toInt()
}

/**
 * Extension for show converting sp to px
 */
@Suppress("UNUSED")
fun Fragment.spToPx(dp: Float): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP,
        dp,
        context?.resources?.displayMetrics
    ).toInt()
}

/**
 * Extension for show converting dp to sp
 */
@Suppress("UNUSED")
fun Fragment.dpToSp(dp: Float): Int {
    val d: Float = context?.resources?.displayMetrics?.scaledDensity ?: 1f
    return (dpToPx(dp) / d).toInt()
}

/**
 * Extension for get path file from URI
 */
@Suppress("UNUSED")
fun Fragment.getPathFromURI(contentUri: Uri?): String? {
    var res: String? = null
    contentUri?.let { u ->
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = requireContext().contentResolver.query(u, proj, null, null, null)
        cursor?.let {
            if (it.moveToFirst()) {
                val col = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                res = it.getString(col)
            }
            it.close()
        }
    }
    return res
}