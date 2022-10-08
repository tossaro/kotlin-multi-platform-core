package tossaro.android.core.external.utility

import android.util.Patterns
import com.google.i18n.phonenumbers.PhoneNumberUtil
import com.google.i18n.phonenumbers.Phonenumber

object ValidationUtil {

    fun notBlank(data: String?) = data?.isNotBlank()

    fun minCharacter(minCharacter: Int, data: String?) = data != null && data.length >= minCharacter

    fun emailFormat(data: CharSequence) =
        data.toString().isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(data).matches()

    fun phoneFormat(countryCode: String?, data: CharSequence): Boolean {
        val countryCodeValid =
            (countryCode == null) || isPhoneNumberRegion(data.toString(), countryCode)
        return Patterns.PHONE.matcher(data).matches() && countryCodeValid
    }

    private fun isPhoneNumberRegion(data: String?, countryCode: String): Boolean {
        val alteredCountryCode = countryCode.replace("+", "").toIntOrNull() ?: return false
        val phoneNumber = data?.toLongOrNull() ?: return false
        val phoneUtil = PhoneNumberUtil.getInstance()
        val internationalPhoneNumber = Phonenumber.PhoneNumber()
        internationalPhoneNumber.countryCode = alteredCountryCode
        internationalPhoneNumber.nationalNumber = phoneNumber
        return phoneUtil?.isValidNumber(internationalPhoneNumber) ?: false
    }
}