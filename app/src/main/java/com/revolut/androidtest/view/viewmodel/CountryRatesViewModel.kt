package com.revolut.androidtest.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.revolut.androidtest.domain.RateRepository
import com.revolut.androidtest.domain.model.Currency
import com.revolut.androidtest.domain.model.Rates
import com.revolut.androidtest.view.model.CurrencyList
import com.revolut.androidtest.view.utils.CurrencyConverter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers


class CountryRatesViewModel(private val rateRepository: RateRepository) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    private var progressLiveData: MutableLiveData<Boolean> = MutableLiveData()
    private var currencyListLiveData: MutableLiveData<CurrencyList> = MutableLiveData()
    private var refreshedCurrencyListLiveData: MutableLiveData<CurrencyList> = MutableLiveData()
    private var errorLiveData: MutableLiveData<Boolean> = MutableLiveData()

    private lateinit var rates: CurrencyList
    private var enteredCode = ""
    private var enteredAmount: Float = 0f
    private var enteredRate: Float = 0f

    fun fragmentLoaded() {
        showFetchingDialog()
        fetchRates()
    }

    fun refreshCurrencyRates(
        existingList: MutableList<Currency>
    ) = fetchRates(existingList)

    fun currencyValueUpdated(
        enteredCode: String,
        enteredRate: Float,
        enteredAmount: Float,
        currentCurrencyList: MutableList<Currency>
    ) {
        //Log.e("currencyValueUpdated", "currencyValueUpdated enteredCode--$enteredCode")
        //Log.e("currencyValueUpdated", "currencyValueUpdated enteredAmount--$enteredAmount")
        this.enteredAmount = enteredAmount
        this.enteredCode = enteredCode
        this.enteredRate = enteredRate
        val updatedCurrencyList = ArrayList<Currency>()
        for (currency: Currency in currentCurrencyList) {
            val updatedRates = Currency(currency.code, CurrencyConverter(enteredAmount, enteredRate).convertTo(currency.rate))
            updatedCurrencyList.add(updatedRates)
        }
        refreshedCurrencyListLiveData.postValue(CurrencyList(updatedCurrencyList))
        this.rates = CurrencyList(updatedCurrencyList)
    }


    private fun fetchRates() {
        compositeDisposable.add(
            rateRepository.getRates()
                .map { getCurrencyRates(it) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleResponse, this::handleError)
        )
    }

    private fun fetchRates(existingList: MutableList<Currency>) {
        compositeDisposable.add(
            rateRepository.getRates()
                .map { refreshList(it, existingList) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleRefreshRates, this::handleError)
        )
    }

    private fun getCurrencyRates(rates: Rates): CurrencyList {
        return CurrencyList(rates.countryList)
    }

    private fun refreshList(
        newRates: Rates,
        existingRatesInScreen: MutableList<Currency>
    ): CurrencyList {
        val updatedCurrencyList = ArrayList<Currency>()
        for (oldRates: Currency in existingRatesInScreen) {
            for (newRates: Currency in newRates.countryList) {
                if (oldRates.code == newRates.code) {
                    if(enteredCode.isEmpty()) {
                        updatedCurrencyList.add(Currency(newRates.code, newRates.rate))
                    } else{
                        if(enteredCode == newRates.code) enteredRate = newRates.rate
                        val updatedRates = Currency(newRates.code, CurrencyConverter(enteredAmount, enteredRate).convertTo(newRates.rate))
                        updatedCurrencyList.add(updatedRates)
                    }
                }
            }
        }
        return CurrencyList(updatedCurrencyList)
    }

    private fun handleResponse(rates: CurrencyList) {
        progressLiveData.value = false
        currencyListLiveData.value = rates
        this.rates = rates
    }

    private fun handleRefreshRates(rates: CurrencyList) {
        refreshedCurrencyListLiveData.value = rates
        this.rates = rates
    }

    private fun handleError(throwable: Throwable) {
        dismissFetchingDialog()
        showErrorScreen()
    }

    fun showProgressDialog(): LiveData<Boolean> = progressLiveData

    fun getRates(): LiveData<CurrencyList> = currencyListLiveData

    fun getRefreshedRates(): LiveData<CurrencyList> = refreshedCurrencyListLiveData

    fun getError(): LiveData<Boolean> = errorLiveData


    private fun showFetchingDialog() {
        progressLiveData.value = true
    }

    private fun dismissFetchingDialog() {
        progressLiveData.value = false
    }

    private fun showErrorScreen() {
        errorLiveData.value = true
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}