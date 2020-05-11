package com.example.plantapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_top_nav.*


const val PICK_IMAGE = 1

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
            val picture = Intent()
            picture.type = "image/*"
            picture.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(picture, "Select Picture"), PICK_IMAGE)

            /*val intent = Intent(this, DataVisualisationActivity::class.java).apply {
                putExtra("picture from gallery", picture.)
            }
            startActivity(intent)*/
        }


    }
}

