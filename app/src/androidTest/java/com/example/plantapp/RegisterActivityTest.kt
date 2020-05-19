package com.example.plantapp

import androidx.test.espresso.Espresso.*
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers.withDecorView
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import org.hamcrest.Matchers.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4


// Runs all unit tests.
@RunWith(JUnit4::class)
class RegisterActivityTest {


    // set the activity layer you want test
    @Rule
    @JvmField

    val rule: ActivityTestRule<RegisterActivity> = ActivityTestRule(RegisterActivity::class.java)

    @Before
    fun setupContext(){
        val contextApplication = InstrumentationRegistry.getInstrumentation().context
    }

    @Test
    fun clientCanEnterFirstName(){
        onView(withId(R.id.editText)).perform(typeText("testFirstName"))
    }

    @Test
    fun clientCanEnterEmailAddress(){
        onView(withId(R.id.regEmail)).perform(typeText("testEmail@emailTest.com"))
    }

    @Test
    fun clientCanEnterPassword(){
        onView(withId(R.id.regPassword)).perform(typeText("parolaTest"))
    }

    @Test
    fun registerWithAnExistingAccount(){

        onView(withId(R.id.editText)).perform(typeText("testfirstname"))
        onView(withId(R.id.regEmail)).perform(typeText("testareip@yahoo.com"))
        closeSoftKeyboard();
        onView(withId(R.id.regPassword)).perform(typeText("parolaTest123"))
        closeSoftKeyboard();
        onView(withId(R.id.btn_Register)).perform(click())


        onView(withText(startsWith("Signed up successfully!"))).inRoot(
            withDecorView(
                not(`is`(rule.activity.window.decorView))
            )
        ).check(matches(isDisplayed()))
    }

    @Test
    fun registerWithoutFirstName(){

        //onView(withId(R.id.editText)).perform(typeText("testfirstname"))
        onView(withId(R.id.regEmail)).perform(typeText("testareip@yahoo.com"))
        closeSoftKeyboard();
        onView(withId(R.id.regPassword)).perform(typeText("parolaTest123"))
        closeSoftKeyboard();
        onView(withId(R.id.btn_Register)).perform(click())


        onView(withText(startsWith("Signed up successfully!"))).inRoot(
            withDecorView(
                not(`is`(rule.activity.window.decorView))
            )
        ).check(matches(isDisplayed()))
    }

    @Test
    fun registerWithoutEmailAddress(){

        onView(withId(R.id.editText)).perform(typeText("testfirstname"))
        //onView(withId(R.id.regEmail)).perform(typeText("testareip@yahoo.com"))
        closeSoftKeyboard();
        onView(withId(R.id.regPassword)).perform(typeText("parolaTest123"))
        closeSoftKeyboard();
        onView(withId(R.id.btn_Register)).perform(click())


        onView(withText(startsWith("Signed up successfully!"))).inRoot(
            withDecorView(
                not(`is`(rule.activity.window.decorView))
            )
        ).check(matches(isDisplayed()))
    }


    @Test
    fun registerWithoutPassword(){

        onView(withId(R.id.editText)).perform(typeText("testfirstname"))
        onView(withId(R.id.regEmail)).perform(typeText("testareip@yahoo.com"))
        closeSoftKeyboard();
        //onView(withId(R.id.regPassword)).perform(typeText("parolaTest123"))
        closeSoftKeyboard();
        onView(withId(R.id.btn_Register)).perform(click())


        onView(withText(startsWith("Signed up successfully!"))).inRoot(
            withDecorView(
                not(`is`(rule.activity.window.decorView))
            )
        ).check(matches(isDisplayed()))
    }


    // to discover a passed test for the next test, just change every time you run this test the email address

    @Test
    fun registerSuccessfuly(){

        onView(withId(R.id.editText)).perform(typeText("testfirstname"))
        onView(withId(R.id.regEmail)).perform(typeText("cont.test3@yahoo.com"))
        closeSoftKeyboard();
        onView(withId(R.id.regPassword)).perform(typeText("parolaTest123"))
        closeSoftKeyboard();
        onView(withId(R.id.btn_Register)).perform(click())


        onView(withText(startsWith("Signed up successfully!"))).inRoot(
            withDecorView(
                not(`is`(rule.activity.window.decorView))
            )
        ).check(matches(isDisplayed()))
    }

}