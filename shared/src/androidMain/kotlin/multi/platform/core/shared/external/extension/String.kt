package multi.platform.core.shared.external.extension

import android.os.Build
import android.text.Html
import android.text.Spanned
import java.security.MessageDigest
import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone
import java.util.regex.Pattern

@Suppress("UNUSED")
fun String?.orDash(): String = this.takeIf { !it.isNullOrBlank() } ?: "-"

@Suppress("UNUSED")
fun String.buildUtcDateFormat() = SimpleDateFormat(this, Locale.getDefault()).apply {
    timeZone = TimeZone.getTimeZone("UTC")
}

@Suppress("UNUSED")
fun String.buildSimpleDateFormat() = SimpleDateFormat(this, Locale.getDefault())

@Suppress("UNUSED")
fun String.getAlphaNumericDashAfterColon(): String {
    if (this.isNotBlank()) {
        val pattern = Pattern.compile(": (\\w+)")
        val matcher = pattern.matcher(this)
        if (matcher.find()) {
            val result = matcher.group()
            return result.substring(result.lastIndexOf(' ') + 1)
        }
    }
    return ""
}

@Suppress("UNUSED")
fun String.substringBetween(startDelimiter: String, endDelimiter: String): String {
    val string = this
    val startIndex = string.lastIndexOf(startDelimiter)
    val endIndex = string.indexOf(endDelimiter)
    return if (startIndex > -1) string.substring(startIndex, endIndex) else ""
}

@Suppress("UNUSED")
fun String.toCurrency(symbol: String): String {
    var result = replace(symbol, "")
    if (result.isNotEmpty()) {
        try {
            result = result.replace(",", "").replace(".", "")
            val strLong = result.toLong()
            val formatter = NumberFormat.getInstance(Locale.US) as DecimalFormat
            formatter.applyPattern("#,###,###,###")
            var formatted = formatter.format(strLong)
            if (symbol == "Rp " || symbol == "IDR ") formatted = formatted.replace(",", ".")
            result = symbol + formatted
        } catch (e: java.lang.NumberFormatException) {
            e.printStackTrace()
            result = this
        }
    }
    return result
}

@Suppress("DEPRECATION", "UNUSED", "kotlin:S1874")
fun String.toHtml(): Spanned? {
    if (this.isNotEmpty()) {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) Html.fromHtml(
            this,
            Html.FROM_HTML_MODE_LEGACY
        )
        else Html.fromHtml(this)
    }
    return null
}

fun String.toMD5(): String {
    val bytes = MessageDigest.getInstance("MD5").digest(this.toByteArray())
    return bytes.joinToString("") { "%02x".format(it) }
}