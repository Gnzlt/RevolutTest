package com.gnzlt.revoluttest.rates

import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import com.gnzlt.revoluttest.data.model.RateItem
import com.gnzlt.revoluttest.databinding.ItemRateBinding

class RatesViewHolder(
    private val binding: ItemRateBinding,
    isBaseItem: Boolean
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.editRate.isFocusableInTouchMode = isBaseItem
        binding.editRate.isFocusable = isBaseItem

        if (isBaseItem) {
            binding.editRate.setOnFocusChangeListener { view, hasFocus ->
                if (hasFocus) {
                    val editText = view as EditText
                    editText.setSelection(editText.text.length)
                }
            }
            binding.editRate.setOnClickListener { view ->
                val editText = view as EditText
                editText.setSelection(editText.text.length)
            }
            binding.root.setOnClickListener { binding.editRate.requestFocus() }
        }
    }

    fun bind(item: RateItem) {
        binding.textSymbol.text = item.symbol
        binding.textCurrency.text = item.getCurrencyName()
        binding.imageFlag.setImageResource(item.getFlagResourceId(itemView.context))
        binding.editRate.setText(item.getFormattedValue())
    }
}
