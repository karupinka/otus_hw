package ru.yandex.repinanr.movies.ui.screens

import androidx.constraintlayout.widget.ConstraintLayout
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.instanceOf
import ru.yandex.repinanr.movies.R
import ru.yandex.repinanr.movies.presentation.common.MovieViewHolder
import ru.yandex.repinanr.movies.ui.util.RecyclerViewMatcher.atPosition
import ru.yandex.repinanr.movies.ui.util.WaitUntilViewExists

class MovieFavoriteScreen {
    fun waitForForm() = WaitUntilViewExists.waitForView(R.id.rc_favorite)

    fun assertRecyclerViewDisplayed() {
        onView(withId(R.id.rc_favorite)).check(matches(isDisplayed()))
    }

    fun assertItem(position: Int, text: String) {
        onView(withId(R.id.rc_favorite))
            .check(matches(atPosition(position, hasDescendant(withText(text)))))
    }

    fun removeItem(text: String) {
        onView(
            allOf(
                withId(R.id.image_view_remove),
                withParent(
                    allOf(
                        instanceOf(ConstraintLayout::class.java),
                        withChild(withText(text))
                    )
                )
            )
        )
            .perform(click())
    }
}