package com.example.plantapp

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageButton
import androidx.core.text.HtmlCompat
import androidx.core.view.GravityCompat
import com.google.firebase.BuildConfig
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_top_nav.*
import kotlinx.android.synthetic.main.top_nav_login_fragment.*
import kotlinx.android.synthetic.main.top_nav_no_login_fragment.*


open class TopNavViewActivity : AppCompatActivity(){

    private fun logged(): Boolean {
        return null != FirebaseAuth.getInstance().currentUser
    }

    protected fun setUpToolbar()
    {
        val user = FirebaseAuth.getInstance().currentUser
        setContentView(R.layout.activity_top_nav)

        btn_contact.setOnClickListener {
            val mailAddress = "robert.zahariea@gmail.com"
            val arr = arrayOf(mailAddress)
            val versionCode = BuildConfig.VERSION_CODE
            val osVersion = android.os.Build.VERSION.SDK_INT
            val deviceModel = Build.MANUFACTURER + " " + Build.MODEL

            val mailBody = "<html> <br><br><br><hr> <p> OS Version: $osVersion</p><p> Device: $deviceModel</p><p> App version: $versionCode</p></html>"

            val intent = Intent(Intent.ACTION_SENDTO).apply {
                type = "text/html"
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, arr)
                putExtra(Intent.EXTRA_SUBJECT, "PlantApp Contact")
                if(Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
                    putExtra(Intent.EXTRA_TEXT, Html.fromHtml(mailBody, HtmlCompat.FROM_HTML_MODE_LEGACY))
                } else {
                    @Suppress("DEPRECATION")
                    putExtra(Intent.EXTRA_TEXT, Html.fromHtml(mailBody))
                }
            }
            if(intent.resolveActivity(packageManager) != null) {
                startActivity(intent)
            }
        }

        btn_home.setOnClickListener {
            var currentActivity = window.decorView.rootView.toString().split("[", "]")[1]
            if(currentActivity != "HomeActivity") {
                Toast.makeText(this, "btn_home clicked", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
            }
            else
            {
                if (topNavView.isDrawerOpen(GravityCompat.START)) {
                    topNavView.closeDrawer(GravityCompat.START)
                }
            }
        }

        btn_quick_scan.setOnClickListener {
            Toast.makeText(this, "btn_quick_scan clicked", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, TakePhotoActivity::class.java)
            startActivity(intent)
        }

        btn_log_out.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            updateUI()
        }

        // TODO: make sure that when navigation is open keyboard is closed ( Stefan Tomsa )

        topNavViewLogged.title = " ";
        setSupportActionBar(topNavViewLogged)
        topNavViewLogged.setNavigationOnClickListener {
            topNavView.openDrawer(GravityCompat.START)
        }

        close_nav_view_logged.setOnClickListener {
            if(topNavView.isDrawerOpen(GravityCompat.START)){
                topNavView.closeDrawer(GravityCompat.START)
            }
        }

        updateUI()
    }
    override fun onBackPressed() {
        if (topNavView.isDrawerOpen(GravityCompat.START)) {
            topNavView.closeDrawer(GravityCompat.START)
        }
        else {
            super.onBackPressed();
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpToolbar()
    }

    protected fun updateUI()
    {
        navigaton_anchor_view.removeAllViews()

        if(!logged())
        {
            btn_log_out.visibility = View.GONE
            this.layoutInflater.inflate(R.layout.top_nav_no_login_fragment,navigaton_anchor_view)

            btn_log_in.setOnClickListener {
                var currentActivity = window.decorView.rootView.toString().split("[", "]")[1]
                if(currentActivity != "LoginActivity") {
                    Toast.makeText(this, "login clicked", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                }
                else
                {
                    if (topNavView.isDrawerOpen(GravityCompat.START)) {
                        topNavView.closeDrawer(GravityCompat.START)
                    }
                }
            }
            btn_sign_up.setOnClickListener {
                var currentActivity = window.decorView.rootView.toString().split("[", "]")[1]
                if(currentActivity != "RegisterActivity"){
                    Toast.makeText(this, "Sign Up button clicked" ,Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, RegisterActivity::class.java)
                    startActivity(intent)
                }
                else
                {
                    if (topNavView.isDrawerOpen(GravityCompat.START)) {
                        topNavView.closeDrawer(GravityCompat.START)
                    }
                }
            }
        }
        else
        {
            btn_log_out.visibility = View.VISIBLE
            this.layoutInflater.inflate(R.layout.top_nav_login_fragment,navigaton_anchor_view)
            username.text = FirebaseAuth.getInstance().currentUser?.displayName.toString()
            mail.text = FirebaseAuth.getInstance().currentUser?.email.toString()
        }
    }
}
