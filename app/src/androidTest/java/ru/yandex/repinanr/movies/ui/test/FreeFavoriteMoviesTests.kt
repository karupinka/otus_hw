package ru.yandex.repinanr.movies.ui.test

import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import ru.yandex.repinanr.movies.presentation.common.MoviesActivity
import ru.yandex.repinanr.movies.ui.util.app

@RunWith(AndroidJUnit4::class)
@LargeTest
class FreeFavoriteMoviesTests {

    @get : Rule
    var mActivityRule = ActivityScenarioRule(MoviesActivity::class.java)

    @Test
    fun testAddFavoriteMovie() {
            app {
            var name = ""

            movieListScreen {
                clickOnItem(0)
            }
            movieDetailScreen {
                clickFavoriteButton()
                name = getTitleText()
                pressBack()
            }
            saveDialogScreen {
                clickYesButton()
            }
            openFavorite {
                assertItem(0, name)
                removeItem(name)
            }
        }
    }
}