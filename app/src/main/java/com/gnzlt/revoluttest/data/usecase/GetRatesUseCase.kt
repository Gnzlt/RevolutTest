package com.gnzlt.revoluttest.data.usecase

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.double
import java.net.URL
import javax.inject.Inject

class GetRatesUseCase
@Inject constructor(
    private val json: Json
) {

    companion object {
        private const val RATES_URL = "https://revolut.duckdns.org/latest?base="
        private const val RATES_FIELD_KEY = "rates"
        private const val REQUEST_TIMEOUT = 1000L
    }

    suspend fun get(currencySymbol: String): Map<String, Double> =
        withTimeout(REQUEST_TIMEOUT) {
            withContext(Dispatchers.IO) {
                val response = URL(RATES_URL + currencySymbol).readText()
                val ratesJsonObject = json.parseJson(response).jsonObject.getObject(RATES_FIELD_KEY)
                ratesJsonObject.mapValues { it.value.double }
            }
        }
}
