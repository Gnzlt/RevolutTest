package com.gnzlt.revoluttest.rates

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gnzlt.revoluttest.data.model.RateItem
import com.gnzlt.revoluttest.databinding.ItemRateBinding

class RatesAdapter(
    private val editListener: (item: RateItem) -> Unit,
    private val clickListener: (item: RateItem) -> Unit
) : ListAdapter<RateItem, RatesViewHolder>(RateItem.DiffCallBack()) {

    companion object {
        private const val TYPE_BASE = 0
        private const val TYPE_RATE = 1
    }

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RatesViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemRateBinding.inflate(layoutInflater, parent, false)
        val isBaseItem = viewType == TYPE_BASE

        return RatesViewHolder(binding, isBaseItem).apply {
            if (isBaseItem) {
                binding.editRate.doOnTextChanged { text, _, _, _ ->
                    val rateItem = getItem(adapterPosition)
                    rateItem.value = rateItem.getValueFromString(text.toString())
                    editListener(rateItem)
                }
            } else {
                binding.root.setOnClickListener {
                    if (adapterPosition != RecyclerView.NO_POSITION && adapterPosition < itemCount) {
                        clickListener(getItem(adapterPosition))
                    }
                }
                binding.editRate.setOnClickListener {
                    if (adapterPosition != RecyclerView.NO_POSITION && adapterPosition < itemCount) {
                        clickListener(getItem(adapterPosition))
                    }
                }
            }
        }
    }

    override fun onBindViewHolder(holder: RatesViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun getItemViewType(position: Int): Int =
        if (position == itemCount - 1) TYPE_BASE else TYPE_RATE

    override fun getItemId(position: Int): Long =
        getItem(position).symbol.hashCode().toLong()
}
