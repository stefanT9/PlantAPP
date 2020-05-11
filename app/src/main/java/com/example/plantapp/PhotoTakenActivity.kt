package com.example.plantapp

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.graphics.drawable.BitmapDrawable
import android.media.ExifInterface
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import io.fotoapparat.result.BitmapPhoto
import kotlinx.android.synthetic.main.activity_photo_taken.*
import kotlinx.android.synthetic.main.activity_top_nav.*
import plantToTextAPI.getPlantName
import plantToTextAPI.ocrFunction
import wikiapi.wikiapi

@Volatile
var done: Boolean = false

@Volatile
var failed: Boolean = false

class PhotoTakenActivity : TopNavViewActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        done = false
        failed = false

        /// TODO: make activity responsive to the intent coming in from either upload or camera ( Robert Zahariea )

        this.layoutInflater.inflate(R.layout.activity_photo_taken, mainLayout)
        seeresult.isClickable = true
        progressBar.visibility = View.GONE

        val bitmap = BitmapFactory.decodeStream(this.openFileInput("myImage"))


        val photoBitmap = BitmapPhoto(bitmap, 0)
        plant_photo.setImageDrawable(BitmapDrawable(resources, photoBitmap.bitmap))

        retakephoto.setOnClickListener {
            finish()
        }

        seeresult.setOnClickListener {
            Toast.makeText(this, "See result pressed!", Toast.LENGTH_SHORT).show()
            //TODO: progressBar on Absolute Layout( Robert zahariea )
            progressBar.visibility = View.VISIBLE
            val intent = Intent(this, DataVisualisationActivity::class.java)
            Thread {
                val plantName = getPlantName((photoBitmap))
                println("plantname finished")
                val res = wikiapi(plantName)
                println("wikiapi finished")
                if (res != null) {
                    intent.putExtra("description", res["description"])
                    intent.putExtra("table", res["table"])
                    intent.putExtra("latinName", plantName)
                    intent.putExtra("photoUrl", res["image"])
                    if (!done) {
                        done = true
                        startActivity(intent)
                    }
                } else {
                    if (!failed) {
                        failed = true
                    } else {
                        println("something happened")
                        Toast.makeText(this, "Try o make another pcture", Toast.LENGTH_SHORT).show()
                    }
                }
            }.start()

            Thread {
                val plantName = ocrFunction((photoBitmap))
                println("plantname finished")
                val res = wikiapi(plantName)
                println("wikiapi finished")
                if (res != null) {
                    intent.putExtra("description", res["description"])
                    intent.putExtra("table", res["table"])
                    intent.putExtra("latinName", plantName)
                    intent.putExtra("photoUrl", res["image"])
                    if (!done) {
                        done = true
                        startActivity(intent)
                    }
                } else {
                    if (!failed) {
                        failed = true
                    } else {
                        println("something happened")
                        Toast.makeText(this, "Try o make another pcture", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            seeresult.isClickable = false
        }
    }


}

