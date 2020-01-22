package com.revolut.androidtest.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.revolut.androidtest.domain.RateRepository
import com.revolut.androidtest.domain.Rates
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class CountryRatesViewModel(private val rateRepository: RateRepository) : ViewModel() {

    private val disposable = CompositeDisposable()
    private var progressLiveData: MutableLiveData<Boolean> = MutableLiveData()
    private var rateListLiveData: MutableLiveData<Rates> = MutableLiveData()
    private var errorLiveData: MutableLiveData<Boolean> = MutableLiveData()

    fun fetchRates() {
        progressLiveData.value = true
        disposable.add(
            rateRepository.getRates()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleResponse, this::handleError)
        )
    }

    private fun handleResponse(rates: Rates) {
        progressLiveData.value = false
        rateListLiveData.value = rates
    }

    private fun handleError(throwable: Throwable) {
        progressLiveData.value = false
        errorLiveData.value = true
    }

    fun showProgressDialog(): LiveData<Boolean> {
        return progressLiveData
    }

    fun getRates(): LiveData<Rates> {
        return rateListLiveData
    }

    fun getError(): LiveData<Boolean> {
        return errorLiveData
    }
}