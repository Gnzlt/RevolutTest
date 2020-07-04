package com.gnzlt.revoluttest.rates

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.gnzlt.revoluttest.data.model.RateItem
import com.gnzlt.revoluttest.data.usecase.GetRatesUseCase
import com.gnzlt.revoluttest.util.moveToBottom
import com.gnzlt.revoluttest.util.notifyObserver
import kotlinx.coroutines.launch
import javax.inject.Inject

class RatesViewModel
@Inject constructor(
    private val getRatesUseCase: GetRatesUseCase
) : ViewModel() {

    companion object {
        private const val DEFAULT_SYMBOL = "EUR"
        private const val DEFAULT_VALUE = 100.0
    }

    private var baseValue: Double = DEFAULT_VALUE
    private var baseSymbol: String = DEFAULT_SYMBOL

    private val rates: MutableLiveData<LinkedHashMap<String, Double>> = MutableLiveData(LinkedHashMap())
    private val error: MutableLiveData<Boolean> = MutableLiveData(false)

    fun onError(): LiveData<Boolean> = error

    fun onRates(): LiveData<List<RateItem>> =
        rates.map { items ->
            items.map { item ->
                if (item.key == baseSymbol) {
                    RateItem(item.key, baseValue)
                } else {
                    RateItem(item.key, item.value * baseValue)
                }
            }
        }

    fun updateRates() {
        viewModelScope.launch {
            try {
                val result = getRatesUseCase.get(baseSymbol)
                rates.value!!.putAll(result)
                rates.value!!.put(baseSymbol, baseValue)
                rates.notifyObserver()

                error.value = false
            } catch (exception: Exception) {
                error.value = true
            }
        }
    }

    fun setBaseRate(item: RateItem) {
        baseSymbol = item.symbol
        baseValue = item.value

        rates.value?.moveToBottom(item.symbol)
    }

    fun setBaseValue(value: Double) {
        if (value < 0) return

        baseValue = value
    }
}
