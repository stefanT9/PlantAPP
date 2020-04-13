package com.example.plantapp

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_take_photo.*

class takePhotoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_take_photo)

        photo_button.setOnClickListener {
            Toast.makeText(this, "photo taken", Toast.LENGTH_LONG).show()
        }
        gallery_button.setOnClickListener {
            Toast.makeText(this, "Enter Gallery", Toast.LENGTH_LONG).show()
        }
    }
}