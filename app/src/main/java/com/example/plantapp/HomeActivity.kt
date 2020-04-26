package com.example.plantapp

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.plantapp.R
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)



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

