package com.revolut.androidtest.api

import com.google.gson.JsonObject
import com.nhaarman.mockitokotlin2.verify
import io.reactivex.Single
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
}