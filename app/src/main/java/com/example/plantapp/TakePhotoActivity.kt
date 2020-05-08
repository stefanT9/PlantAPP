package com.example.plantapp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import io.fotoapparat.Fotoapparat
import io.fotoapparat.log.fileLogger
import io.fotoapparat.log.logcat
import io.fotoapparat.log.loggers
import io.fotoapparat.parameter.ScaleType
import io.fotoapparat.selector.back
import kotlinx.android.synthetic.main.activity_take_photo.*
import plantToTextAPI.getPlantName
import wikiapi.wikiapi

class TakePhotoActivity : AppCompatActivity() {

    private var fotoapparat: Fotoapparat? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_take_photo)

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_DENIED
        ) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 222)
        } else {
            initFotoapparat()
        }

        gallery_button.setOnClickListener {
            Toast.makeText(this, "Enter Gallery", Toast.LENGTH_LONG).show()
        }

        photo_button.setOnClickListener {
            fotoapparat!!.takePicture().toBitmap().whenAvailable {
                if (it != null) {
                    val intent = Intent(this, DataVisualisationActivity::class.java)
                    Thread {
                        val plantName= getPlantName((it))
                        val res = wikiapi(plantName)
                        if (res != null) {
                            intent.putExtra("description", res["description"])
                            intent.putExtra("table", res["table"])
                            intent.putExtra("latinName", plantName)
                            startActivity(intent)
                        } else {
                            println("something happened")
                            Toast.makeText(this,"Try o make another pcture",Toast.LENGTH_SHORT).show()
                        }
                    }.start()
                }
            }
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 222 && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
            initFotoapparat()
            fotoapparat?.start()
        } else {
            Toast.makeText(
                this,
                "Permission for camera needed for using PlantApp",
                Toast.LENGTH_LONG
            ).show()
            finish()
        }
    }

    private fun initFotoapparat() {
        fotoapparat = Fotoapparat(
            context = this,
            view = camera_view,
            scaleType = ScaleType.CenterCrop,
            lensPosition = back(),
            logger = loggers(
                logcat(),
                fileLogger(this)
            ),
            cameraErrorCallback = { error -> throw error }
        )
    }

    override fun onStart() {
        super.onStart()
        fotoapparat?.start()
    }

    override fun onStop() {
        super.onStop()
        fotoapparat?.stop()
    }
}