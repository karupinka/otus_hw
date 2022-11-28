package ru.yandex.repinanr.movies.ui.util

import androidx.test.espresso.Espresso.closeSoftKeyboard
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.platform.app.InstrumentationRegistry
import ru.yandex.repinanr.movies.ui.screens.MovieDetailScreen
import ru.yandex.repinanr.movies.ui.screens.MovieFavoriteScreen
import ru.yandex.repinanr.movies.R
import ru.yandex.repinanr.movies.ui.screens.MovieListScreen
import ru.yandex.repinanr.movies.ui.screens.SaveDialogScreen
import ru.yandex.repinanr.movies.ui.util.WaitUntilViewExists.waitForActivity

fun app(func: App.() -> Unit) = App().apply {
    waitForActivity(InstrumentationRegistry.getInstrumentation())
    func()
}

class App {

    fun movieListScreen(func: MovieListScreen.() -> Unit = {}) = MovieListScreen().apply {
        waitForForm()
        func()
    }

    fun movieDetailScreen(func: MovieDetailScreen.() -> Unit = {}) = MovieDetailScreen().apply {
        waitForScreen()
        func()
    }

    fun movieFavoriteScreen(func: MovieFavoriteScreen.() -> Unit = {}) =
        MovieFavoriteScreen().apply {
            waitForForm()
            func()
        }

    fun saveDialogScreen(func: SaveDialogScreen.() -> Unit = {}) =
        SaveDialogScreen().apply {
            waitForScreen()
            func()
        }

    fun openFavorite(func: MovieFavoriteScreen.() -> Unit) = MovieFavoriteScreen().apply {
        onView(withId(R.id.favoriteMovieFragment)).perform(click())
        waitForForm()
        func()
    }
}