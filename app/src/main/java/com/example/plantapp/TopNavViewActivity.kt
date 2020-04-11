package com.example.plantapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

class TopNavViewActivity : AppCompatActivity(){
    private lateinit var toolbar: Toolbar
    private lateinit var navViewLayout:DrawerLayout
    private lateinit var navView:NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_top_nav_view)

        //cred ca aici ar veni if ul pt stabilire daca este logat sau nu si apoi setate toate variabilele
        toolbar = findViewById(R.id.topNavView)
        setSupportActionBar(toolbar)
        navViewLayout = findViewById(R.id.topNavView_layout)
        navView = findViewById(R.id.navView)

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
