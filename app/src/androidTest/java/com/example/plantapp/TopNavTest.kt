package com.example.plantapp

import android.content.Context
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.swipeRight
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.google.firebase.auth.FirebaseAuth
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

// Runs all unit tests.
@RunWith(JUnit4::class)
class TopNavTest
{
    fun logged(): Boolean {
        return null != FirebaseAuth.getInstance().currentUser
    }
    @Rule
    @JvmField
    val rule: ActivityTestRule<TopNavViewActivity> = ActivityTestRule(
        TopNavViewActivity
        ::class.java)

    lateinit var instrumentationContext: Context

    @Before
    fun setup() {
        instrumentationContext = InstrumentationRegistry.getInstrumentation().context
    }
    @Test
    fun swipe_drawer(){
        onView(withId(R.id.topNavViewLogged)).perform(swipeRight())
    }
    @Test
    fun click_drawer() {
        onView(withId(R.id.topNavViewLogged)).perform(click())
    }
    @Test
    fun click_login() {
        if (!logged()) {
            onView(withId(R.id.topNavViewLogged)).perform(click())
            onView(withId(R.id.btn_log_in)).perform(click())
        }
    }
    @Test
    fun click_register() {
        if (!logged()) {
            onView(withId(R.id.topNavViewLogged)).perform(click())
            onView(withId(R.id.btn_sign_up)).perform(click())
        }
    }
    @Test
    fun click_home() {
        onView(withId(R.id.topNavViewLogged)).perform(click())
        onView(withId(R.id.btn_home)).perform(click())
    }



}