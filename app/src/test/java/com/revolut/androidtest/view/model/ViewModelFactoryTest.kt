package com.revolut.androidtest.view.model

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.revolut.androidtest.domain.RateRepository
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.instanceOf
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class ViewModelFactoryTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    private lateinit var service: RateRepository

    private lateinit var factory: ViewModelFactory

    @Before
    fun setUp() {
        factory = ViewModelFactory(service)
    }

    @Test
    fun `should return corresponding viewModel based on provided class`() {
        val viewModel = factory.create(CountryRatesViewModel::class.java)

        assertThat(viewModel, `is`(instanceOf(CountryRatesViewModel::class.java)))
    }
}