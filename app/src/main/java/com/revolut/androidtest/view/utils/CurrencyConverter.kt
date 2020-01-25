package com.revolut.androidtest.view.utils

class CurrencyConverter(private val enteredValue: Float, private val fromCurrencyRate: Float) {
    fun convertTo(toCurrencyRate: Float): Float {
        val updatedAmount = (toCurrencyRate * enteredValue) / fromCurrencyRate
        return "%.2f".format(updatedAmount).toFloat()
    }
}