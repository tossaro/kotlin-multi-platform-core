package multi.platform.core.shared

import io.ktor.client.engine.okhttp.*
import multi.platform.core.shared.external.utility.LocaleUtil
import multi.platform.core.shared.external.utility.SharedPrefsUtil
import org.koin.dsl.module
import java.text.SimpleDateFormat
import java.util.*

actual typealias Context = android.app.Application

class AndroidPlatform : Platform {
    override val name: String = "Android ${android.os.Build.VERSION.SDK_INT}"
}

actual fun getPlatform(): Platform = AndroidPlatform()
actual fun getLanguage(context: Context?): String {
    return context?.let {
        LocaleUtil.retrieveAppLanguage(it)
    } ?: run { "ID" }
}

@Suppress("Unused")
actual typealias Parcelize = kotlinx.android.parcel.Parcelize

@Suppress("Unused")
actual interface Parcelable : android.os.Parcelable

actual fun getStringPref(context: Context?, name: String, key: String, default: String?): String? {
    return context?.let {
        val preferences = SharedPrefsUtil.build(it, name)
        preferences.getString(key, default)
    } ?: run { null }
}

actual fun putStringPref(context: Context?, name: String, key: String, value: String?) {
    context?.let {
        val preferences = SharedPrefsUtil.build(it, name)
        preferences.edit().putString(key, value).apply()
    }
}

actual fun removePref(context: Context?, name: String, key: String) {
    context?.let {
        val preferences = SharedPrefsUtil.build(it, name)
        preferences.edit().remove(key).apply()
    }
}

@Suppress("SimpleDateFormat", "UnUsed")
actual fun formatDate(dateString: String, fromFormat: String, toFormat: String): String {
    val date =
        if (dateString.isNotEmpty()) SimpleDateFormat(fromFormat).parse(dateString) else Date()
    val dateFormatter = SimpleDateFormat(toFormat, Locale.getDefault())
    return dateFormatter.format(date ?: Date())
}

actual fun platformModule() = module {
    single { OkHttp.create() }
}

@Suppress("UnUsed")
actual class DecimalFormat {
    actual fun format(double: Double, maximumFractionDigits: Int): String {
        val df = java.text.DecimalFormat()
        df.isGroupingUsed = false
        df.maximumFractionDigits = maximumFractionDigits
        df.isDecimalSeparatorAlwaysShown = false
        return df.format(double)
    }
}