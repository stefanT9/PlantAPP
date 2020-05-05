package com.example.plantapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        hamburgerMenu_imgButton.setOnClickListener {

//            val intent = Intent(this@HomeActivity, TopNavBarActivity::class.java)
//            startActivity(intent)

            Toast.makeText(this@HomeActivity, "the button works!", Toast.LENGTH_SHORT).show()
        }

        scanPlant_imgButton.setOnClickListener {

            val intent = Intent(this@HomeActivity, TakePhotoActivity::class.java)
            startActivity(intent)

        }

        uploadPhoto_imgButton.setOnClickListener {

//            val intent = Intent(this@HomeActivity, UploadPhotoActivity::class.java)
//            startActivity(intent)

            Toast.makeText(this@HomeActivity, "Upload a photo!", Toast.LENGTH_SHORT).show()
        }


    }
}
