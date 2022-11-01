package multi.platform.core.example.domain.stock.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Entity
@Serializable
data class Stock(
    @PrimaryKey
    @SerialName("Id") val id: String,
    @SerialName("Name") val name: String?,
    @SerialName("FullName") val fullname: String?,
    var price: String?,
    var status: String?
)