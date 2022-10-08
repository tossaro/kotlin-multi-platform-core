package tossaro.android.core.external.extension

import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions

private const val FADE_IN_DURATION_MILLIS = 200

@Suppress("UNUSED")
fun ImageView.loadImage(
    any: Any,
    options: RequestOptions = RequestOptions()
) = loadImage(any, null, null, options)

@Suppress("UNUSED")
fun ImageView.loadImage(
    any: Any,
    errorPlaceholder: Any?,
    options: RequestOptions = RequestOptions()
) = loadImage(any, null, errorPlaceholder, options)

@Suppress("CheckResult", "UNUSED")
fun ImageView.loadImage(
    any: Any,
    loadingPlaceholder: Any?,
    errorPlaceholder: Any?,
    options: RequestOptions = RequestOptions()
) {
    when (loadingPlaceholder) {
        is Int -> options.placeholder(loadingPlaceholder)
        is Drawable -> options.placeholder(loadingPlaceholder)
    }
    when (errorPlaceholder) {
        is Int -> options.error(errorPlaceholder)
        is Drawable -> options.error(errorPlaceholder)
    }

    Glide.with(context).load(any)
        .transition(DrawableTransitionOptions.withCrossFade(FADE_IN_DURATION_MILLIS))
        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
        .apply(options)
        .into(this)
}

@Suppress("UNUSED")
fun ImageView.loadImageNoCache(
    any: Any,
    options: RequestOptions = RequestOptions()
) = loadImageNoCache(any, null, null, options)

@Suppress("UNUSED")
fun ImageView.loadImageNoCache(
    any: Any,
    errorPlaceholder: Any?,
    options: RequestOptions = RequestOptions()
) = loadImageNoCache(any, null, errorPlaceholder, options)

@Suppress("CheckResult", "UNUSED")
fun ImageView.loadImageNoCache(
    any: Any,
    loadingPlaceholder: Any?,
    errorPlaceholder: Any?,
    options: RequestOptions = RequestOptions()
) = loadImage(any, loadingPlaceholder, errorPlaceholder, options.apply {
    skipMemoryCache(true)
    diskCacheStrategy(DiskCacheStrategy.NONE)
})
