package com.revolut.androidtest.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.nhaarman.mockitokotlin2.verify
import com.revolut.androidtest.RxTrampolineSchedulerRule
import com.revolut.androidtest.api.exception.InvalidResponseException
import com.revolut.androidtest.domain.RateRepository
import com.revolut.androidtest.domain.model.Currency
import com.revolut.androidtest.domain.model.Rates
import io.reactivex.Observable
import io.reactivex.Single
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InOrder
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.inOrder
import org.mockito.Spy
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

    @Spy
    private lateinit var progressObserver: Observer<Boolean>

    private lateinit var viewModel: CountryRatesViewModel

    @Before
    fun setUp() {
        viewModel = CountryRatesViewModel(service)
        `when`(service.getRates()).thenReturn(Single.just(aDummyRates(base = "EUR")))
    }

    @Test
    fun `should call getRates service on fragment loaded`() {
        viewModel.fragmentLoaded()

        verify(service).getRates()
    }

    @Test
    fun `should show and dismiss progress dialog when response received from service`() {
        viewModel.showProgressDialog().observeForever(progressObserver)

        viewModel.fragmentLoaded()

        val inOrder: InOrder = inOrder(progressObserver)
        inOrder.verify(progressObserver).onChanged(true)
        inOrder.verify(progressObserver).onChanged(false)
    }

    @Test
    fun `should show and dismiss progress dialog when service failed`() {
        `when`(service.getRates()).thenReturn(Single.error(RuntimeException()))
        viewModel.showProgressDialog().observeForever(progressObserver)

        viewModel.fragmentLoaded()

        val inOrder = inOrder(progressObserver)
        inOrder.verify(progressObserver).onChanged(true)
        inOrder.verify(progressObserver).onChanged(false)
    }

    @Test
    fun `should update list when received response from server`() {
        viewModel.fragmentLoaded()

        Assert.assertNotNull(viewModel.getRates().value)
        Assert.assertEquals(viewModel.getRates().value?.currencyList?.size, 3)
    }

    @Test
    fun `should show error when service failed`() {
        `when`(service.getRates()).thenReturn(Single.error(InvalidResponseException()))

        viewModel.fragmentLoaded()

        Assert.assertEquals(viewModel.getError().value, true)
    }

    @Test
    fun `should return as-is currency list from repository`() {
        `when`(service.getRates()).thenReturn(Single.just(aDummyRates(base = "YEN")))

        viewModel.fragmentLoaded()

        Assert.assertEquals(viewModel.getRates().value?.currencyList?.size, 3)
        Assert.assertEquals(viewModel.getRates().value?.currencyList?.get(0)?.code, "INR")
        Assert.assertEquals(viewModel.getRates().value?.currencyList?.get(1)?.code, "USA")
        Assert.assertEquals(viewModel.getRates().value?.currencyList?.get(2)?.code, "EUR")
    }

    @Test
    fun `should update all currencies with equivalent conversion rate when when rate is changed`() {
        //Act
        viewModel.currencyValueUpdated("INR", 78.64f, 100f, aDummyRates("EUR").countryList)

        //Assert
        Assert.assertEquals(viewModel.getRefreshedRates().value?.currencyList?.size, 3)
        Assert.assertEquals(100f, viewModel.getRefreshedRates().value?.currencyList?.get(0)?.rate)
        Assert.assertEquals(1.4f, viewModel.getRefreshedRates().value?.currencyList?.get(1)?.rate)
        Assert.assertEquals(1.27f, viewModel.getRefreshedRates().value?.currencyList?.get(2)?.rate)
    }

    @Test
    fun `should refresh existing rate with new rates`() {
        `when`(service.getRates()).thenReturn(Single.just(aDummyRefreshedRates(base = "EUR")))

        viewModel.refreshCurrencyRates(aDummyRates("EUR").countryList)

        Assert.assertEquals(viewModel.getRefreshedRates().value?.currencyList?.size, 3)
        Assert.assertEquals(78f, viewModel.getRefreshedRates().value?.currencyList?.get(0)?.rate)
        Assert.assertEquals(1.15f, viewModel.getRefreshedRates().value?.currencyList?.get(1)?.rate)
        Assert.assertEquals(1.2f, viewModel.getRefreshedRates().value?.currencyList?.get(2)?.rate)
    }

    @Test
    fun `should refresh currency rates after a currency rate updated`() {
        `when`(service.getRates()).thenReturn(Single.just(aDummyRefreshedRates(base = "EUR")))
        viewModel.currencyValueUpdated("INR", 78.64f, 100f, aDummyRates("EUR").countryList)

        viewModel.refreshCurrencyRates(aDummyRates("EUR").countryList)

        Assert.assertEquals(viewModel.getRefreshedRates().value?.currencyList?.size, 3)
        Assert.assertEquals(100f, viewModel.getRefreshedRates().value?.currencyList?.get(0)?.rate)
        Assert.assertEquals(1.47f, viewModel.getRefreshedRates().value?.currencyList?.get(1)?.rate)
        Assert.assertEquals(1.54f, viewModel.getRefreshedRates().value?.currencyList?.get(2)?.rate)
    }

    @Test
    fun `should clear all service when viewModel cleared`() {
        viewModel.compositeDisposable.add(Observable.just("").subscribe())
        viewModel.compositeDisposable.add(Observable.just("").subscribe())
        Assert.assertEquals(2, viewModel.compositeDisposable.size())

        viewModel.onCleared()

        Assert.assertEquals(0, viewModel.compositeDisposable.size())
    }


    private fun aDummyRates(base: String): Rates {
        val countryRates = ArrayList<Currency>()
        countryRates.add(Currency("INR", 78.64f))
        countryRates.add(Currency("USA", 1.10f))
        countryRates.add(Currency("EUR", 1f))
        return Rates(countryRates, base, "2020-11-01")
    }

    private fun aDummyRefreshedRates(base: String): Rates {
        val countryRates = ArrayList<Currency>()
        countryRates.add(Currency("INR", 78f))
        countryRates.add(Currency("USA", 1.15f))
        countryRates.add(Currency("EUR", 1.2f))
        return Rates(countryRates, base, "2020-11-01")
    }
}