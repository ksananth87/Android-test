package com.revolut.androidtest.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.revolut.androidtest.RxTrampolineSchedulerRule
import com.revolut.androidtest.api.exception.InvalidResponseException
import com.revolut.androidtest.domain.Country
import com.revolut.androidtest.domain.RateRepository
import com.revolut.androidtest.domain.Rates
import io.reactivex.Single
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.TestScheduler
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
import java.util.concurrent.TimeUnit

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
        `when`(service.getRates()).thenReturn(Single.just(aDummyRates()))
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
        Assert.assertEquals(viewModel.getRates().value?.countryList?.size, 2)
        Assert.assertEquals(viewModel.getRates().value?.base, "EUR")
        Assert.assertEquals(viewModel.getRates().value?.date, "2020-11-01")
    }

    @Test
    fun `should show error when service failed`() {
        `when`(service.getRates()).thenReturn(Single.error(InvalidResponseException()))

        viewModel.fragmentLoaded()

        Assert.assertEquals(viewModel.getError().value, true)
    }

    @Test
    fun `should call rates every 1 second`() {
        val testScheduler = TestScheduler()
        RxJavaPlugins.setComputationSchedulerHandler { testScheduler }

        viewModel.refreshRatesEveryOneSec()

        testScheduler.advanceTimeBy(2, TimeUnit.SECONDS)
        verify(service, times(2)).getRates()
    }

    private fun aDummyRates(): Rates {
        val countryRates = ArrayList<Country>()
        countryRates.add(Country("INR", 85.33f))
        countryRates.add(Country("USA", 1f))
        return Rates(countryRates, "EUR", "2020-11-01")
    }
}