package com.example.plantapp

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Matrix
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
import java.io.ByteArrayOutputStream
import java.io.FileOutputStream


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

        // TODO: make this work( Robert Zahariea )
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
                    val intent=Intent(this,PhotoTakenActivity::class.java)

                    val matrix = Matrix()

                    matrix.postRotate(90f)

                    val scaledBitmap =
                        Bitmap.createScaledBitmap(it.bitmap, it.bitmap.width, it.bitmap.height, true)

                    val rotatedBitmap = Bitmap.createBitmap(
                        scaledBitmap,
                        0,
                        0,
                        scaledBitmap.width,
                        scaledBitmap.height,
                        matrix,
                        true
                    )

                    createImageFromBitmap(rotatedBitmap)

                    startActivity(intent)
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
    fun createImageFromBitmap(bitmap: Bitmap): String? {
        var fileName: String? = "myImage" //no .png or .jpg needed
        try {
            val bytes = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
            val fo: FileOutputStream = openFileOutput(fileName, Context.MODE_PRIVATE)
            fo.write(bytes.toByteArray())
            fo.close()

        } catch (e: Exception) {
            e.printStackTrace()
            fileName = null
        }
        //return fileName

 //       var exif  = ExifInterface("")
//         var rot  =  exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
//        Log.d("orietnation", rot.toString());

        return fileName;
    }

    fun rotateImage(source: Bitmap, angle: Float): Bitmap? {
        val matrix = Matrix()
        matrix.postRotate(angle)
        return Bitmap.createBitmap(
            source, 0, 0, source.width, source.height,
            matrix, true
        )
    }



}