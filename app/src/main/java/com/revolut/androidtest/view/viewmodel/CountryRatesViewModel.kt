package com.revolut.androidtest.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.revolut.androidtest.domain.RateRepository
import com.revolut.androidtest.domain.model.Currency
import com.revolut.androidtest.domain.model.Rates
import com.revolut.androidtest.view.model.CurrencyList
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList


class CountryRatesViewModel(private val rateRepository: RateRepository) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()
    private var disposable: Disposable? = null

    private var progressLiveData: MutableLiveData<Boolean> = MutableLiveData()
    private var currencyListLiveData: MutableLiveData<CurrencyList> = MutableLiveData()
    private var errorLiveData: MutableLiveData<Boolean> = MutableLiveData()
    private var moveCurrencyIndexLiveData: MutableLiveData<Int> = MutableLiveData()
    private lateinit var rates: CurrencyList

    fun fragmentLoaded() {
        progressLiveData.value = true
        fetchRates()
    }

    fun refreshRatesEveryOneSec() {
        disposable = Observable.interval(1, TimeUnit.SECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::callEndPoint) { this.handleBackgroundError(it) }
    }

    fun currencyClicked(clickedCurrencyIndex: Int) {
        moveClickedCurrencyToTop(clickedCurrencyIndex)
        swapCurrency(clickedCurrencyIndex)
    }

    fun getCachedRates(): List<Currency> {
        return rates.currencyList
    }

    private fun moveClickedCurrencyToTop(clickedCurrencyIndex: Int) {
        moveCurrencyIndexLiveData.value = clickedCurrencyIndex
    }

    private fun swapCurrency(clickedIndex: Int) {
        val currencyList: ArrayList<Currency> = rates.currencyList
        Collections.swap(
            currencyList,
            clickedIndex,
            ZERO
        )
        rates.currencyList = currencyList
        //currencyListLiveData.postValue(rates)
    }

    private fun fetchRates() {
        compositeDisposable.add(
            rateRepository.getRates()
                .map { moveBaseCurrencyToFirst(it) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleResponse, this::handleError)
        )
    }

    private fun moveBaseCurrencyToFirst(rates: Rates): CurrencyList {
        val currencyList: ArrayList<Currency> = rates.countryList
        val baseCurrencyIndex: Int = currencyList.indexOfFirst { it.code == rates.base }
        if(baseCurrencyIndex != -1) {
            val baseCurrency: Currency = currencyList[baseCurrencyIndex]
            val sortedList: ArrayList<Currency> = currencyList.apply {
                removeAt(baseCurrencyIndex)
                add(ZERO, baseCurrency)
            }
            return CurrencyList(sortedList)
        }
        return CurrencyList(currencyList)
    }

    private fun callEndPoint(aLong: Long) = fetchRates()

    private fun handleResponse(rates: CurrencyList) {
        progressLiveData.value = false
        currencyListLiveData.value = rates
        this.rates = rates
    }

    private fun handleError(throwable: Throwable) {
        progressLiveData.value = false
        errorLiveData.value = true
    }

    private fun handleBackgroundError(throwable: Throwable) = Unit //Do nothing

    fun showProgressDialog(): LiveData<Boolean> = progressLiveData

    fun getRates(): LiveData<CurrencyList> = currencyListLiveData

    fun getError(): LiveData<Boolean> = errorLiveData

    fun moveCurrencyToTop(): LiveData<Int> = moveCurrencyIndexLiveData

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
        disposable?.dispose()
    }

    fun currencyChanged(index: Int, code: String, enteredAmount: String) {
        val updatedCurrencyList = ArrayList<Currency>()
        for (currency: Currency in rates.currencyList) {
            if (currency.code == code) {
                updatedCurrencyList.add(
                    Currency(
                        currency.code,
                        enteredAmount.toFloat()
                    )
                )
            } else {
                updatedCurrencyList.add(
                    Currency(
                        currency.code,
                        enteredAmount.toFloat() * currency.rate
                    )
                )
            }
        }
        rates.currencyList = updatedCurrencyList
        currencyListLiveData.value = rates
    }


    companion object{
        private const val ZERO = 0
    }
}