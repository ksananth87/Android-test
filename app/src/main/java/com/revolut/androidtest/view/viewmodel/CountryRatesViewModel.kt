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


class CountryRatesViewModel(private val rateRepository: RateRepository) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()
    private var disposable: Disposable? = null

    private var progressLiveData: MutableLiveData<Boolean> = MutableLiveData()
    private var rateListLiveData: MutableLiveData<CurrencyList> = MutableLiveData()
    private var errorLiveData: MutableLiveData<Boolean> = MutableLiveData()

    fun fragmentLoaded() {
        progressLiveData.value = true
        fetchRates()
    }

    fun refreshRatesEveryOneSec() {
        disposable = Observable.interval(1, TimeUnit.SECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::callEndPoint, this::handleBackgroundError)
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
        Collections.swap(
            currencyList,
            currencyList.indexOfFirst { it.code == rates.base },
            ZERO
        )
        return CurrencyList(currencyList)
    }

    private fun callEndPoint(aLong: Long) {
        fetchRates()
    }

    private fun handleResponse(rates: CurrencyList) {
        progressLiveData.value = false
        rateListLiveData.value = rates
    }

    private fun handleError(throwable: Throwable) {
        progressLiveData.value = false
        errorLiveData.value = true
    }

    private fun handleBackgroundError(throwable: Throwable) {
        //Do nothing
    }

    fun showProgressDialog(): LiveData<Boolean> {
        return progressLiveData
    }

    fun getRates(): LiveData<CurrencyList> {
        return rateListLiveData
    }

    fun getError(): LiveData<Boolean> {
        return errorLiveData
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
        disposable?.dispose()
    }

    companion object{
        private const val ZERO = 0
    }
}