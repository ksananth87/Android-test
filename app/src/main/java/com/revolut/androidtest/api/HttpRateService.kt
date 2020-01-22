package com.revolut.androidtest.api

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.revolut.androidtest.api.exception.InvalidResponseException
import com.revolut.androidtest.domain.Country
import com.revolut.androidtest.domain.RateRepository
import com.revolut.androidtest.domain.Rates
import io.reactivex.Single


class HttpRateService(private val api: RatesApi): RateRepository {
    override fun getRates(): Single<Rates> {
        return api.getRates().map { parseResponse(it) }
    }

    private fun parseResponse(response: JsonObject): Rates {
        try {
            val base: String = response.asJsonObject["base"].asString
            val date: String = response.asJsonObject["date"].asString
            val rates: JsonElement = response.asJsonObject["rates"]
            val ratesJsonObject: JsonObject = rates.asJsonObject

            val countryRates = ArrayList<Country>()
            ratesJsonObject.keySet().forEach { countryCode ->
                run {
                    val countryRate = ratesJsonObject.get(countryCode)
                    countryRates.add(Country(countryCode, countryRate.asFloat))
                }
            }
            return Rates(countryRates, base, date)
        } catch (e: Exception) {
            throw InvalidResponseException()
        }
    }
}