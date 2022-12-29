package multi.platform.core.shared.app.gallery

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import multi.platform.core.shared.databinding.GalleryItemBinding
import multi.platform.core.shared.external.extension.loadImage

class GalleryAdapter : RecyclerView.Adapter<GalleryAdapter.ViewHolder>() {
    var items = mutableListOf<String>()
    var onClick: ((View, String) -> Unit)? = null

    inner class ViewHolder(
        private val binding: GalleryItemBinding,
        private val onClick: ((View, String) -> Unit)?,
    ) :
        RecyclerView.ViewHolder(binding.root) {

        @Suppress("kotlin:S1186")
        fun bind(item: String) {
            binding.ivGallery.loadImage(item)
            binding.ivGallery.clipToOutline = true
            binding.root.setOnClickListener {
                onClick?.invoke(binding.ivGallery, item)
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            GalleryItemBinding.inflate(
                LayoutInflater.from(viewGroup.context),
                viewGroup,
                false
            )
        return ViewHolder(binding, onClick)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bind(items[position])
    }

    override fun getItemCount() = items.size

    @Suppress("Unused")
    fun clear() {
        val size = items.size
        items = mutableListOf()
        notifyItemRangeRemoved(0, size)
    }
}