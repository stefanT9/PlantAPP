package com.example.plantapp

import android.content.Context
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

// Runs all unit tests.
@RunWith(JUnit4::class)
class LoginActivityTest
{
    @Rule
    @JvmField
    val rule: ActivityTestRule<LoginActivity> = ActivityTestRule(
        LoginActivity::class.java)

    lateinit var instrumentationContext: Context

    @Before
    fun setup() {
        instrumentationContext = InstrumentationRegistry.getInstrumentation().context
    }

    @Test
    fun enter_email() {
        onView(withId(R.id.email_textView)).perform(typeText("Email"))
    }
    @Test
    fun enter_password() {
        onView(withId(R.id.password_editText)).perform(typeText("Password"))
    }

    @Test
    fun basic_login_true(){
        onView(withId(R.id.email_textView)).perform(typeText("danj@trl.com"))
        onView(withId(R.id.password_editText)).perform(typeText("abcdefg"))
        onView(withId(R.id.email_login_button)).perform(click())
        onView(withId(R.id.message)).check(matches(withText("Logged in successfully")))
    }


}