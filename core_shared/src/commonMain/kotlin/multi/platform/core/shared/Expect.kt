package multi.platform.core.shared

import org.koin.core.module.Module

expect class Context

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
expect fun getLanguage(context: Context?): String

@OptIn(ExperimentalMultiplatform::class)
@OptionalExpectation
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.BINARY)
expect annotation class Parcelize

expect interface Parcelable

expect fun getStringPref(context: Context?, name: String, key: String, default: String?): String?
expect fun putStringPref(context: Context?, name: String, key: String, value: String?)
expect fun removePref(context: Context?, name: String, key: String)
expect fun formatDate(dateString: String, fromFormat: String, toFormat: String): String

expect fun platformModule(): Module

expect class DecimalFormat() {
    fun format(double: Double, maximumFractionDigits: Int): String
}