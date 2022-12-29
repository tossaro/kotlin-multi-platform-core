package multi.platform.core.shared.app.common

import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.google.android.material.internal.ViewUtils.dpToPx
import multi.platform.core.shared.R
import multi.platform.core.shared.databinding.GenericItemBinding
import multi.platform.core.shared.domain.common.entity.GenericItem
import multi.platform.core.shared.external.constant.AppConstant
import multi.platform.core.shared.external.extension.loadImage

class GenericAdapter(
    private val widthRatio: Double = 1.0,
    private val height: Int = 0,
    private val radius: Int = 0,
    private val elevation: Int = 0,
) : RecyclerView.Adapter<GenericAdapter.ViewHolder>() {

    var items = mutableListOf<GenericItem>()
    var itemsCache = mutableListOf<GenericItem>()
    var isLastPage = false
    var skeleton = GenericItem()
    var fetchData: (() -> Unit)? = null
    var onSelected: ((GenericItem) -> Unit)? = null
    var onClickMore: ((GenericItem) -> Unit)? = null
    private val dataType = 1
    private val loadingType = 2

    @Suppress("RestrictedApi")
    inner class ViewHolder(
        private val binding: GenericItemBinding,
        private val radius: Int,
        private val elevation: Int,
        private val onClick: ((GenericItem) -> Unit)?,
    ) :
        RecyclerView.ViewHolder(binding.root) {
        private val colors = listOf(
            R.color.greenLight,
            R.color.mustardLight,
            R.color.redDark,
            R.color.blueSoft
        )

        private fun isLoading(item: GenericItem) =
            item.id == 0 && (item.name?.isEmpty() == true || item.title?.isEmpty() == true || item.titleOverlay?.isEmpty() == true)

        @Suppress("kotlin:S1186")
        fun bind(item: GenericItem) {
            val context = binding.root.context
            if (isLoading(item)) {
                val anim = AnimationUtils.loadAnimation(context, R.anim.left_right)
                binding.shine.isVisible = true
                binding.shine.startAnimation(anim)
                anim.setAnimationListener(object : Animation.AnimationListener {
                    override fun onAnimationStart(p0: Animation?) {
                    }

                    override fun onAnimationEnd(p0: Animation?) {
                        binding.shine.startAnimation(anim)
                    }

                    override fun onAnimationRepeat(p0: Animation?) {
                    }

                })
            } else if (item.id == 0 && (item.title?.isNotEmpty() == true || item.moreInfo?.isNotEmpty() == true)) {
                binding.cvGeneric.setBackgroundResource(android.R.color.transparent)
                binding.tvGenericMoreInfo.setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.negative
                    )
                )
            } else binding.shine.isVisible = false

            binding.cvGeneric.radius = dpToPx(context, radius)
            binding.cvGeneric.elevation = dpToPx(context, elevation)
            if (elevation > 0) binding.cvGeneric.useCompatPadding = true

            setupImage(item)
            setupTitle(item)
            setupSubTitle(item)
            setupDescription(item)
            setupPrice(item)
            setupDiscount(item)
            setupTopTags(context, item)
            setupBottomTags(context, item)
            setupMoreInfo(context, item)

            if (item.id != 0) binding.root.setOnClickListener { onClick?.invoke(item) }
            binding.tvGenericMoreInfo.setOnClickListener { onClickMore?.invoke(item) }
        }

        private fun setupImage(item: GenericItem) {
            binding.ivGenericFull.isVisible = false
            item.fullImage?.let {
                binding.ivGenericFull.isVisible = true
                if (item.id != 0 && it.isNotEmpty()) binding.ivGenericFull.loadImage(it)
            }
            binding.ivGenericTop.isVisible = false
            item.topImage?.let {
                binding.ivGenericTop.isVisible = true
                if (item.id != 0 && it.isNotEmpty()) binding.ivGenericTop.loadImage(it)
            }
            binding.ivGenericLeft.isVisible = false
            item.leftImage?.let {
                binding.ivGenericLeft.isVisible = true
                if (item.id != 0 && it.isNotEmpty()) binding.ivGenericLeft.loadImage(it)
            }
        }

        private fun setupTitle(item: GenericItem) {
            binding.tvGenericName.isVisible = false
            item.name?.let {
                binding.tvGenericName.apply {
                    text = it
                    isVisible = true
                    if (isLoading(item)) setBackgroundResource(R.color.grey20)
                }
            }
            binding.tvGenericTitle.isVisible = false
            item.title?.let {
                binding.tvGenericTitle.apply {
                    text = it
                    isVisible = true
                    if (isLoading(item)) setBackgroundResource(R.color.grey20)
                }
            }
            binding.tvGenericTitleOverlay.isVisible = false
            item.titleOverlay?.let {
                binding.tvGenericTitleOverlay.apply {
                    text = it
                    isVisible = true
                    if (isLoading(item)) setBackgroundResource(R.color.grey20)
                    else binding.vGenericOverlay.isVisible = true
                }
            }
        }

        private fun setupSubTitle(item: GenericItem) {
            binding.tvGenericSubtitle.isVisible = false
            item.subtitleIconRes?.let {
                binding.tvGenericSubtitle.setCompoundDrawablesWithIntrinsicBounds(it, 0, 0, 0)
                if (isLoading(item)) binding.tvGenericName.setBackgroundResource(
                    R.color.grey20
                )
            }
            binding.tvGenericSubtitle.isVisible = false
            item.subtitle?.let {
                binding.tvGenericSubtitle.apply {
                    text = it
                    isVisible = true
                    if (isLoading(item)) setBackgroundResource(R.color.grey20)
                }
            }
            binding.tvGenericSubtitleOverlay.isVisible = false
            item.subtitleOverlay?.let {
                binding.tvGenericSubtitleOverlay.apply {
                    text = it
                    isVisible = true
                    if (isLoading(item)) setBackgroundResource(R.color.grey20)
                    else binding.vGenericOverlay.isVisible = true
                }
            }
        }

        private fun setupDescription(item: GenericItem) {
            binding.tvGenericDescription.isVisible = false
            item.description?.let {
                binding.tvGenericDescription.apply {
                    text = it
                    isVisible = true
                    if (isLoading(item)) setBackgroundResource(R.color.grey20)
                }
            }
            item.descriptionIconRes?.let {
                binding.tvGenericDescription.setCompoundDrawablesWithIntrinsicBounds(it, 0, 0, 0)
            }
        }

        private fun setupPrice(item: GenericItem) {
            binding.tvGenericPriceMiddle.isVisible = false
            item.middlePrice?.let {
                binding.tvGenericPriceMiddle.apply {
                    text = it
                    isVisible = true
                    if (isLoading(item)) setBackgroundResource(R.color.grey20)
                }
            }
            binding.tvGenericPriceMiddleUnit.isVisible = false
            item.middlePriceUnit?.let {
                binding.tvGenericPriceMiddleUnit.apply {
                    text = it
                    isVisible = true
                    if (isLoading(item)) setBackgroundResource(R.color.grey20)
                }
            }
            binding.tvGenericPriceRight.isVisible = false
            item.rightPrice?.let {
                binding.tvGenericPriceRight.apply {
                    text = it
                    isVisible = true
                    if (isLoading(item)) setBackgroundResource(R.color.grey20)
                }
            }
        }

        private fun setupDiscount(item: GenericItem) {
            binding.tvGenericDiscountMiddle.isVisible = false
            item.middleDiscount?.let {
                binding.tvGenericDiscountMiddle.apply {
                    text = it
                    paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                    isVisible = true
                    if (isLoading(item)) setBackgroundResource(R.color.grey20)
                }
            }
            binding.tvGenericDiscountRight.isVisible = false
            item.rightDiscount?.let {
                binding.tvGenericDiscountRight.apply {
                    text = it
                    paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                    isVisible = true
                    if (isLoading(item)) setBackgroundResource(R.color.grey20)
                }
            }
        }

        private fun setupTopTags(context: Context, item: GenericItem) {
            binding.cgGenericTagsTop.isVisible = false
            item.topTags?.let {
                binding.cgGenericTagsTop.removeAllViews()
                it.forEach { t ->
                    var ranColor = colors.asSequence().shuffled().toList()[0]
                    if (t.isEmpty()) ranColor = R.color.grey20
                    binding.cgGenericTagsTop.addView(Chip(context).apply {
                        text = t
                        textAlignment = View.TEXT_ALIGNMENT_CENTER
                        isEnabled = false
                        id = View.generateViewId()
                        chipStrokeWidth = 0f
                        chipMinHeight = dpToPx(context, 16)
                        setEnsureMinTouchTargetSize(false)
                        setChipBackgroundColorResource(ranColor)
                        setTextAppearanceResource(R.style.Text_Poppins_Body5_Regular)
                        setPadding(0, 0, 0, 0)
                    })
                }
                binding.cgGenericTagsTop.isVisible = true
            }
        }

        private fun setupBottomTags(context: Context, item: GenericItem) {
            binding.cgGenericTagsBottom.isVisible = false
            item.bottomTags?.let {
                binding.cgGenericTagsBottom.removeAllViews()
                it.forEach { t ->
                    var ranColor = colors.asSequence().shuffled().toList()[0]
                    if (t.isEmpty()) ranColor = R.color.grey20
                    binding.cgGenericTagsBottom.addView(Chip(context).apply {
                        text = t
                        textAlignment = View.TEXT_ALIGNMENT_CENTER
                        isEnabled = false
                        id = View.generateViewId()
                        chipStrokeWidth = 0f
                        chipMinHeight = dpToPx(context, 16)
                        setEnsureMinTouchTargetSize(false)
                        setChipBackgroundColorResource(ranColor)
                        setTextAppearanceResource(R.style.Text_Poppins_Body5_Regular)
                        setPadding(0, 0, 0, 0)
                    })
                }
                binding.cgGenericTagsBottom.isVisible = true
            }
        }

        private fun setupMoreInfo(context: Context, item: GenericItem) {
            binding.tvGenericMoreInfo.isVisible = false
            item.moreInfo?.let {
                binding.tvGenericMoreInfo.text = it
                binding.tvGenericMoreInfo.isVisible = true
                binding.ivGenericLeft.updateLayoutParams<ViewGroup.LayoutParams> {
                    width = dpToPx(context, 138).toInt()
                }
                if (item.id == 0 && it.isEmpty()) binding.tvGenericMoreInfo.setBackgroundResource(
                    R.color.grey20
                )
            }
        }
    }

    inner class Listener : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            if (!recyclerView.canScrollVertically(1) && !isLastPage) {
                fetchData?.invoke()
            }
        }
    }

    @Suppress("RestrictedApi")
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            GenericItemBinding.inflate(
                LayoutInflater.from(viewGroup.context),
                viewGroup,
                false
            )
        if (widthRatio != 1.0) binding.root.layoutParams.width =
            (viewGroup.measuredWidth * widthRatio).toInt()
        if (height != 0) binding.root.layoutParams.height =
            dpToPx(binding.root.context, height).toInt()
        return ViewHolder(binding, radius, elevation, onSelected)
    }

    override fun getItemViewType(position: Int) =
        if (items[position].id == 0) loadingType else dataType

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bind(items[position])
    }

    override fun getItemCount() = items.size

    fun clear() {
        val size = items.size
        items.clear()
        itemsCache.clear()
        notifyItemRangeRemoved(0, size)
    }

    fun showSkeletons(size: Int = AppConstant.LIST_LIMIT) {
        clear()
        val skeletons = mutableListOf<GenericItem>()
        for (i in 0 until size) skeletons.add(skeleton)
        items = skeletons
        notifyItemRangeInserted(0, skeletons.size - 1)
    }

    fun showInfo(desc: String, button: String? = null) {
        clear()
        items = mutableListOf(GenericItem(title = desc, moreInfo = button))
        notifyItemInserted(0)
    }

    fun addItem(process: () -> Unit) {
        if (itemsCache.size == 0 && items.size > 0) {
            clear()
        }
        val sizeBefore = items.size
        process.invoke()
        val sizeAfter = items.size
        if ((sizeAfter - sizeBefore) < AppConstant.LIST_LIMIT) {
            isLastPage = true
        }
        notifyItemRangeInserted(sizeBefore, sizeAfter - 1)
    }
}