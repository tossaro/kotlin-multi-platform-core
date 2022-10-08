package tossaro.android.core.example.app.stocklist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import tossaro.android.core.example.databinding.StockItemBinding
import tossaro.android.core.example.domain.stock.entity.Stock

class StockAdapter :
    RecyclerView.Adapter<StockAdapter.ViewHolder>() {
    var onClick: ((String, String) -> Unit)? = null
    var stocks = mutableListOf<Stock>()

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
        viewHolder.bind(stocks[position], onClick)
    }

    override fun getItemCount() = stocks.size
}