package com.revolut.androidtest.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.verify
import com.revolut.androidtest.RxTrampolineSchedulerRule
import com.revolut.androidtest.domain.Country
import com.revolut.androidtest.domain.RateRepository
import com.revolut.androidtest.domain.Rates
import io.reactivex.Single
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class CountryRatesViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Rule
    @JvmField
    var testSchedulerRule = RxTrampolineSchedulerRule()


    @Mock
    private lateinit var service: RateRepository

    private lateinit var viewModel: CountryRatesViewModel

    @Before
    fun setUp() {
        viewModel = CountryRatesViewModel(service)
    }

    @Test
    fun `should call getRates service on fragment loaded`() {
        Mockito.`when`(service.getRates()).thenReturn(Single.just(aDummyRates()))
        viewModel.fetchRates()

        verify(service).getRates()
    }

    private fun aDummyRates(): Rates {
        val countryRates = ArrayList<Country>()
        countryRates.add(Country("INR", 85.33f))
        countryRates.add(Country("USA", 1f))
        return Rates(countryRates, "EUR", "2020-11-01")
    }
}