@file:Suppress("unused")

package tossaro.android.core.domain.common.entity

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class Ticket(
    @SerializedName("timestamp") var timestamp: Long?,
    @SerializedName("state") var state: String?,
    @SerializedName("transaction_id") var transactionId: String?,
    @SerializedName("otp") var otp: Otp?,
    @SerializedName("session") var session: Session?,
)