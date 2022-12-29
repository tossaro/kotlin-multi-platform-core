package multi.platform.core.shared.external.utility

object Validation {

    fun notBlank(data: CharSequence?) = data?.isNotBlank()

    fun minCharacter(minCharacter: Int, data: CharSequence?): Boolean? {
        if (data == null) return null
        return data.length >= minCharacter
    }

    fun emailFormat(data: CharSequence?): Boolean? {
        if (data == null) return null
        return data.isNotEmpty() && Regex("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}\$").matches(
            data
        )
    }

    fun phoneFormat(data: CharSequence?): Boolean? {
        if (data == null) return null
        return data.isNotEmpty() && Regex("\\+?([ -]?\\d+)+|\\(\\d+\\)([ -]\\d+)").matches(
            data
        )
    }
}