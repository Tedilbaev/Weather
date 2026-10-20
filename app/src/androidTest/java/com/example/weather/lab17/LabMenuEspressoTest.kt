package com.example.weather.lab17

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.weather.LabMenuActivity
import com.example.weather.R
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LabMenuEspressoTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(LabMenuActivity::class.java)

    @Test
    fun menuTitle_isVisible() {
        onView(withText(R.string.lab_menu_title))
            .check(matches(isDisplayed()))
    }

    @Test
    fun lab1Button_isVisible_andClickable() {
        onView(withId(R.id.btnLab1))
            .check(matches(isDisplayed()))
            .perform(click())
    }
}