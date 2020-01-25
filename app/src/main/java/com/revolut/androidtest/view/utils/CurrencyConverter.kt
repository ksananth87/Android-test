package com.revolut.androidtest.view.utils

class CurrencyConverter(private val enteredValue: Float, private val fromRate: Float) {
    fun convertTo(toRate: Float): Float {
        val updatedAmount = (toRate * enteredValue) / fromRate
        return "%.2f".format(updatedAmount).toFloat()
    }
}