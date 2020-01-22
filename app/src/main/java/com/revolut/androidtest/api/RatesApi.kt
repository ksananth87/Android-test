package com.revolut.androidtest.api

import com.google.gson.JsonObject
import io.reactivex.Single
import retrofit2.http.GET

interface RatesApi {
    @GET("/latest?base=EUR")
    fun getRates(): Single<JsonObject>
}