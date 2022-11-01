package multi.platform.core.shared

import multi.platform.core.shared.external.utility.LocaleUtil
import multi.platform.core.shared.external.utility.SharedPrefsUtil

actual typealias Context = android.content.Context

class AndroidPlatform : Platform {
    override val name: String = "Android ${android.os.Build.VERSION.SDK_INT}"
}

actual fun getPlatform(): Platform = AndroidPlatform()
actual fun getLanguage(context: Context) = LocaleUtil.retrieveAppLanguage(context, LocaleUtil.ID)

@Suppress("Unused")
actual typealias Parcelize = kotlinx.android.parcel.Parcelize

@Suppress("Unused")
actual interface Parcelable : android.os.Parcelable

actual fun getStringPref(context: Context, name: String, key: String, default: String?): String? {
    val preferences = SharedPrefsUtil.build(context, name)
    return preferences.getString(key, default)
}

actual fun putStringPref(context: Context, name: String, key: String, value: String?) {
    val preferences = SharedPrefsUtil.build(context, name)
    preferences.edit().putString(key, value).apply()
}

actual fun removePref(context: Context, name: String, key: String) {
    val preferences = SharedPrefsUtil.build(context, name)
    preferences.edit().remove(key).apply()
}