package com.revolut.androidtest.api

import com.google.gson.JsonObject
import com.revolut.androidtest.domain.RateRepository
import com.revolut.androidtest.domain.Rates
import io.reactivex.Single

class HttpRateService(private val api: RatesApi): RateRepository {
    override fun getRates(): Single<Rates> {
        return api.getRates().map { parseResponse(it) }
    }

    private fun parseResponse(response: JsonObject): Rates {
        return Rates(ArrayList(), "", "")
    }
}