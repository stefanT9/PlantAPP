package com.example.plantapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_photo_taken.*

class PhotoTakenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_taken)



        retakephoto.setOnClickListener(View.OnClickListener {
            Toast.makeText(this@PhotoTakenActivity, "Retake photo!", Toast.LENGTH_SHORT).show()
        })



        seeresult.setOnClickListener(View.OnClickListener {

            Toast.makeText(this@PhotoTakenActivity, "See result!", Toast.LENGTH_SHORT).show()

        })
    }
}

