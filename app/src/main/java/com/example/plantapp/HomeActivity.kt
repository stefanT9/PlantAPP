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

        scanPlant_imgButton.setOnClickListener {
            val intent= Intent(this,TakePhotoActivity::class.java)
            startActivity(intent)
        }

        uploadPhoto_imgButton.setOnClickListener {
            // http://www.codeplayon.com/2018/11/android-image-upload-to-server-from-camera-and-gallery/
            // TODO: make this work(Teodora Balan)
        }

    }
}

