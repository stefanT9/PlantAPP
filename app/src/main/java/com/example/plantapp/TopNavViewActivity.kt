package com.example.plantapp

import android.content.Intent
import android.net.Uri
import android.opengl.Visibility
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_top_nav.*
import kotlinx.android.synthetic.main.top_nav_no_login_fragment.*


open class TopNavViewActivity : AppCompatActivity(){
    private lateinit var drawerLayout: DrawerLayout

    private fun logged(): Boolean {
        return null == FirebaseAuth.getInstance().currentUser
    }

    protected fun setUpToolbar()
    {
        val user = FirebaseAuth.getInstance().currentUser

        setContentView(R.layout.activity_top_nav)

        /// TODO: Make Contact button go to the mail with preset email address(Robert Zahariea) intenturi speciale
        /// TODO: Make logout button only visible when you are logged in and add functionality to it(Robert Zahariea)
        /// TODO: Make Home button go to HomeActivity (Robert Zahariea)

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
            Toast.makeText(this, "btn_home clicked", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

        btn_plants.setOnClickListener {
            Toast.makeText(this, "btn_plants clicked", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, TakePhotoActivity::class.java)
            startActivity(intent)
        }

        if(user != null)
        {
            btn_log_out.visibility = View.GONE
            this.layoutInflater.inflate(R.layout.top_nav_no_login_fragment,navigaton_anchor_view)

            btn_log_in.setOnClickListener {
                Toast.makeText(this,"login clicked",Toast.LENGTH_SHORT).show()
                val intent= Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }
            btn_sign_up.setOnClickListener {
                Toast.makeText(this,"registered clicked",Toast.LENGTH_SHORT).show()
                val intent = Intent(this, RegisterActivity::class.java)
                startActivity(intent)
            }
        }
        else
        {
            btn_log_out.visibility = View.VISIBLE
            this.layoutInflater.inflate(R.layout.top_nav_login_fragment,navigaton_anchor_view)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpToolbar()
    }

    protected fun updateUI()
    {
        ///todo deflate before
        if(logged())
        {
            this.layoutInflater.inflate(R.layout.top_nav_login_fragment,navigaton_anchor_view)
        }
        else
        {
            this.layoutInflater.inflate(R.layout.top_nav_no_login_fragment,navigaton_anchor_view)
        }
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
