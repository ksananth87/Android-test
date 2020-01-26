package com.revolut.androidtest.view

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.revolut.androidtest.R
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class MainActivityTest{

    @get:Rule
    public val mActivityRule: ActivityTestRule<MainActivity> =
        ActivityTestRule(MainActivity::class.java)

    @Test
    fun shouldShowTitleBarWhenAppLaunched() {
        onView(withId(R.id.toolbar_main_title)).check(matches(isDisplayed()))
    }

}