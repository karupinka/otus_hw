package ru.yandex.repinanr.movies.ui.screens

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import ru.yandex.repinanr.movies.R
import ru.yandex.repinanr.movies.ui.util.TextActions.getText
import ru.yandex.repinanr.movies.ui.util.WaitUntilViewExists.waitForView

class MovieDetailScreen {
    fun waitForScreen() = waitForView(R.id.iv_movie)

    fun assertImageDisplayed() {
        onView(withId(R.id.iv_movie)).check(matches(isDisplayed()))
    }

    fun getTitleText(): String {
        return getText(onView(withId(R.id.tv_movie_name)))
    }

    fun clickFavoriteButton() {
        onView(withId(R.id.favorite_btn)).perform(click())
    }

    fun pressBack() {
        onView(isRoot()).perform(ViewActions.pressBack())
    }
}