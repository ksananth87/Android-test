package com.revolut.androidtest.view.utils

class CurrencyConverter(private val enteredValue: Float, private val fromCurrencyRate: Float?) {
    fun convertTo(toCurrencyRate: Float): Float {
        if(fromCurrencyRate == null){
            return toCurrencyRate
        }
        val updatedAmount = (toCurrencyRate * enteredValue) / fromCurrencyRate
        return "%.2f".format(updatedAmount).toFloat()
    }
}