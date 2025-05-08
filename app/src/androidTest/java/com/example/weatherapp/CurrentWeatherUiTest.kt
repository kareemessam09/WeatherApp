package com.example.weatherapp

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.regex.Pattern.matches
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.espresso.assertion.ViewAssertions.*
import org.hamcrest.Matchers.*

@RunWith(AndroidJUnit4::class)
class CurrentWeatherUiTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun labelTemp_isDisplayedWithCorrectText() {
        onView(withId(R.id.label_temp))
            .check(matches(isDisplayed())) // Ensure it's visible

        onView(withId(R.id.label_temp))
            .check(matches(withText("Current Temperature"))) // Ensure the text is correct
    }
}
