package ru.yandex.repinanr.movies

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import ru.yandex.repinanr.movies.presentation.common.MoviesActivity
import ru.yandex.repinanr.movies.presentation.dialog.SaveDataDialog

//@RunWith(RobolectricTestRunner.class)
//class SaveDialogFragmentTests {
//
//    @Test
//    fun shouldNotBeNull() {
//        // GIVEN
//        val contactName = "Test User"
//        val scenario = launchActivity<AddContactActivity>()
//
//        // WHEN
//        // Enter contact name
//        onView(withId(R.id.contact_name_text)).perform(typeText(contactName))
//        // Destroy and recreate Activity
//        scenario.recreate()
//
//        // THEN
//        // Check contact name was preserved.
//        onView(withId(R.id.contact_name_text)).check(matches(withText(contactName)))
//    }
//}