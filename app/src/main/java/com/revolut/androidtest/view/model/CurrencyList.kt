package com.revolut.androidtest.view.model

import com.revolut.androidtest.domain.model.Currency
import com.revolut.androidtest.domain.model.Rates
import com.revolut.androidtest.view.utils.CurrencyConverter

data class CurrencyList(var currencyList: ArrayList<Currency>) {
    private var enteredCode = ""
    private var enteredAmount: Float = 0f
    private var enteredRate: Float? = null

    fun enteredAmount(enteredAmount: Float): CurrencyList {
        this.enteredAmount = enteredAmount
        return this
    }

    fun enteredCode(enteredCode: String): CurrencyList {
        this.enteredCode = enteredCode
        return this
    }

    fun enteredRate(enteredRate: Float?): CurrencyList {
        this.enteredRate = enteredRate
        return this
    }

    fun getUpdatedList(): CurrencyList {
        val updatedCurrencyList = ArrayList<Currency>()
        for (currency: Currency in currencyList) {
            val updatedRates = Currency(
                currency.code,
                CurrencyConverter(enteredAmount, enteredRate).convertTo(currency.rate)
            )
            updatedCurrencyList.add(updatedRates)
        }
        return CurrencyList(updatedCurrencyList)
    }

    fun refreshRatesWith(newRates: Rates): CurrencyList {
        val updatedCurrencyList = ArrayList<Currency>()
        for (oldRates: Currency in currencyList) {
            for (newRate: Currency in newRates.countryList) {
                if (oldRates.code == newRate.code) {
                    if (enteredCode == newRate.code) {
                        enteredRate = newRate.rate
                    }
                    val updatedRates = Currency(
                        newRate.code,
                        CurrencyConverter(enteredAmount, enteredRate).convertTo(newRate.rate)
                    )
                    updatedCurrencyList.add(updatedRates)
                }
            }
        }
        return CurrencyList(updatedCurrencyList)
    }
}