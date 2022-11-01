package multi.platform.core.shared

import platform.UIKit.UIDevice
import platform.Foundation.NSUserDefaults

actual typealias Context = platform.darwin.NSObject

class IOSPlatform : Platform {
    override val name: String =
        UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
}

actual fun getPlatform(): Platform = IOSPlatform()
actual fun getLanguage(_: Context) = "ID"
actual interface Parcelable

@Suppress("kotlin:S1172")
actual fun getStringPref(context: Context, name: String, key: String, default: String?): String? {
    return NSUserDefaults.standardUserDefaults.stringForKey(key)
}
@Suppress("kotlin:S1172")
actual fun putStringPref(context: Context, name: String, key: String, value: String?) {
    NSUserDefaults.standardUserDefaults.setObject(value, key)
}
@Suppress("kotlin:S1172")
actual fun removePref(context: Context, name: String, key: String) {
    NSUserDefaults.standardUserDefaults.removeObjectForKey(key)
}