package multi.platform.core.shared.external.extension

import java.text.NumberFormat

@Suppress("UnUsed")
fun Double.roundAsString(maxFractionDigits: Int = 0): String {
    val nf = NumberFormat.getInstance()
    nf.maximumFractionDigits = maxFractionDigits
    return nf.format(this)
}

@Suppress("UnUsed")
fun Double.toCurrency(symbol: String, maxFractionDigits: Int = 0) =
    this.roundAsString(maxFractionDigits).toCurrency(symbol)