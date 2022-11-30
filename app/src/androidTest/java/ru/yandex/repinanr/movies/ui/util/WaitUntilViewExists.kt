package ru.yandex.repinanr.movies.ui.util

import android.app.Activity
import android.app.Instrumentation
import android.util.Log
import android.view.View
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.PerformException
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.runner.lifecycle.ActivityLifecycleMonitorRegistry
import androidx.test.runner.lifecycle.Stage
import org.hamcrest.Matcher
import ru.yandex.repinanr.movies.ui.util.AssertDisplayed.viewIsDisplayed
import java.util.concurrent.TimeoutException
import ru.yandex.repinanr.movies.ui.util.WaitForViewMatcher.waitForViewMatcher

const val timeoutDefault: Long = 5000

object WaitUntilViewExists {

    fun waitForView(
        @IdRes id: Int,
        waitTime: Long = timeoutDefault,
        checkVisibility: Boolean = false
    ) {
        onView(ViewMatchers.isRoot())
            .perform(waitForViewMatcher(ViewMatchers.withId(id), waitTime, checkVisibility))
    }

    fun waitForViewByString(
        @StringRes stringId: Int,
        waitTime: Long = timeoutDefault,
        checkVisibility: Boolean = false
    ) {
        onView(ViewMatchers.isRoot())
            .perform(waitForViewMatcher(ViewMatchers.withText(stringId), waitTime, checkVisibility))
    }

    fun waitUntilViewGone(
        @IdRes id: Int,
        waitTime: Long = timeoutDefault,
    ) {
        do {
            if (!viewIsDisplayed(id)) {
                return
            }
        } while (System.currentTimeMillis() < waitTime)
    }

    fun waitUntilViewDisplayed(
        @IdRes id: Int,
        waitTime: Long = timeoutDefault,
    ) {
        do {
            if (viewIsDisplayed(id)) {
                return
            }
        } while (System.currentTimeMillis() < waitTime)
    }

    fun waitForViewByString(
        text: String,
        waitTime: Long = timeoutDefault,
        checkVisibility: Boolean = false
    ) {
        onView(ViewMatchers.isRoot())
            .perform(waitForViewMatcher(ViewMatchers.withText(text), waitTime, checkVisibility))
    }

    fun waitForViewByContentDescription(
        contentDescription: String,
        waitTime: Long = timeoutDefault,
        checkVisibility: Boolean = false
    ) {
        onView(ViewMatchers.isRoot()).perform(
            waitForViewMatcher(
                ViewMatchers.withContentDescription(contentDescription),
                waitTime,
                checkVisibility
            )
        )
    }

    fun waitForActivity(instrumentation: Instrumentation, timeoutMillis: Long = timeoutDefault) {
        val startTime = System.currentTimeMillis()
        val endTime = startTime + timeoutMillis
        do {
            waitMillis(instrumentation, 100)
            val activityInstance = getActivityInstance(instrumentation)
            if (activityInstance != null) {
                return
            }
        } while (System.currentTimeMillis() < endTime)
        throw PerformException.Builder()
            .withActionDescription("Activity not found")
            .withViewDescription("Initial")
            .withCause(TimeoutException())
            .build()
    }

    fun waitForView(
        viewMatcher: Matcher<View>,
        waitTime: Long = timeoutDefault,
        checkVisibility: Boolean = false
    ) {
        onView(ViewMatchers.isRoot())
            .perform(waitForViewMatcher(viewMatcher, waitTime, checkVisibility))
    }

    fun waitMillis(instrumentation: Instrumentation, delay: Int) {
        synchronized(instrumentation) {
            try {
                (instrumentation as Object).wait(delay.toLong())
            } catch (e: InterruptedException) {
                Log.e("OLOLO", e.toString())
            }
        }
    }

    private fun getActivityInstance(instrumentation: Instrumentation): Activity? {
        val currentActivity = arrayOfNulls<Activity>(1)
        instrumentation.runOnMainSync {
            val resumedActivities: Collection<*> =
                ActivityLifecycleMonitorRegistry.getInstance().getActivitiesInStage(Stage.RESUMED)
            if (resumedActivities.iterator().hasNext()) {
                currentActivity[0] = resumedActivities.iterator().next() as Activity?
            }
        }
        return currentActivity[0]
    }

}