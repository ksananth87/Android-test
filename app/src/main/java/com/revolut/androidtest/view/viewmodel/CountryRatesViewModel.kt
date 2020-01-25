package com.revolut.androidtest.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.revolut.androidtest.domain.RateRepository
import com.revolut.androidtest.domain.model.Currency
import com.revolut.androidtest.domain.model.Rates
import com.revolut.androidtest.view.model.CurrencyList
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
    private var enteredRate: Float? = null

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
        this.enteredAmount = enteredAmount
        this.enteredCode = enteredCode
        this.enteredRate = enteredRate
        val updatedList = CurrencyList(currentCurrencyList as ArrayList<Currency>)
            .enteredAmount(enteredAmount)
            .enteredCode(enteredCode)
            .enteredRate(enteredRate)
            .getUpdatedList()
        refreshedCurrencyListLiveData.postValue(CurrencyList(updatedList.currencyList))
        this.rates = updatedList
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

        return CurrencyList(existingRatesInScreen as ArrayList<Currency>)
            .enteredAmount(enteredAmount)
            .enteredCode(enteredCode)
            .enteredRate(enteredRate)
            .refreshRatesWith(newRates)
    }

    private fun handleResponse(rates: CurrencyList) {
        dismissFetchingDialog()
        showCurrencyRates(rates)
        this.rates = rates
    }

    private fun handleRefreshRates(rates: CurrencyList) {
        refreshCurrencyRates(rates)
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

    private fun showCurrencyRates(rates: CurrencyList) {
        currencyListLiveData.value = rates
    }

    private fun refreshCurrencyRates(rates: CurrencyList) {
        refreshedCurrencyListLiveData.value = rates
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}