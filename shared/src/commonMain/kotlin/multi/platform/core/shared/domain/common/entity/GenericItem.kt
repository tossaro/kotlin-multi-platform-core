package multi.platform.core.shared.domain.common.entity

import kotlinx.serialization.Serializable

@Serializable
data class GenericItem(
    val id: Int? = 0,
    val fullImage: String? = null,
    val topImage: String? = null,
    val leftImage: String? = null,
    val name: String? = null,
    val title: String? = null,
    val titleOverlay: String? = null,
    val subtitle: String? = null,
    val subtitleOverlay: String? = null,
    val subtitleIconRes: Int? = 0,
    val description: String? = null,
    val descriptionIconRes: Int? = 0,
    val moreInfo: String? = null,
    val topTags: MutableList<String>? = null,
    val bottomTags: MutableList<String>? = null,
    val middleDiscount: String? = null,
    val middlePrice: String? = null,
    val middlePriceUnit: String? = null,
    val rightDiscount: String? = null,
    val rightPrice: String? = null,
)