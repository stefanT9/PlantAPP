package com.example.plantapp

import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import io.fotoapparat.result.BitmapPhoto
import kotlinx.android.synthetic.main.activity_photo_taken.*
import kotlinx.android.synthetic.main.activity_top_nav.*
import plantToTextAPI.GetPlantNameTask
import plantToTextAPI.OcrTask
import plantToTextAPI.OnTaskEventListener
import wikiapi.wikiapi

@Volatile
var done: Boolean = false

@Volatile
var failed: Boolean = false

abstract class PhotoTakenActivity : TopNavViewActivity() {
    abstract var plantNameTask: AsyncTask<BitmapPhoto, Int?, String?>
    abstract var ocrTask: AsyncTask<BitmapPhoto, Int?, String?>
    private var failNumber : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        done = false
        failed = false

        val intentFrom: String? = intent.getStringExtra("From")


        this.layoutInflater.inflate(R.layout.activity_photo_taken, mainLayout)
        seeresult.isClickable = true
        progressBar.visibility = View.GONE

        val bitmap = BitmapFactory.decodeStream(this.openFileInput("myImage"))


        val photoBitmap = BitmapPhoto(bitmap, 0)
        plant_photo.setImageDrawable(BitmapDrawable(resources, photoBitmap.bitmap))

        if (intentFrom == "UploadPhoto") {

            retakephoto.visibility = View.GONE

            uploadAnotherPhoto.setOnClickListener {
                // TODO: Make this go to the gallery instead of the home activity ( Alexandra Ciocoiu )
                finish()
            }

        } else if (intentFrom == "TakePhotoActivity") {

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
            failNumber = 0

            plantNameTask = GetPlantNameTask(object : OnTaskEventListener<String> {
                override fun onSuccess(result: String) {
                    ocrTask.cancel(true)
                    successFunction(result)
                }

                override fun onFailure(e: Exception?) {
                    failureFunction(Exception("Plant api didn't find any plant"))
                }
            }).execute(photoBitmap)

            ocrTask = OcrTask(object : OnTaskEventListener<String> {
                override fun onSuccess(result: String) {
                    plantNameTask.cancel(true)
                    successFunction(result)
                }

                override fun onFailure(e: Exception?) {
                   failureFunction(Exception("Ocr didn't find any text"))
                }
            }).execute(photoBitmap)

        }
    }


    fun successFunction(plantName : String){
        //Call wikiapi and move to PlantDetailsActivity
        val res = wikiapi(plantName)
        if (res != null) {
            println("wikiapi finished")
            intent.putExtra("description", res["description"])
            intent.putExtra("table", res["table"])
            intent.putExtra("latinName", plantName)
            intent.putExtra("photoUrl", res["image"])
            startActivity(intent)
        }
        //Useless path?
        else {
            println("Something went wrong with wikiapi")
            Log.d("wikiapi", "Something went wrong with wikiapi")
            Toast.makeText(this, "Try to take another picture", Toast.LENGTH_SHORT).show()
        }
    }

    fun failureFunction(e : Exception){
        //If both tasks have failed, ask user for another photo
        failNumber++
        if (failNumber == 2)
            Toast.makeText(this, "Try to take another picture", Toast.LENGTH_SHORT).show()
        println(e)
    }

    override fun onResume() {
        super.onResume()
        /// TODO: De verificat sa se intoarka in photo taken sau in home ( Robert Zahariea )
    }

}

