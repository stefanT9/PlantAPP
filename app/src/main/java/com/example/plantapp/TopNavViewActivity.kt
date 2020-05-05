package com.example.plantapp

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.chip.Chip
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


class TopNavViewActivity : AppCompatActivity(){
    private lateinit var toolbar: Toolbar
    private lateinit var drawerLayout: DrawerLayout
    private  lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    private lateinit var navigationView: NavigationView
    private lateinit var close : AppCompatImageButton
    private lateinit var logIn : Button
    private lateinit var signUp : Button
    private lateinit var plants : Chip
    private lateinit var contact : Chip
    private lateinit var home : Chip
    private lateinit var logOut : Chip

    fun logged(): Boolean {
        return null == FirebaseAuth.getInstance().getCurrentUser()
    }

    fun setUpToolbar()
    {
        if(logged()){
            setContentView(R.layout.activity_top_nav_view_logged)
            drawerLayout =  findViewById(R.id.topNavView_layoutLogged)
            toolbar = findViewById(R.id.topNavViewLogged)
            navigationView = findViewById(R.id.navViewLogged)

            //Event listeners
            close = findViewById(R.id.close_nav_view_logged)
            close.setOnClickListener {
                drawerLayout.closeDrawer(GravityCompat.START);
            }

            logOut = findViewById(R.id.btn_log_out)
            logOut.setOnClickListener {
                //Change activity to home and log out
                var logOutText = Toast.makeText(applicationContext,"Log out button was clicked",Toast.LENGTH_SHORT)
				logOutText.show()
            }
        }
        else
        {
            setContentView(R.layout.activity_top_nav_view)
            drawerLayout =  findViewById(R.id.topNavView_layout)
            toolbar = findViewById(R.id.topNavView)
            navigationView = findViewById(R.id.navView)

            //Event listeners
            close = findViewById(R.id.close_nav_view)
            close.setOnClickListener {
                drawerLayout.closeDrawer(GravityCompat.START);
            }
            logIn = findViewById(R.id.btn_log_in)
            logIn.setOnClickListener {
                //Change activity to log in
                var logInText = Toast.makeText(applicationContext,"Log in button was clicked",Toast.LENGTH_SHORT)
				logInText.show()
            }
            signUp = findViewById(R.id.btn_sign_up)
            signUp.setOnClickListener {
                //Change activity to sign up
                var signUpTxt = Toast.makeText(applicationContext,"Sign up button was clicked",Toast.LENGTH_SHORT)
				signUpTxt.show()
            }
        }



        plants = findViewById(R.id.btn_plants)
        plants.setOnClickListener {
            //Change activity to plants
            Toast.makeText(applicationContext,"Plants button was clicked",Toast.LENGTH_SHORT).show()
        }
        contact = findViewById(R.id.btn_contact)
        contact.setOnClickListener {
            //Change activity to contact
            var contactTxt = Toast.makeText(applicationContext,"Contact button was clicked",Toast.LENGTH_SHORT)
			contactTxt.show()
        }
        home = findViewById(R.id.btn_home)
        home.setOnClickListener {
            //Change activity to home
            var homeTxt = Toast.makeText(applicationContext,"Home button was clicked",Toast.LENGTH_SHORT)
			homeTxt.show()
        }

        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        actionBar?.title=" "

//        actionBarDrawerToggle = ActionBarDrawerToggle(
//            this,
//            drawerLayout,
//            toolbar,
//            R.string.openNavView,
//            R.string.closeNavView
//        )


//        drawerLayout.addDrawerListener(actionBarDrawerToggle)
//        actionBarDrawerToggle.syncState()
        toolbar.setNavigationOnClickListener{
            if(drawerLayout.isDrawerOpen(GravityCompat.START)){
                drawerLayout.closeDrawer(GravityCompat.START);
            }
            else{
                drawerLayout.openDrawer(GravityCompat.START);
                var openMenu = Toast.makeText(applicationContext,"Deschidem meniu",Toast.LENGTH_SHORT)
				openMenu.show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpToolbar()
    }

    override fun onBackPressed(){
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else
        {
            super.onBackPressed();
        }
    }


}
