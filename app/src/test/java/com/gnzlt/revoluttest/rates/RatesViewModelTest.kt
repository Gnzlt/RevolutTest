package com.gnzlt.revoluttest.rates

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.gnzlt.revoluttest.MainCoroutineRule
import com.gnzlt.revoluttest.data.model.RateItem
import com.gnzlt.revoluttest.data.usecase.GetRatesUseCase
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class RatesViewModelTest {

    private val BASE_RATE = RateItem("AUD", 100.0)

    private val FAKE_RATES: Map<String, Double> = mapOf(
        "BGN" to 0.11,
        "BRL" to 2.23,
        "CAD" to 0.54,
        "CHF" to 0.3,
        "CNY" to 1.7
    )

    private val FAKE_RATEITEMS: List<RateItem> = listOf(
        RateItem("BGN", 0.11 * BASE_RATE.value),
        RateItem("BRL", 2.23 * BASE_RATE.value),
        RateItem("CAD", 0.54 * BASE_RATE.value),
        RateItem("CHF", 0.3 * BASE_RATE.value),
        RateItem("CNY", 1.7 * BASE_RATE.value),
        BASE_RATE
    )

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()
    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @Mock
    private lateinit var getRatesUseCase: GetRatesUseCase
    @InjectMocks
    private lateinit var ratesViewModel: RatesViewModel

    @Mock
    private lateinit var ratesObserver: Observer<List<RateItem>>

    @Mock
    private lateinit var errorObserver: Observer<Boolean>

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        ratesViewModel.setBaseRate(BASE_RATE)
        ratesViewModel.onRates().observeForever(ratesObserver)
        ratesViewModel.onError().observeForever(errorObserver)
    }

    @Test
    fun testRates() {
        mainCoroutineRule.runBlockingTest {
            whenever(getRatesUseCase.get(BASE_RATE.symbol)).thenReturn(FAKE_RATES)

            ratesViewModel.updateRates()

            verify(ratesObserver).onChanged(FAKE_RATEITEMS)
        }
    }
}