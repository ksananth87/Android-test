package com.revolut.androidtest.view.utils

import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class CurrencyConverterTest
{
    @Test
    fun `should get converted rate from INR to USD`() {
        val enteredINRValue = 100f
        val INR = 83.679f
        val USD = 1.1629f

        val equivalentUSD =  CurrencyConverter(enteredINRValue, INR).convertTo(USD)

        assertEquals(1.39f, equivalentUSD)
    }
}