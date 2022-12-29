package multi.platform.example_lib.shared.domain.stock.entity

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
open class Stock : RealmObject {
    @PrimaryKey
    @SerialName("Id")
    var id: String = "primary"

    @SerialName("Name")
    var name: String? = null

    @SerialName("FullName")
    var fullname: String? = null
    @SerialName("ImageUrl")
    var imageUrl: String? = null
    var price: String? = null
    var status: String? = null
}