package com.revolut.androidtest.view.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.revolut.androidtest.domain.RateRepository

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(private val rateRepository: RateRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when (modelClass) {
            CountryRatesViewModel::class.java -> CountryRatesViewModel(rateRepository) as T
            else -> throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}