package com.revolut.androidtest.view.viewmodel

import androidx.lifecycle.ViewModel
import com.revolut.androidtest.domain.RateRepository
import com.revolut.androidtest.domain.Rates
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class CountryRatesViewModel(private val rateRepository: RateRepository) : ViewModel() {

    private val disposable = CompositeDisposable()

    fun fetchRates() {
        disposable.add(
            rateRepository.getRates()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleResponse, this::handleError)
        )
    }

    private fun handleResponse(rates: Rates) {

    }

    private fun handleError(error: Throwable) {

    }
}