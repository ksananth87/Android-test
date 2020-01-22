package com.revolut.androidtest.view.viewmodel

import com.nhaarman.mockitokotlin2.verify
import com.revolut.androidtest.domain.RateRepository
import org.junit.Before
import org.junit.Test
import org.mockito.Mock


class CountryRatesViewModelTest {
    @Mock
    private lateinit var service: RateRepository

    private lateinit var viewModel: CountryRatesViewModel

    @Before
    fun setUp() {
        viewModel = CountryRatesViewModel(service)
    }

    @Test
    fun `should call getRates service on fragment loaded`() {
        viewModel.fetchRates()

        verify(service).getRates()
    }
}