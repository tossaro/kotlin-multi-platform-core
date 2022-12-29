package multi.platform.core.shared.app.gallery

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.graphics.Point
import android.graphics.Rect
import android.graphics.RectF
import android.os.Bundle
import android.view.View
import android.view.animation.DecelerateInterpolator
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import multi.platform.core.shared.R
import multi.platform.core.shared.app.common.BaseFragment
import multi.platform.core.shared.databinding.GalleryFragmentBinding
import multi.platform.core.shared.external.extension.dpToPx
import multi.platform.core.shared.external.extension.loadImage

class GalleryFragment : BaseFragment<GalleryFragmentBinding>(R.layout.gallery_fragment) {
    private var currentAnimator: Animator? = null
    private var zoomDuration: Int = 250

    override fun actionBarTitle() = arguments?.getString("title") ?: " "
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val images = arguments?.getString("images", null)
        val imageAdapter = GalleryAdapter()
        imageAdapter.onClick = { v, i ->
            zoomImage(v, i)
        }
        images?.split(";")?.let {
            imageAdapter.items = it as MutableList<String>
        }
        binding.rvGallery.adapter = imageAdapter
        binding.rvGallery.isNestedScrollingEnabled = false
        val lm = binding.rvGallery.layoutManager as StaggeredGridLayoutManager
        lm.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE
        binding.rvGallery.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                val layoutParams = view.layoutParams as StaggeredGridLayoutManager.LayoutParams
                val spanIndex = layoutParams.spanIndex
                if (spanIndex == 0) outRect.right = dpToPx(4f)
                else outRect.left = dpToPx(4f)

                outRect.bottom = dpToPx(8f)
            }
        })
    }

    private fun zoomImage(thumbView: View, image: String) {
        currentAnimator?.cancel()
        binding.ivGalleryZoom.loadImage(image)

        val startBoundsInt = Rect()
        val finalBoundsInt = Rect()
        val globalOffset = Point()

        thumbView.getGlobalVisibleRect(startBoundsInt)
        binding.clGallery.getGlobalVisibleRect(finalBoundsInt, globalOffset)
        startBoundsInt.offset(-globalOffset.x, -globalOffset.y)
        finalBoundsInt.offset(-globalOffset.x, -globalOffset.y)

        val startBounds = RectF(startBoundsInt)
        val finalBounds = RectF(finalBoundsInt)

        val startScale: Float
        if ((finalBounds.width() / finalBounds.height() > startBounds.width() / startBounds.height())) {
            startScale = startBounds.height() / finalBounds.height()
            val startWidth: Float = startScale * finalBounds.width()
            val deltaWidth: Float = (startWidth - startBounds.width()) / 2
            startBounds.left -= deltaWidth.toInt()
            startBounds.right += deltaWidth.toInt()
        } else {
            startScale = startBounds.width() / finalBounds.width()
            val startHeight: Float = startScale * finalBounds.height()
            val deltaHeight: Float = (startHeight - startBounds.height()) / 2f
            startBounds.top -= deltaHeight.toInt()
            startBounds.bottom += deltaHeight.toInt()
        }

        thumbView.alpha = 0f
        binding.llGalleryZoom.isVisible = true

        binding.ivGalleryZoom.pivotX = 0f
        binding.ivGalleryZoom.pivotY = 0f

        currentAnimator = AnimatorSet().apply {
            play(
                ObjectAnimator.ofFloat(
                    binding.ivGalleryZoom,
                    View.X,
                    startBounds.left,
                    finalBounds.left
                )
            ).apply {
                with(
                    ObjectAnimator.ofFloat(
                        binding.ivGalleryZoom,
                        View.Y,
                        startBounds.top,
                        finalBounds.top
                    )
                )
                with(ObjectAnimator.ofFloat(binding.ivGalleryZoom, View.SCALE_X, startScale, 1f))
                with(ObjectAnimator.ofFloat(binding.ivGalleryZoom, View.SCALE_Y, startScale, 1f))
            }
            duration = zoomDuration.toLong()
            interpolator = DecelerateInterpolator()
            addListener(object : AnimatorListenerAdapter() {

                override fun onAnimationEnd(animation: Animator) {
                    currentAnimator = null
                }

                override fun onAnimationCancel(animation: Animator) {
                    currentAnimator = null
                }
            })
            start()
        }

        binding.llGalleryZoom.setOnClickListener {
            currentAnimator?.cancel()

            currentAnimator = AnimatorSet().apply {
                play(
                    ObjectAnimator.ofFloat(
                        binding.ivGalleryZoom,
                        View.X,
                        startBounds.left
                    )
                ).apply {
                    with(ObjectAnimator.ofFloat(binding.ivGalleryZoom, View.Y, startBounds.top))
                    with(ObjectAnimator.ofFloat(binding.ivGalleryZoom, View.SCALE_X, startScale))
                    with(ObjectAnimator.ofFloat(binding.ivGalleryZoom, View.SCALE_Y, startScale))
                }
                duration = zoomDuration.toLong()
                interpolator = DecelerateInterpolator()
                addListener(object : AnimatorListenerAdapter() {

                    override fun onAnimationEnd(animation: Animator) {
                        hide()
                    }

                    override fun onAnimationCancel(animation: Animator) {
                        hide()
                    }

                    fun hide() {
                        thumbView.alpha = 1f
                        binding.llGalleryZoom.isVisible = false
                        currentAnimator = null
                    }
                })
                start()
            }
        }
    }
}