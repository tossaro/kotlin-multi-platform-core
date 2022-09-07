package tossaro.android.core.external.utility

import com.google.i18n.phonenumbers.PhoneNumberUtil
import com.google.i18n.phonenumbers.Phonenumber
import java.util.regex.Pattern

object ValidationUtil {

    fun notBlank(data: String?) = data?.isNotBlank()

    fun minCharacter(minCharacter: Int, data: String?) = data != null && data.length >= minCharacter

    fun emailFormat(data: String?): Boolean? = data?.trim()?.matches(
        Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
        ).toRegex()
    )

    fun phoneFormat(countryCode: String?, data: String?): Boolean {
        val countryCodeValid = (countryCode == null) || isPhoneNumberRegion(data, countryCode)
        return data?.matches(
            Pattern.compile( // sdd = space, dot, or dash
                "(\\+[0-9]+[\\- \\.]*)?" // +<digits><sdd>*
                        + "(\\([0-9]+\\)[\\- \\.]*)?" // (<digits>)<sdd>*
                        + "([0-9][0-9\\- \\.]+[0-9])"
            ) // <digit><digit|sdd>+<digit>
                .toRegex()
        ) == true && countryCodeValid
    }

    fun isPhoneNumberRegion(data: String?, countryCode: String): Boolean {
        val alteredCountryCode = countryCode.replace("+", "").toIntOrNull() ?: return false
        val phoneNumber = data?.toLongOrNull() ?: return false
        val phoneUtil = PhoneNumberUtil.getInstance()
        val internationalPhoneNumber = Phonenumber.PhoneNumber()
        internationalPhoneNumber.countryCode = alteredCountryCode
        internationalPhoneNumber.nationalNumber = phoneNumber
        return phoneUtil?.isValidNumber(internationalPhoneNumber) ?: return false
    }
}