package com.gnzlt.revoluttest.data.model

import android.content.Context
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.DiffUtil
import java.text.NumberFormat
import java.text.ParseException
import java.util.Currency
import java.util.Locale

@androidx.annotation.Keep
data class RateItem(
    val symbol: String,
    var value: Double = 0.0
) {

    private val currencyName: String
    private val numberFormat: NumberFormat

    init {
        val currency = Currency.getInstance(symbol)
        currencyName = currency.displayName
        numberFormat = NumberFormat.getInstance().apply { this.currency = currency }
    }

    fun getCurrencyName(): String =
        currencyName

    fun getFormattedValue(): String =
        numberFormat.format(value)

    fun getValueFromString(text: String): Double =
        try {
            numberFormat.parse(text).toDouble()
        } catch (ignored: ParseException) {
            0.0
        }

    @DrawableRes
    fun getFlagResourceId(context: Context): Int =
        context.resources.getIdentifier(
            symbol.toLowerCase(Locale.US) + "_flag",
            "drawable",
            context.packageName
        )

    class DiffCallBack : DiffUtil.ItemCallback<RateItem>() {
        override fun areItemsTheSame(oldItem: RateItem, newItem: RateItem): Boolean =
            oldItem.symbol == newItem.symbol

        override fun areContentsTheSame(oldItem: RateItem, newItem: RateItem): Boolean =
            oldItem.value == newItem.value
    }
}
