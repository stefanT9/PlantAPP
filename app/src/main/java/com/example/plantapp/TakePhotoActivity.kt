package com.example.plantapp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
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

const val GET_FROM_GALLERY = 3

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

        //TODO: extract photo and send it to photo taken activity
        gallery_button.setOnClickListener {
            startActivityForResult(
                Intent(
                    Intent.ACTION_PICK,
                    MediaStore.Images.Media.INTERNAL_CONTENT_URI
                ), GET_FROM_GALLERY
            )
        }

        photo_button.setOnClickListener {
            fotoapparat!!.takePicture().toBitmap().whenAvailable {
                if (it != null) {
                    val intent = Intent(this, DataVisualisationActivity::class.java)
                    Thread {
                        val plantName= getPlantName((it))
                        println("plantname finished")
                        val res = wikiapi(plantName)
                        println("wikiapi finished")
                        if (res != null) {
                            intent.putExtra("description", res["description"])
                            intent.putExtra("table", res["table"])
                            intent.putExtra("latinName", plantName)
                            intent.putExtra("photoUrl",res["image"])
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