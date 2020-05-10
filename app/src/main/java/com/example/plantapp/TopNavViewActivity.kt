package com.example.plantapp

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_top_nav.*
import kotlinx.android.synthetic.main.top_nav_login_fragment.*
import kotlinx.android.synthetic.main.top_nav_no_login_fragment.*


open class TopNavViewActivity : AppCompatActivity(){

    private fun logged(): Boolean {
        // TODO: make ui update when a user logs in(Stefan Tomsa)
        return null != FirebaseAuth.getInstance().currentUser
    }

    protected fun setUpToolbar()
    {
        val user = FirebaseAuth.getInstance().currentUser

        println("---------------")
        println(user?.email)
        println("---------------")


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
            Toast.makeText(this, "btn_home clicked", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
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

        updateUI()
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
            // TODO: Update content of the view with user data (Alex Barsan)

            btn_log_out.visibility = View.VISIBLE
            this.layoutInflater.inflate(R.layout.top_nav_login_fragment,navigaton_anchor_view)
            username.setText(FirebaseAuth.getInstance().currentUser?.displayName.toString())
            mail.setText(FirebaseAuth.getInstance().currentUser?.email.toString())
        }
    }
}
