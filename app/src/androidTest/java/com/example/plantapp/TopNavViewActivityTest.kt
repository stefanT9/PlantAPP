package com.example.plantapp

import org.junit.After
import org.junit.Before
import android.content.Context
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.*
//import androidx.test.rule.ActivityTestRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class TopNavViewActivityTest {
    lateinit var instrumentationContext: Context

    @Before
    fun setUp() {
        instrumentationContext = InstrumentationRegistry.getInstrumentation().context
		onView(withId(R.id.nav_home)).perform(click())
    }
	
	@Test
	fun btnLogIn(){
		onView(withId(R.id.btn_log_in).perform(click())		
		if(logInTxt == 'Log in button was clicked') 
			print('Login button works')
	}
	
	@Test
	fun btnSignUp(){
		onView(withId(R.id.btn_sign_up).perform(click())
		if(signUpTxt == 'Sign up button was clicked') 
			print('SignUp button works')
	}
	
	@Test
	fun btnPlants(){
		onView(withId(R.id.btn_plants).perform(click())
		if(plantsTxt == 'Plants button was clicked') 
			print('Plants button works')
	}
	
	@Test
	fun btnContact(){
		onView(withId(R.id.btn_contact).perform(click())
		if(contactTxt == 'Contact button was clicked') 
			print('Contact button works')
	}
	
	@Test
	fun btnHome(){
		onView(withId(R.id.btn_home).perform(click())
		if(homeTxt == 'Home button was clicked') 
			print('Home button works')
	}

    @After
    fun tearDown() {
		onView(withId(R.id.close_nav_view)).perform(click())
    }
}