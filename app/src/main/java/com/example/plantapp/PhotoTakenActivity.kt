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

        val intentFrom:String? = intent.getStringExtra("From")


        this.layoutInflater.inflate(R.layout.activity_photo_taken, mainLayout)
        seeresult.isClickable = true
        progressBar.visibility = View.GONE

        val bitmap = BitmapFactory.decodeStream(this.openFileInput("myImage"))


        val photoBitmap = BitmapPhoto(bitmap, 0)
        plant_photo.setImageDrawable(BitmapDrawable(resources, photoBitmap.bitmap))

        if(intentFrom == "UploadPhoto") {

            retakephoto.visibility = View.GONE

            uploadAnotherPhoto.setOnClickListener {
                finish()
            }

        }else if(intentFrom == "TakePhotoActivity"){

            uploadAnotherPhoto.visibility = View.GONE

            retakephoto.setOnClickListener {
                finish()
            }

        }

        seeresult.setOnClickListener {
            Toast.makeText(this, "See result pressed!", Toast.LENGTH_SHORT).show()
            progressBar.visibility = View.VISIBLE
            val intent = Intent(this, DataVisualisationActivity::class.java)

            /// TODO: Make threads stop when the activity is exited on back button press sau retake photo/ upload another photo ( Robert Zahariea )
            val t1 = Thread {
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
                        Toast.makeText(this, "Try to make another picture", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            t1.start()

// TODO: Repair this (the ocr from plat to TextAPI) ( Cosim Aftanase )
//            val t2 = Thread {
//                val plantName = ocrFunction((photoBitmap))
//                println("plantname finished")
//                val res = wikiapi(plantName)
//                println("wikiapi finished")
//                if (res != null) {
//                    intent.putExtra("description", res["description"])
//                    intent.putExtra("table", res["table"])
//                    intent.putExtra("latinName", plantName)
//                    intent.putExtra("photoUrl", res["image"])
//                    if (!done) {
//                        done = true
//                        startActivity(intent)
//                    }
//                } else {
//                    if (!failed) {
//                        failed = true
//                    } else {
//                        println("something happened")
//                        Toast.makeText(this, "Try o make another pcture", Toast.LENGTH_SHORT).show()
//                    }
//                }
//            }
//           t2.start()
        }
    }

    override fun onResume() {
        super.onResume()
        /// TODO: De verificat sa se intoarka in photo taken sau in home ( Robert Zahariea )
    }

}

