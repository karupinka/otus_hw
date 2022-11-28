package ru.yandex.repinanr.movies.ui.screens

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import ru.yandex.repinanr.movies.R
import ru.yandex.repinanr.movies.presentation.common.MovieViewHolder
import ru.yandex.repinanr.movies.ui.util.WaitUntilViewExists

class MovieListScreen {
    fun waitForForm() = WaitUntilViewExists.waitForView(R.id.rc_movie)

    fun assertRecyclerViewDisplayed() {
        Espresso.onView(withId(R.id.rc_movie)).check(matches(isDisplayed()))
    }

    fun scrollToItem(position: Int) {
        Espresso.onView(withId(R.id.rc_movie))
            .perform(RecyclerViewActions.scrollToPosition<MovieViewHolder>(position))
    }

    fun clickOnItem(position: Int) {
        Espresso.onView(withId(R.id.rc_movie))
            .perform(RecyclerViewActions.actionOnItemAtPosition<MovieViewHolder>(position, click()))
    }
}