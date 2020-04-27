package com.example.plantapp

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView


class TopNavViewActivity : AppCompatActivity(){
    private lateinit var toolbar: Toolbar
    private lateinit var navViewLayout:DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_top_nav_view)

        toolbar = findViewById(R.id.topNavView)
        setSupportActionBar(toolbar)
        navViewLayout = findViewById(R.id.topNavView_layout)

        val actionBar = ActionBarDrawerToggle(
            this,
            navViewLayout,
            toolbar,
            R.string.openNavView,
            R.string.closeNavView
        )

        navViewLayout.addDrawerListener(actionBar)
        actionBar.syncState()

    }
}
