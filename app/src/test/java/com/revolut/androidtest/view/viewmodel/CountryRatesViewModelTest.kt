package com.revolut.androidtest.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.nhaarman.mockitokotlin2.verify
import com.revolut.androidtest.RxTrampolineSchedulerRule
import com.revolut.androidtest.api.exception.InvalidResponseException
import com.revolut.androidtest.domain.RateRepository
import com.revolut.androidtest.domain.model.Currency
import com.revolut.androidtest.domain.model.Rates
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
    fun `should move base currency to top of currency list`() {
        viewModel.fragmentLoaded()

        Assert.assertEquals(viewModel.getRates().value?.currencyList?.size, 3)
        Assert.assertEquals(viewModel.getRates().value?.currencyList?.get(0)?.code, "EUR")
        Assert.assertEquals(viewModel.getRates().value?.currencyList?.get(1)?.code, "INR")
        Assert.assertEquals(viewModel.getRates().value?.currencyList?.get(2)?.code, "USA")
    }

    @Test
    fun `should return as-is currency list when base currency is not in currency list`() {
        `when`(service.getRates()).thenReturn(Single.just(aDummyRates(base = "YEN")))

        viewModel.fragmentLoaded()

        Assert.assertEquals(viewModel.getRates().value?.currencyList?.size, 3)
        Assert.assertEquals(viewModel.getRates().value?.currencyList?.get(0)?.code, "INR")
        Assert.assertEquals(viewModel.getRates().value?.currencyList?.get(1)?.code, "USA")
        Assert.assertEquals(viewModel.getRates().value?.currencyList?.get(2)?.code, "EUR")
    }

    @Test
    fun `should move clicked item to first in the currency rate list`() {
        //Arrange
        viewModel.fragmentLoaded()

        //Act
        viewModel.currencyClicked(1)

        //Assert
        Assert.assertEquals( 1, viewModel.moveCurrencyToTop().value)
    }

    @Test
    fun `should update all amount when an amount is changed`() {
        //Arrange
        viewModel.fragmentLoaded()

        //Act
        viewModel.currencyChanged(1, "INR", "100")

        //Assert
        Assert.assertEquals(viewModel.getRates().value?.currencyList?.size, 3)
        Assert.assertEquals(200.0f, viewModel.getRates().value?.currencyList?.get(0)?.rate)
        Assert.assertEquals(100.0f, viewModel.getRates().value?.currencyList?.get(1)?.rate)
        Assert.assertEquals(100.0f, viewModel.getRates().value?.currencyList?.get(2)?.rate)
    }

    private fun aDummyRates(base: String): Rates {
        val countryRates = ArrayList<Currency>()
        countryRates.add(Currency("INR", 2f))
        countryRates.add(Currency("USA", 1f))
        countryRates.add(Currency("EUR", 2f))
        return Rates(countryRates, base, "2020-11-01")
    }
}