package com.example.plantapp

import android.Manifest
import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.plantapp.HomeActivity.Companion.PERMISSION_CODE
import io.fotoapparat.Fotoapparat
import io.fotoapparat.log.fileLogger
import io.fotoapparat.log.logcat
import io.fotoapparat.log.loggers
import io.fotoapparat.parameter.ScaleType
import io.fotoapparat.selector.back
import kotlinx.android.synthetic.main.activity_take_photo.*
import java.io.ByteArrayOutputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException


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

        gallery_button.setOnClickListener {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if(ContextCompat.checkSelfPermission(this,
                        android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                    //permission denied
                    ActivityCompat.requestPermissions(this,
                        arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), PERMISSION_CODE)
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

        photo_button.setOnClickListener {
            fotoapparat!!.takePicture().toBitmap().whenAvailable {
                if (it != null) {
                    val intent=Intent(this,PhotoTakenActivity::class.java)
                    intent.putExtra("From", "TakePhotoActivity")

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
            PERMISSION_CODE -> {
                if (grantResults.size > 0 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED) {
                    pickImageFromGallery()
                }
                else {
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
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
        return fileName;
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK &&
            requestCode == HomeActivity.IMAGE_PICK_CODE &&
            data != null) {
            val intent = Intent(this, PhotoTakenActivity::class.java)
            intent.putExtra("From", "UploadPhoto")

            val uriImage = data.data

            val bitmap = getBitmap(uriImage, this.contentResolver)
            if (bitmap != null) {
                createImageFromBitmap(bitmap)
            }

            startActivity(intent)
        }
    }

    fun getBitmap(file: Uri?, cr: ContentResolver): Bitmap?{
        var bitmap: Bitmap ?= null
        try {
            val inputStream = file?.let { cr.openInputStream(it) }
            bitmap = BitmapFactory.decodeStream(inputStream)
            // close stream
            try {
                if (inputStream != null) {
                    inputStream.close()
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }catch (e: FileNotFoundException){}
        return bitmap
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