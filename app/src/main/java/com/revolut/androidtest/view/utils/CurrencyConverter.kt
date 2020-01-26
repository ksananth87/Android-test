package com.revolut.androidtest.view.utils

import java.math.BigDecimal

class CurrencyConverter(private val enteredValue: Float, private val fromCurrencyRate: Float?) {
    fun convertTo(toCurrencyRate: Float): Float {
        if(fromCurrencyRate == null){
            return toCurrencyRate
        }
        val updatedAmount = (toCurrencyRate * enteredValue) / fromCurrencyRate
        return round(updatedAmount,2)
    }

    private fun round(d: Float, decimalPlace: Int): Float {
        var bd = BigDecimal(d.toString())
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP)
        return bd.toFloat()
    }
}