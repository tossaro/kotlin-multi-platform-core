package tossaro.android.core.external.ext

import android.app.Activity
import android.net.Uri
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import tossaro.android.core.BuildConfig

fun Fragment.hideKeyboard() {
    val currentFocus = if (this is DialogFragment) dialog?.currentFocus else activity?.currentFocus
    val imm = requireContext().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
}

fun Fragment.showKeyboard(focusView: View) {
    if (focusView.isEnabled) {
        focusView.requestFocus()
        val imm =
            requireContext().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(focusView.findFocus(), InputMethodManager.SHOW_IMPLICIT)
    }
}

fun Fragment.isSafe(): Boolean {
    return !(this.isRemoving || this.activity == null || this.isDetached || !this.isAdded || this.view == null)
}

/**
 * Open function for override direction to fragment path
 * Example: /home
 */
fun Fragment.goTo(path: String, navOptions: NavOptions? = null) {
    findNavController().navigate(
        Uri.parse("android-app://${BuildConfig.DEEP_LINK_HOST}$path"),
        navOptions
    )
}

/**
 * Open function for override show toast message
 */
fun Fragment.showToast(messageString: String) {
    Toast.makeText(context, messageString, Toast.LENGTH_LONG).show()
}