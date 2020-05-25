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
    @Test
    fun basic_login_false(){
        onView(withId(R.id.email_textView)).perform(typeText("aaa"))
        onView(withId(R.id.password_editText)).perform(typeText("123123"))
        onView(withId(R.id.email_login_button)).perform(click())
        onView(withId(R.id.message2)).check(matches(withText("Authentication failed!")))
    }
   /* nu mai merge
   @Test
    fun check_id_edit_text() {
        onView(withId(R.id.password_editText)).perform(typeText("p"))
        onView(withId(R.id.email_login_button)).perform(click())
        onView(withId(R.id.message_edit_text_email)).check(matches(withText("Please enter email id!")))
    }
    @Test
    fun check_pw_edit_text() {
        onView(withId(R.id.email_textView)).perform(typeText("E"))
        onView(withId(R.id.email_login_button)).perform(click())
        onView(withId(R.id.message_edit_text_password)).check(matches(withText("Please enter your password")))
    }
    @Test
    fun check_google_login() {
        onView(withId(R.id.sign_in_button)).perform(click())
        onView(withId(R.id.message_google)).check(matches(withText("Signed in with Google Successfully")))
    }
*/


}