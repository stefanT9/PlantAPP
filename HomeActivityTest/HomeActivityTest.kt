package com.example.plantapp

import android.content.Context
import android.service.autofill.Validators.not
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.internal.inject.InstrumentationContext
import androidx.test.espresso.matcher.RootMatchers.withDecorView
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.google.common.base.CharMatcher.`is`
import junit.framework.TestCase
import org.hamcrest.Matchers
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.util.regex.Pattern.matches

@RunWith(JUnit4::class)
class HomeActivityTest:TestCase(){

    @Rule
    @JvmField
    val rule:ActivityTestRule<HomeActivity> = ActivityTestRule(HomeActivity::class.java)


    @Test
    fun scanPlantBtn() {
        onView(withId(R.id.scanPlant_imgButton)).perform(click())
        onView(withText(Matchers.startsWith("Scan plant btn success"))).inRoot(
            withDecorView(
                Matchers.not(Matchers.`is`(rule.activity.window.decorView))
            )
        ).check(ViewAssertions.matches(isDisplayed()))
    }

    @Test
    fun uploadPhotoAccesDenied(){
        onView(withId(R.id.uploadPhoto_imgButton)).perform(click())
        onView(withText(Matchers.startsWith("Upload photo, permission denied"))).inRoot(
            withDecorView(
                Matchers.not(Matchers.`is`(rule.activity.window.decorView))
            )
        ).check(ViewAssertions.matches(isDisplayed()))
    }


    @Test
    fun uploadPhotoAccesGranted(){
        onView(withId(R.id.uploadPhoto_imgButton)).perform(click())
        onView(withText(Matchers.startsWith("Upload photo, permission already granted"))).inRoot(
            withDecorView(
                Matchers.not(Matchers.`is`(rule.activity.window.decorView))
            )
        ).check(ViewAssertions.matches(isDisplayed()))
    }
}
