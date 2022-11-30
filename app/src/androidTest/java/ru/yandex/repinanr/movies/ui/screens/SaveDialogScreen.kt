package ru.yandex.repinanr.movies.ui.screens

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import ru.yandex.repinanr.movies.R
import ru.yandex.repinanr.movies.ui.util.WaitUntilViewExists.waitForView

class SaveDialogScreen {
    fun waitForScreen() = waitForView(R.id.dialog_yes_button)

    fun clickYesButton() {
        onView(withId(R.id.dialog_yes_button)).perform(click())
    }
}