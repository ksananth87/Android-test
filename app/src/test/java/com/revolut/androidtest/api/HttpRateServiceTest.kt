package com.revolut.androidtest.api

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.nhaarman.mockitokotlin2.verify
import com.revolut.androidtest.domain.Rates
import io.reactivex.Single
import io.reactivex.observers.TestObserver
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class HttpRateServiceTest{

    @Mock
    private lateinit var api: RatesApi

    @Test
    fun `should call api to retrieve rates`() {
        Mockito.`when`(api.getRates()).thenReturn(Single.just(JsonObject()))
        val service = HttpRateService(api)

        service.getRates()

        verify(api).getRates()
    }

    @Test
    fun `should return rates Model after parsing the response`() {
        Mockito.`when`(api.getRates()).thenReturn(Single.just(dummyRatesResponse()))
        val service = HttpRateService(api)

        val result: TestObserver<Rates> = service.getRates().test()

        assertThat(result.values().first(), `is`(CoreMatchers.instanceOf(Rates::class.java)))
    }

    private fun dummyRatesResponse() = JsonObject()
        .apply {
            addProperty("base", "base")
            addProperty("date", "2018-09-06")

            add("rates", JsonArray()
                .apply {
                    addProperty("AUD", 1.6173)
                    addProperty("BGN", 1.9569)
                    addProperty("INR", 83.763)
                    addProperty("KRW", "1305.5")
                })
        }
}