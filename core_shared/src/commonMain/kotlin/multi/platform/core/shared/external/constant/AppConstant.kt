@file:Suppress("Unused")

package multi.platform.core.shared.external.constant

object AppConstant {
    // Default Values
    const val CHANNEL = "APPTRAVELLER"
    const val LIST_LIMIT = 10
    const val CURRENCY = "Rp "
    const val LOADING = "loading..."

    // Key List
    const val ACCESS_TOKEN_KEY = "ACCESS_TOKEN"
    const val REFRESH_TOKEN_KEY = "REFRESH_TOKEN"
    const val PHONE_KEY = "PHONE"
    const val FILTERED_KEY = "FILTERED"
    const val SELECT_DATE_KEY = "select_date"
    const val ONBOARDING_KEY = "ONBOARDING"
    const val PAYMENT_KEY = "payment"
    const val RETRY_KEY = "retry"

    // Date Format
    const val NORMAL_DATE_FORMAT = "dd-MM-yyyy"
    const val API_DATE_FORMAT = "yyyy-MM-dd"

    // Oauth
    object Oauth {
        var API_AUTH = "/v1/authorization"
        var API_REFRESH = "/v1/token"
    }
}