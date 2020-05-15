package com.example.plantapp

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.graphics.drawable.BitmapDrawable
import android.media.ExifInterface
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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
        SeeResult.isClickable = true
        progressBar.visibility = View.GONE

        val bitmap = BitmapFactory.decodeStream(this.openFileInput("myImage"))


        val photoBitmap = BitmapPhoto(bitmap, 0)
        plant_photo.setImageDrawable(BitmapDrawable(resources, photoBitmap.bitmap))

        if(intentFrom == "UploadPhoto") {

            retakephoto.visibility = View.GONE

            uploadAnotherPhoto.setOnClickListener {
                // TODO: Make this go to the gallery instead of the home activity ( Alexandra Ciocoiu )
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if(ContextCompat.checkSelfPermission(this,
                            android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                        //permission denied
                        ActivityCompat.requestPermissions(this,
                            arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                            HomeActivity.PERMISSION_CODE
                        )
                    }
                    else {
                        //permission already granted
                        pickImageFromGallery()
                    }
                }
                else {
                    //system OS is < Marshmallow
                    pickImageFromGallery()
                }
            }

        }else if(intentFrom == "TakePhotoActivity"){

            uploadAnotherPhoto.visibility = View.GONE

            retakephoto.setOnClickListener {
                finish()
            }

        }

        SeeResult.setOnClickListener {
            Toast.makeText(this, "See result pressed!", Toast.LENGTH_SHORT).show()
            progressBar.visibility = View.VISIBLE
            val intent = Intent(this, DataVisualisationActivity::class.java)

            /// TODO: Make threads stop when the activity is exited on back button press sau retake photo/ upload another photo ( Robert Zahariea )
            /// TODO: Replace the threads with the afferent tasks ( Cosmin Aftanase )
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

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, HomeActivity.IMAGE_PICK_CODE)
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            HomeActivity.PERMISSION_CODE -> {
                if (grantResults.size > 0 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED) {
                    pickImageFromGallery()
                }
                else {
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }

        }
    }



