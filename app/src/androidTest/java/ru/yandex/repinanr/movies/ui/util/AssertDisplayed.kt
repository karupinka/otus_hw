package ru.yandex.repinanr.movies.ui.util

import android.view.View
import androidx.annotation.IdRes
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import org.hamcrest.Matcher

object AssertDisplayed {

    fun viewIsDisplayed(@IdRes viewId: Int): Boolean {
        var isDisplayed = true
        onView(ViewMatchers.withId(viewId)).withFailureHandler { _, _ -> isDisplayed = false }
            .check(matches(isDisplayed()))
        return isDisplayed
    }

    fun viewIsDisplayed(matcher: Matcher<View>): Boolean {
        var isDisplayed = true
        onView(matcher).withFailureHandler { _, _ -> isDisplayed = false }
            .check(matches(isDisplayed()))
        return isDisplayed
    }
}
