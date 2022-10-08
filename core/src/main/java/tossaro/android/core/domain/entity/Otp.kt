package tossaro.android.core.domain.entity

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class Otp(
    @SerializedName("duration") var duration: Long?,
    @SerializedName("msisdn") var msisdn: String?,
)