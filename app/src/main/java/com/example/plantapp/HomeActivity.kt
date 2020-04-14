package com.example.plantapphome

import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_home.*

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



         hamburgerMenu_imgButton.setOnClickListener(View.OnClickListener {

             Toast.makeText(this@HomeActivity, "the button works!", Toast.LENGTH_SHORT).show()

         })




        scanPlant_imgButton.setOnClickListener(View.OnClickListener {

            Toast.makeText(this@HomeActivity, "Scan flowers!", Toast.LENGTH_SHORT).show()

        })



        uploadPhoto_imgButton.setOnClickListener(View.OnClickListener {

            Toast.makeText(this@HomeActivity, "Upload a photo!", Toast.LENGTH_SHORT).show()

        })


    }
}

