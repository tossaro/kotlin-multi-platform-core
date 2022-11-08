package multi.platform.core.example.app.stocklist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import multi.platform.core.example.databinding.StockItemBinding
import multi.platform.core.example.domain.stock.entity.Stock

class StockAdapter :
    RecyclerView.Adapter<StockAdapter.ViewHolder>() {
    var onClick: ((String, String) -> Unit)? = null
    var items = mutableListOf<Stock>()

    class ViewHolder(private val binding: StockItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(stock: Stock, onClick: ((String, String) -> Unit)?) {
            binding.tvName.text = stock.name
            binding.tvPrice.text = stock.price
            binding.tvFullname.text = stock.fullname
            binding.tvStatus.text = stock.status
            binding.clStockRow.setOnClickListener {
                onClick?.invoke(
                    stock.name.toString(),
                    stock.price.toString()
                )
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            StockItemBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bind(items[position], onClick)
    }

    override fun getItemCount() = items.size

    fun clear() {
        val size = items.size
        items = mutableListOf()
        notifyItemRangeRemoved(0, size)
    }
}