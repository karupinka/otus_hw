package ru.yandex.repinanr.movies

import android.R
import androidx.core.os.bundleOf
import androidx.fragment.app.testing.launchFragment
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.LooperMode
import ru.yandex.repinanr.movies.presentation.dialog.DateDialog

@RunWith(AndroidJUnit4::class)
@LooperMode(LooperMode.Mode.PAUSED)
class DateDialogFragmentTests {

    @Test
    fun checkTitle() {
        val fragmentArgs = bundleOf("movieId" to TEST_MOVIE_ID)
        launchFragmentInContainer<DateDialog>(fragmentArgs)
        onView(withText(R.string.cancel)).check(matches(isDisplayed()))
    }

    @Test
    fun checkDialogDismiss() {
        val fragmentArgs = bundleOf("movieId" to TEST_MOVIE_ID)
        with(launchFragment<DateDialog>(fragmentArgs)) {
            onFragment { fragment ->
                assert(fragment.dialog != null)
                assert(fragment.requireDialog().isShowing)
                fragment.dismiss()
                fragment.parentFragmentManager.executePendingTransactions()
                assert(fragment.dialog == null)
            }
        }
    }

    companion object {
        const val TEST_MOVIE_ID = 12
    }
}