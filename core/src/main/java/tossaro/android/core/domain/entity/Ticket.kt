package tossaro.android.core.domain.entity

import com.google.gson.annotations.SerializedName

data class Ticket(
    @SerializedName("ticket_id") var ticketId: String,
    @SerializedName("duration") var duration: Long,
    @SerializedName("type") var type: Int?,
)