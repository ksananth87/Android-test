package com.revolut.androidtest.api

import com.google.gson.JsonObject
import com.nhaarman.mockitokotlin2.verify
import com.revolut.androidtest.domain.Rates
import io.reactivex.Single
import io.reactivex.observers.TestObserver
import org.hamcrest.CoreMatchers.hasItem
import org.hamcrest.CoreMatchers.instanceOf
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

        assertThat(result.values().first(), `is`(instanceOf(Rates::class.java)))
    }

    @Test
    fun `should parse json and return Rates Model`() {
        Mockito.`when`(api.getRates()).thenReturn(Single.just(dummyRatesResponse()))
        val service = HttpRateService(api)

        val result: TestObserver<Rates> = service.getRates().test()
        val ratesObj = result.values().first()

        assertThat(ratesObj.base, `is`("base"))
        assertThat(ratesObj.date, `is`("2018-09-06"))
        assertThat(ratesObj.countryList.size, `is`(4))
    }

    @Test
    fun `should throw exception when api returns error`() {
        Mockito.`when`(api.getRates()).thenReturn(Single.error(RuntimeException()))
        val service = HttpRateService(api)

        val result: TestObserver<Rates> = service.getRates().test()

        assertThat(result.errors(), hasItem(instanceOf(RuntimeException::class.java)))
    }

    private fun dummyRatesResponse() = JsonObject()
        .apply {
            addProperty("base", "base")
            addProperty("date", "2018-09-06")

            add("rates", JsonObject().apply {
                        addProperty("AUD",1.6173)
                        addProperty("BGN", 1.9569)
                        addProperty("INR", 83.763)
                        addProperty("KRW", 1305.5)
                    })
        }
}