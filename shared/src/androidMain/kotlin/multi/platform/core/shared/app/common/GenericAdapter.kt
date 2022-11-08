package multi.platform.core.shared.app.common

import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.google.android.material.internal.ViewUtils.dpToPx
import multi.platform.core.shared.R
import multi.platform.core.shared.databinding.GenericItemBinding
import multi.platform.core.shared.domain.common.entity.GenericItem
import multi.platform.core.shared.external.extension.loadImage

class GenericAdapter(
    private val widthRatio: Double = 1.0,
    private val height: Int = 0,
    private val radius: Int = 0,
    private val elevation: Int = 0,
) : RecyclerView.Adapter<GenericAdapter.ViewHolder>() {

    var items = mutableListOf<GenericItem>()
    var onClick: ((GenericItem) -> Unit)? = null
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

        @Suppress("kotlin:S1186")
        fun bind(item: GenericItem) {
            val context = binding.root.context
            if (item.id == 0) {
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
            } else binding.shine.isVisible = false

            binding.cvGeneric.radius = dpToPx(context, radius)
            binding.cvGeneric.elevation = dpToPx(context, elevation)
            if (elevation > 0) binding.cvGeneric.useCompatPadding = true

            setImage(item)
            setTitle(item)
            setSubTitle(item)
            setDescription(item)
            setPrice(item)
            setTopTags(context, item)
            setBottomTags(context, item)
            setMoreInfo(context, item)

            binding.root.setOnClickListener {
                onClick?.invoke(item)
            }

        }

        private fun setImage(item: GenericItem) {
            item.fullImage?.let {
                binding.ivGenericFull.loadImage(it)
                binding.ivGenericFull.isVisible = true
                if (item.id == 0) binding.ivGenericFull.setBackgroundResource(R.color.grey20)
            }
            item.topImage?.let {
                binding.ivGenericTop.loadImage(it)
                binding.ivGenericTop.isVisible = true
                if (item.id == 0) binding.ivGenericTop.setBackgroundResource(R.color.grey20)
            }
            item.leftImage?.let {
                binding.ivGenericLeft.loadImage(it)
                binding.ivGenericLeft.isVisible = true
                if (item.id == 0) binding.ivGenericLeft.setBackgroundResource(R.color.grey20)
            }
        }

        private fun setTitle(item: GenericItem) {
            item.name?.let {
                binding.tvGenericName.apply {
                    text = it
                    isVisible = true
                    if (item.id == 0) setBackgroundResource(R.color.grey20)
                }
            }
            item.title?.let {
                binding.tvGenericTitle.apply {
                    text = it
                    isVisible = true
                    if (item.id == 0) setBackgroundResource(R.color.grey20)
                }
            }
            item.titleOverlay?.let {
                binding.tvGenericTitleOverlay.apply {
                    text = it
                    isVisible = true
                    if (item.id == 0) setBackgroundResource(R.color.grey20)
                }
                binding.vGenericOverlay.isVisible = true
            }
        }

        private fun setSubTitle(item: GenericItem) {
            item.subtitleIconRes?.let {
                binding.tvGenericSubtitle.setCompoundDrawablesWithIntrinsicBounds(it, 0, 0, 0)
                if (item.id == 0) binding.tvGenericName.setBackgroundResource(R.color.grey20)
            }
            item.subtitle?.let {
                binding.tvGenericSubtitle.apply {
                    text = it
                    isVisible = true
                    if (item.id == 0) setBackgroundResource(R.color.grey20)
                }
            }
            item.subtitleOverlay?.let {
                binding.tvGenericSubtitleOverlay.apply {
                    text = it
                    isVisible = true
                    if (item.id == 0) setBackgroundResource(R.color.grey20)
                }
                binding.vGenericOverlay.isVisible = true
            }
        }

        private fun setDescription(item: GenericItem) {
            item.description?.let {
                binding.tvGenericDescription.apply {
                    text = it
                    isVisible = true
                    if (item.id == 0) setBackgroundResource(R.color.grey20)
                }
            }
            item.descriptionIconRes?.let {
                binding.tvGenericDescription.setCompoundDrawablesWithIntrinsicBounds(it, 0, 0, 0)
            }
        }

        private fun setPrice(item: GenericItem) {
            item.middlePrice?.let {
                binding.tvGenericPriceMiddle.apply {
                    text = it
                    isVisible = true
                    if (item.id == 0) setBackgroundResource(R.color.grey20)
                }
            }
            item.middlePriceUnit?.let {
                binding.tvGenericPriceMiddleUnit.apply {
                    text = it
                    isVisible = true
                    if (item.id == 0) setBackgroundResource(R.color.grey20)
                }
            }
            item.rightPrice?.let {
                binding.tvGenericPriceRight.apply {
                    text = it
                    isVisible = true
                    if (item.id == 0) setBackgroundResource(R.color.grey20)
                }
            }
            item.middleDiscount?.let {
                binding.tvGenericDiscountMiddle.apply {
                    text = it
                    paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                    isVisible = true
                    if (item.id == 0) setBackgroundResource(R.color.grey20)
                }
            }
            item.rightDiscount?.let {
                binding.tvGenericDiscountRight.apply {
                    text = it
                    paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                    isVisible = true
                    if (item.id == 0) setBackgroundResource(R.color.grey20)
                }
            }
        }

        private fun setTopTags(context: Context, item: GenericItem) {
            item.topTags?.let {
                binding.cgGenericTagsTop.removeAllViews()
                it.forEach { t ->
                    val color = when (t) {
                        "Alam" -> R.color.greenLight
                        "Seni" -> R.color.mustardLight
                        "Budaya" -> R.color.redDark
                        "Edukasi" -> R.color.blueSoft
                        else -> R.color.grey20
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
        }

        private fun setBottomTags(context: Context, item: GenericItem) {
            item.bottomTags?.let {
                binding.cgGenericTagsBottom.removeAllViews()
                it.forEach { t ->
                    val color = when (t) {
                        "Alam" -> R.color.greenLight
                        "Seni" -> R.color.mustardLight
                        "Budaya" -> R.color.redDark
                        "Edukasi" -> R.color.blueSoft
                        else -> R.color.grey20
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
        }

        private fun setMoreInfo(context: Context, item: GenericItem) {
            item.moreInfo?.let {
                binding.tvGenericMoreInfo.text = it
                binding.tvGenericMoreInfo.isVisible = true
                binding.ivGenericLeft.updateLayoutParams<ViewGroup.LayoutParams> {
                    width = dpToPx(context, 138).toInt()
                }
                if (item.id == 0) binding.tvGenericMoreInfo.setBackgroundResource(R.color.grey20)
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

    override fun getItemViewType(position: Int) =
        if (items[position].id == 0) loadingType else dataType

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bind(items[position])
    }

    override fun getItemCount() = items.size

    fun clear() {
        val size = items.size
        items = mutableListOf()
        notifyItemRangeRemoved(0, size)
    }

}