package multi.platform.core.shared

import io.ktor.client.engine.darwin.*
import org.koin.dsl.module
import platform.Foundation.*
import platform.UIKit.UIDevice
import platform.Foundation.NSNumber
import platform.Foundation.NSNumberFormatter

actual typealias Context = platform.darwin.NSObject

class IOSPlatform : Platform {
    override val name: String =
        UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
}

actual fun getPlatform(): Platform = IOSPlatform()

@Suppress("kotlin:S1172")
actual fun getLanguage(context: Context?) = "ID"

@Suppress("Unused")
actual interface Parcelable

@Suppress("kotlin:S1172")
actual fun getStringPref(context: Context?, name: String, key: String, default: String?): String? {
    return NSUserDefaults.standardUserDefaults.stringForKey(key)
}

@Suppress("kotlin:S1172")
actual fun putStringPref(context: Context?, name: String, key: String, value: String?) {
    NSUserDefaults.standardUserDefaults.setObject(value, key)
}

@Suppress("kotlin:S1172")
actual fun removePref(context: Context?, name: String, key: String) {
    NSUserDefaults.standardUserDefaults.removeObjectForKey(key)
}

@Suppress("SimpleDateFormat", "UnUsed")
actual fun formatDate(dateString: String, fromFormat: String, toFormat: String): String {
    val dateFormatter = NSDateFormatter().apply {
        dateFormat = fromFormat
    }
    val formatter = NSDateFormatter().apply {
        dateFormat = toFormat
        locale = NSLocale(localeIdentifier = "id_ID")
    }
    return formatter.stringFromDate(dateFormatter.dateFromString(dateString) ?: NSDate())
}

actual fun platformModule() = module {
    single { Darwin.create() }
}

@Suppress("UnUsed")
actual class DecimalFormat {
    actual fun format(double: Double, maximumFractionDigits: Int): String {
        val formatter = NSNumberFormatter()
        formatter.minimumFractionDigits = 0u
        formatter.maximumFractionDigits = maximumFractionDigits.toULong()
        formatter.numberStyle = 1u //Decimal
        return formatter.stringFromNumber(NSNumber(double))!!
    }
}