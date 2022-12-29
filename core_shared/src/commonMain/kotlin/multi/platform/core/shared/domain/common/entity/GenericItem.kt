package multi.platform.core.shared.domain.common.entity

data class GenericItem(
    var id: Int? = 0,
    var fullImage: String? = null,
    var topImage: String? = null,
    var leftImage: String? = null,
    var name: String? = null,
    var title: String? = null,
    var titleOverlay: String? = null,
    var subtitle: String? = null,
    var subtitleOverlay: String? = null,
    var subtitleIconRes: Int? = 0,
    var description: String? = null,
    var descriptionIconRes: Int? = 0,
    var moreInfo: String? = null,
    var topTags: MutableList<String>? = null,
    var bottomTags: MutableList<String>? = null,
    var middleDiscount: String? = null,
    var middlePrice: String? = null,
    var middlePriceUnit: String? = null,
    var rightDiscount: String? = null,
    var rightPrice: String? = null,
) {
    @Suppress("UnUsed")
    constructor() : this(id = null)

    @Suppress("UnUsed")
    constructor(
        _id: Int?,
        _title: String?,
        _subtitle: String?
    ) : this(
        id = _id,
        title = _title,
        subtitle = _subtitle
    )

    @Suppress("UnUsed")
    constructor(
        _id: Int?,
        _fullImage: String?,
        _titleOverlay: String?,
        _subtitleOverlay: String?,
    ) : this(
        id = _id,
        fullImage = _fullImage,
        titleOverlay = _titleOverlay,
        subtitleOverlay = _subtitleOverlay
    )

    @Suppress("UnUsed")
    constructor(
        _id: Int?,
        _topImage: String?,
        _title: String?,
        _subtitle: String?,
        _rightDiscount: String?,
        _rightPrice: String?,
    ) : this(
        id = _id,
        topImage = _topImage,
        title = _title,
        subtitle = _subtitle,
        rightDiscount = _rightDiscount,
        rightPrice = _rightPrice,
    )

    @Suppress("UnUsed")
    constructor(
        _id: Int?,
        _leftImage: String?,
        _title: String?,
        _subtitle: String?,
        _middleDiscount: String?,
        _middlePrice: String?,
        _middlePriceUnit: String?,
    ) : this(
        id = _id,
        leftImage = _leftImage,
        title = _title,
        subtitle = _subtitle,
        middleDiscount = _middleDiscount,
        middlePrice = _middlePrice,
        middlePriceUnit = _middlePriceUnit,
    )
}