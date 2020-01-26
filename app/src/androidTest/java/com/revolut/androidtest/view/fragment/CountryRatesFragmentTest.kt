package com.revolut.androidtest.view.fragment

import android.content.Intent
import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.revolut.androidtest.R
import com.revolut.androidtest.domain.RateRepository
import com.revolut.androidtest.domain.model.Currency
import com.revolut.androidtest.domain.model.Rates
import com.revolut.androidtest.view.MainActivity
import com.revolut.androidtest.view.utils.RecycleViewMatcher
import com.revolut.androidtest.view.utils.ViewShownIdlingResource
import io.reactivex.Single
import io.reactivex.SingleEmitter
import org.hamcrest.Matcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.Mockito.`when` as whenever


@RunWith(AndroidJUnit4::class)
class CountryRatesFragmentTest {
    @Rule
    @JvmField
    var activityRule = ActivityTestRule(MainActivity::class.java, true, false)

    @Mock
    private lateinit var mockRatesRepository: RateRepository

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun testCurrencyListVisibleAfterServiceCall() {
        activityRule.launchActivity(Intent())

        waitViewShown(ViewMatchers.withId(R.id.countryList))
    }

    @Test
    fun testRecyclerViewShowingCorrectItemsAfterScroll() {
        mockRepo()

        //activityRule.launchActivity(Intent())

        //onView(ViewMatchers.withId(R.id.countryList)).perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(21))
        //checkRateOnPosition(21, "NZD")
    }

    private fun checkRateOnPosition(position: Int, expectedName: String) {
        onView(
            withRecyclerView(R.id.countryList).atPositionOnView(
                position,
                R.id.countryCurrency
            )
        )
            .check(matches(ViewMatchers.withText(expectedName)))
    }

    private fun withRecyclerView(recyclerViewId: Int): RecycleViewMatcher {
        return RecycleViewMatcher(recyclerViewId)
    }

    private fun waitViewShown(matcher: Matcher<View>) {
        val idlingResource = ViewShownIdlingResource(matcher)
        try {
            IdlingRegistry.getInstance().register(idlingResource)
            onView(matcher).check(matches(isDisplayed()))
        } finally {
            IdlingRegistry.getInstance().unregister(idlingResource)
        }
    }


    private fun mockRepo() {
        val mockSingle = Single.create<Rates> { emitter: SingleEmitter<Rates> ->
            val ratesModel = aDummyRates("EUR")
            emitter.onSuccess(ratesModel)
        }
        whenever(mockRatesRepository.getRates()).thenReturn(mockSingle)
    }

    private fun aDummyRates(base: String): Rates {
        val countryRates = ArrayList<Currency>()
        countryRates.add(Currency("AUD", 78.64f))
        countryRates.add(Currency("BGN", 1.10f))
        countryRates.add(Currency("BRL", 1f))
        countryRates.add(Currency("CAD", 1f))
        countryRates.add(Currency("CHF", 1f))
        countryRates.add(Currency("CNY", 1f))
        countryRates.add(Currency("CZK", 1f))
        countryRates.add(Currency("DKK", 1f))
        countryRates.add(Currency("GBP", 1f))
        countryRates.add(Currency("HKD", 1f))
        countryRates.add(Currency("HRK", 1f))
        countryRates.add(Currency("HUF", 1f))
        countryRates.add(Currency("IDR", 1f))
        countryRates.add(Currency("ILS", 1f))
        countryRates.add(Currency("INR", 1f))
        countryRates.add(Currency("ISK", 1f))
        countryRates.add(Currency("JPY", 1f))
        countryRates.add(Currency("KRW", 1f))
        countryRates.add(Currency("MXN", 1f))
        countryRates.add(Currency("MYR", 1f))
        countryRates.add(Currency("NOK", 22f))
        countryRates.add(Currency("NZD", 22f))
        return Rates(countryRates, base, "2020-11-01")
    }
}