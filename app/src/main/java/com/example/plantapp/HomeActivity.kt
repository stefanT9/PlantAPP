package com.example.plantapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_top_nav.*

class HomeActivity : TopNavViewActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.layoutInflater.inflate(R.layout.activity_home,mainLayout)

         hamburgerMenu_imgButton.setOnClickListener {
             Toast.makeText(this@HomeActivity, "the button works!", Toast.LENGTH_SHORT).show()
         }

        scanPlant_imgButton.setOnClickListener {
            val intent= Intent(this,TakePhotoActivity::class.java)
            startActivity(intent)
            Toast.makeText(this@HomeActivity, "Scan flowers!", Toast.LENGTH_SHORT).show()
        }

        uploadPhoto_imgButton.setOnClickListener {
            Toast.makeText(this@HomeActivity, "Upload a photo!", Toast.LENGTH_SHORT).show()
        }


    }
}

