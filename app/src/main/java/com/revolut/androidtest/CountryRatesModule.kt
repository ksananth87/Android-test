package com.revolut.androidtest

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.revolut.androidtest.api.HttpRateService
import com.revolut.androidtest.api.RatesApi
import com.revolut.androidtest.view.viewmodel.ViewModelFactory
import retrofit2.Retrofit

class CountryRatesModule private constructor(){
    companion object {
        private lateinit var factory: ViewModelFactory

        fun create(retrofit: Retrofit): CountryRatesModule {
            val service = HttpRateService(retrofit.create(RatesApi::class.java))
            factory = ViewModelFactory(service)
            return CountryRatesModule()
        }
    }

    fun <T : ViewModel> getViewModelFor(fragment: Fragment, type: Class<T>): T {
        val provider: ViewModelProvider = ViewModelProviders.of(fragment, factory)
        return provider.get(type)
    }
}