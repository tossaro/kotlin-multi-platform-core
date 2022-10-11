package tossaro.android.core.app.common

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.google.android.material.internal.ViewUtils.dpToPx
import tossaro.android.core.R
import tossaro.android.core.databinding.GenericItemBinding
import tossaro.android.core.domain.common.entity.GenericItem
import tossaro.android.core.external.extension.loadImage

class GenericAdapter(
    private val widthRatio: Double = 1.0,
    private val height: Int = 0,
    private val radius: Int = 0,
    private val elevation: Int = 0,
) : RecyclerView.Adapter<GenericAdapter.ViewHolder>() {

    var items = mutableListOf<GenericItem>()
    var onClick: ((GenericItem) -> Unit)? = null

    class ViewHolder(
        private val binding: GenericItemBinding,
        private val radius: Int,
        private val elevation: Int,
        private val onClick: ((GenericItem) -> Unit)?,
    ) :
        RecyclerView.ViewHolder(binding.root) {
        @Suppress("RestrictedApi")
        fun bind(item: GenericItem) {
            val context = binding.root.context
            binding.cvGeneric.radius = dpToPx(context, radius)
            binding.cvGeneric.elevation = dpToPx(context, elevation)
            if (elevation > 0) binding.cvGeneric.useCompatPadding = true

            item.fullImage?.let {
                binding.ivGenericFull.loadImage(it)
                binding.ivGenericFull.isVisible = true
            }
            item.topImage?.let {
                binding.ivGenericTop.loadImage(it)
                binding.ivGenericTop.isVisible = true
            }
            item.leftImage?.let {
                binding.ivGenericLeft.loadImage(it)
                binding.ivGenericLeft.isVisible = true
            }
            item.name?.let {
                binding.tvGenericName.text = it
                binding.tvGenericName.isVisible = true
            }
            item.title?.let {
                binding.tvGenericTitle.text = it
                binding.tvGenericTitle.isVisible = true
            }
            item.titleOverlay?.let {
                binding.tvGenericTitleOverlay.text = it
                binding.tvGenericTitleOverlay.isVisible = true
                binding.vGenericOverlay.isVisible = true
            }
            item.subtitleIconRes?.let {
                binding.tvGenericSubtitle.setCompoundDrawablesWithIntrinsicBounds(it, 0, 0, 0)
            }
            item.subtitle?.let {
                binding.tvGenericSubtitle.text = it
                binding.tvGenericSubtitle.isVisible = true
            }
            item.subtitleOverlay?.let {
                binding.tvGenericSubtitleOverlay.text = it
                binding.tvGenericSubtitleOverlay.isVisible = true
                binding.vGenericOverlay.isVisible = true
            }
            item.description?.let {
                binding.tvGenericDescription.text = it
                binding.tvGenericDescription.isVisible = true
            }
            item.descriptionIconRes?.let {
                binding.tvGenericDescription.setCompoundDrawablesWithIntrinsicBounds(it, 0, 0, 0)
            }
            item.middlePrice?.let {
                binding.tvGenericPriceMiddle.text = it
                binding.tvGenericPriceMiddle.isVisible = true
            }
            item.middlePriceUnit?.let {
                binding.tvGenericPriceMiddleUnit.text = it
                binding.tvGenericPriceMiddleUnit.isVisible = true
            }
            item.rightPrice?.let {
                binding.tvGenericPriceRight.text = it
                binding.tvGenericPriceRight.isVisible = true
            }
            item.middleDiscount?.let {
                binding.tvGenericDiscountMiddle.apply {
                    text = it
                    paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                    isVisible = true
                }
            }
            item.rightDiscount?.let {
                binding.tvGenericDiscountRight.apply {
                    text = it
                    paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                    isVisible = true
                }
            }
            item.title?.let {
                binding.tvGenericTitle.text = it
                binding.tvGenericTitle.isVisible = true
            }
            item.topTags?.let {
                binding.cgGenericTagsTop.removeAllViews()
                it.forEach { t ->
                    val color = when (t) {
                        "Alam" -> R.color.greenLight
                        "Seni" -> R.color.mustardLight
                        "Budaya" -> R.color.redDark
                        "Edukasi" -> R.color.blueSoft
                        else -> R.color.greenLight
                    }
                    binding.cgGenericTagsTop.addView(Chip(context).apply {
                        text = t
                        textAlignment = View.TEXT_ALIGNMENT_CENTER
                        isEnabled = false
                        id = View.generateViewId()
                        chipStrokeWidth = 0f
                        chipMinHeight = dpToPx(context, 16)
                        setEnsureMinTouchTargetSize(false)
                        setChipBackgroundColorResource(color)
                        setTextAppearanceResource(R.style.Text_Poppins_Body5_Regular)
                        setPadding(0, 0, 0, 0)
                    })
                }
                binding.cgGenericTagsTop.isVisible = true
            }
            item.bottomTags?.let {
                binding.cgGenericTagsBottom.removeAllViews()
                it.forEach { t ->
                    val color = when (t) {
                        "Alam" -> R.color.greenLight
                        "Seni" -> R.color.mustardLight
                        "Budaya" -> R.color.redDark
                        "Edukasi" -> R.color.blueSoft
                        else -> R.color.greenLight
                    }
                    binding.cgGenericTagsBottom.addView(Chip(context).apply {
                        text = t
                        textAlignment = View.TEXT_ALIGNMENT_CENTER
                        isEnabled = false
                        id = View.generateViewId()
                        chipStrokeWidth = 0f
                        chipMinHeight = dpToPx(context, 16)
                        setEnsureMinTouchTargetSize(false)
                        setChipBackgroundColorResource(color)
                        setTextAppearanceResource(R.style.Text_Poppins_Body5_Regular)
                        setPadding(0, 0, 0, 0)
                    })
                }
                binding.cgGenericTagsBottom.isVisible = true
            }
            item.moreInfo?.let {
                binding.tvGenericMoreInfo.text = it
                binding.tvGenericMoreInfo.isVisible = true
                binding.ivGenericLeft.updateLayoutParams<ViewGroup.LayoutParams> {
                    width = dpToPx(context, 138).toInt()
                }
            }
            binding.root.setOnClickListener {
                onClick?.invoke(item)
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
        return ViewHolder(binding, radius, elevation, onClick)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bind(items[position])
    }

    override fun getItemCount() = items.size

    @Suppress("UNUSED")
    fun clear() {
        val size = items.size
        items = mutableListOf()
        notifyItemRangeRemoved(0, size)
    }

}