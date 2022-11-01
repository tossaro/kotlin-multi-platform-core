package multi.platform.core.shared

expect abstract class Context

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
expect fun getLanguage(context: Context): String

@OptIn(ExperimentalMultiplatform::class)
@OptionalExpectation
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.BINARY)
expect annotation class Parcelize

expect interface Parcelable

expect fun getStringPref(context: Context, name: String, key: String, default: String?): String?
expect fun putStringPref(context: Context, name: String, key: String, value: String?)
expect fun removePref(context: Context, name: String, key: String)