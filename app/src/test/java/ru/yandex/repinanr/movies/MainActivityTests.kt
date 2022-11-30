package ru.yandex.repinanr.movies

import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario.launch
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import ru.yandex.repinanr.movies.presentation.common.MoviesActivity

@RunWith(RobolectricTestRunner::class)
class MainActivityTests {

    @Test
    fun visibleBottomNavigation() {
        val scenario = launch(MoviesActivity::class.java)

        scenario.moveToState(Lifecycle.State.CREATED)

        scenario.onActivity { activity ->
            assert(activity.mainBinding.bottomNavigation.isVisible)
        }
    }
}