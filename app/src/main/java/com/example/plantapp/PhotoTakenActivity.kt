package com.example.plantapp

import Util.getLastLocation
import Util.initializeLocationData
import Util.isLocationEnabled
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import com.google.android.gms.location.LocationServices
import io.fotoapparat.result.BitmapPhoto
import kotlinx.android.synthetic.main.activity_photo_taken.*
import kotlinx.android.synthetic.main.activity_top_nav.*
import plantToTextAPI.OcrTask
import plantToTextAPI.OnTaskEventListener
import plantToTextAPI.PlantTask1
import plantToTextAPI.PlantTask2
import wikiapi.WikiapiTask
import java.util.*

class PhotoTakenActivity : TopNavViewActivity() {
    private var seeResultsTimes: Int = 0

    private lateinit var plantTask1: AsyncTask<BitmapPhoto, Int?, String?>
    private lateinit var plantTask2: AsyncTask<BitmapPhoto, Int?, String?>
    private lateinit var ocrTask: AsyncTask<BitmapPhoto, Int?, String?>

    private var failNumber : Int = 0
    private var numberOfTasks : Int = 3

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val intentFrom: String? = intent.getStringExtra("From")

        this.layoutInflater.inflate(R.layout.activity_photo_taken, mainLayout)
        seeresult.isClickable = true
        this.window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        progressBar.visibility = View.GONE
        foto.visibility = View.GONE
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

        seeResultsTimes = 0

        seeresult.setOnClickListener {
            initializeLocationData(this, this, LocationServices.getFusedLocationProviderClient(this))

            //Check if location is activated
            if (!isLocationEnabled())
            {
                Toast.makeText(this, "Please turn location on before!", Toast.LENGTH_SHORT).show()
            }

            //Check if it was pressed only once
            else if (seeResultsTimes > 0)
            {
                Toast.makeText(this, "Please wait while image is being processed", Toast.LENGTH_LONG).show()
            }

            else {
            seeResultsTimes++
            Toast.makeText(this, "See result pressed!", Toast.LENGTH_SHORT).show()
            progressBar.visibility = View.VISIBLE
            var intent = Intent(this, DataVisualisationActivity::class.java)

            getLastLocation()
            failNumber = 0

            plantTask1 = PlantTask1(object : OnTaskEventListener<String> {
                override fun onSuccess(result: String) {
                    cancelOtherTasks(plantTask1)
                    successFunction(result, intent)
                }

                override fun onFailure(e: Exception?) {
                    failureFunction(Exception("Plant api 1 didn't find any plant"))
                }
            }).execute(photoBitmap)

            plantTask2 = PlantTask2(object : OnTaskEventListener<String> {
                override fun onSuccess(result: String) {
                    cancelOtherTasks(plantTask2)
                    successFunction(result, intent)
                }

                override fun onFailure(e: Exception?) {
                    failureFunction(Exception("Plant api 2 didn't find any plant"))
                }
            }).execute(photoBitmap)

            ocrTask = OcrTask(object : OnTaskEventListener<String> {
                override fun onSuccess(result: String) {
                    cancelOtherTasks(ocrTask)
                    successFunction(result, intent)
                }

                override fun onFailure(e: Exception?) {
                   failureFunction(Exception("Ocr didn't find any text"))
                }
            }).execute(photoBitmap)
            }
        }
    }


    fun successFunction(plantName: String, intent: Intent){
        val context = this;
        WikiapiTask (object : OnTaskEventListener<Hashtable<String, String>>{
            override fun onSuccess(res: Hashtable<String, String>) {
                intent.putExtra("description", res["description"])
                intent.putExtra("table", res["table"])
                intent.putExtra("latinName", plantName)
                intent.putExtra("photoUrl", res["image"])
                startActivity(intent)
            }
            override fun onFailure(e: Exception?) {
                println("Something went wrong with wikiapi")
                failNumber++
                Toast.makeText(context, "Try to take another picture", Toast.LENGTH_LONG).show()
                progressBar.visibility = View.GONE
            }
        }).execute(plantName)
    }

    fun failureFunction(e : Exception){
        //If all tasks have failed, ask user for another photo
        failNumber++
        if (failNumber == numberOfTasks) {
            Toast.makeText(this, "Try to take another picture", Toast.LENGTH_LONG).show()
            progressBar.visibility = View.GONE
        }
        println(e)
    }

    fun cancelOtherTasks(thisTask: AsyncTask<BitmapPhoto, Int?, String?>?){
        Log.d("cancelOtherTasks", "cancelling the tasks")
        var taskList = listOf(plantTask1, plantTask2, ocrTask)
        for (task in taskList)
        {
            if (task == thisTask)
                continue;
            if (!task.isCancelled)
                task.cancel(true)
        }
    }

    override fun onPause() {
        super.onPause()
        progressBar.visibility = View.GONE
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == 200) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getLastLocation()
            }
        }
    }
}

