package com.example.plantapp

import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


class TopNavViewActivity : AppCompatActivity(){
    private lateinit var toolbar: Toolbar
    private lateinit var drawerLayout: DrawerLayout
    private  lateinit var actionBarDrawerToggle: ActionBarDrawerToggle

    fun logged(): Boolean {
        return null == FirebaseAuth.getInstance().getCurrentUser()
    }

    fun setUpToolbar()
    {
        if(logged()){
            setContentView(R.layout.activity_top_nav_view_logged)
            drawerLayout =  findViewById(R.id.topNavView_layoutLogged)
            toolbar = findViewById(R.id.topNavViewLogged)
        }
        else
        {
            setContentView(R.layout.activity_top_nav_view)
            drawerLayout =  findViewById(R.id.topNavView_layout)
            toolbar = findViewById(R.id.topNavView)
        }
        setSupportActionBar(toolbar)
        actionBarDrawerToggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.openNavView,
            R.string.closeNavView
        )
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpToolbar()

    }
}
