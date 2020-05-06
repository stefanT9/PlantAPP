package com.example.plantapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_top_nav.*
import kotlinx.android.synthetic.main.top_nav_no_login_fragment.*


open class TopNavViewActivity : AppCompatActivity(){
    private lateinit var drawerLayout: DrawerLayout

    private fun logged(): Boolean {
        return null == FirebaseAuth.getInstance().currentUser
    }

    private fun setUpToolbar()
    {
        val user = FirebaseAuth.getInstance().currentUser

        setContentView(R.layout.activity_top_nav)
        if(user == null)
        {
            this.layoutInflater.inflate(R.layout.top_nav_login_fragment,navigaton_anchor_view)
            btn_log_in.setOnClickListener {
                val intent= Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }
            btn_sign_up.setOnClickListener {
                val intent = Intent(this, RegisterActivity::class.java)
                startActivity(intent)
            }

        }
        else
        {
            this.layoutInflater.inflate(R.layout.top_nav_no_login_fragment,navigaton_anchor_view)

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpToolbar()

    }

    private fun updateUI()
    {
        ///todo deinflate before
        if(logged())
        {
            this.layoutInflater.inflate(R.layout.top_nav_login_fragment,navigaton_anchor_view)

        }
        else
        {
            this.layoutInflater.inflate(R.layout.top_nav_no_login_fragment,navigaton_anchor_view)
        }    }
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
