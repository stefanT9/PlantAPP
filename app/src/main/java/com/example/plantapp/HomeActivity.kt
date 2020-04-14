package com.example.plantapp

import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_home.*

class MainActivity : AppCompatActivity() {

    lateinit var btnHamburger: ImageButton
    lateinit var scanButton: ImageButton
    lateinit var uploadButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

         btnHamburger = imageButton3

         btnHamburger.setOnClickListener(View.OnClickListener {

             Toast.makeText(this@HomeActivity, "the button works!", Toast.LENGTH_SHORT).show()

         })


        scanButton = imageButton5

        scanButton.setOnClickListener(View.OnClickListener {

            Toast.makeText(this@HomeActivity, "Scan flowers!", Toast.LENGTH_SHORT).show()

        })

        uploadButton = imageButton6

        uploadButton.setOnClickListener(View.OnClickListener {

            Toast.makeText(this@HomeActivity, "Upload a photo!", Toast.LENGTH_SHORT).show()

        })


    }
}
