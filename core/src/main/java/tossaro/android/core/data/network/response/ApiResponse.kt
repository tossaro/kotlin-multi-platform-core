package tossaro.android.core.data.network.response

import com.google.gson.annotations.SerializedName
import tossaro.android.core.domain.entity.Meta

data class ApiResponse<D>(
    @SerializedName("meta") var meta: Meta,
    @SerializedName("data") var data: D,
)