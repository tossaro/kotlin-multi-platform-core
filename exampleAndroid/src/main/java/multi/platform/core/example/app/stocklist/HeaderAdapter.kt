package multi.platform.core.example.app.stocklist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import multi.platform.core.example.databinding.HomeHeaderViewBinding

class HeaderAdapter : RecyclerView.Adapter<HeaderAdapter.ViewHolder>() {

    class ViewHolder(binding: HomeHeaderViewBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            HomeHeaderViewBinding.inflate(
                LayoutInflater.from(viewGroup.context),
                viewGroup,
                false
            )
        return ViewHolder(binding)
    }

    @Suppress("kotlin:S1186")
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
    }

    override fun getItemCount() = 1

}