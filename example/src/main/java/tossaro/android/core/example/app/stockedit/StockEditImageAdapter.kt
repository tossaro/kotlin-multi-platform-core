package tossaro.android.core.example.app.stockedit

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import tossaro.android.core.example.databinding.StockEditImageItemBinding

class StockEditImageAdapter(
    private val images: List<Int>,
) : RecyclerView.Adapter<StockEditImageAdapter.ViewHolder>() {

    class ViewHolder(private val binding: StockEditImageItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(imageRes: Int) {
            binding.ivStockEditImage.setImageResource(imageRes)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            StockEditImageItemBinding.inflate(
                LayoutInflater.from(viewGroup.context),
                viewGroup,
                false
            )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bind(images[position])
    }

    override fun getItemCount() = images.size

}