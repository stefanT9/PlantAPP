package com.example.plantapp

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.core.content.FileProvider
import kotlinx.android.synthetic.main.activity_take_photo.*
import java.io.File
import java.io.IOException
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.Date


class TakePhotoActivity : AppCompatActivity() {

    private val PERMISSION_CODE: Int = 1001
    val REQUEST_IMAGE_CAPTURE = 100
    var photoFile: File? = null
    lateinit var currentPhotoPath: String
    var photoURI: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_take_photo)
    }

    fun takePhotoTapped(view: View) {
        //if system os is Marshmallow or Above, we need to request runtime permission
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED ||
                checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_DENIED
            ) {
                //permission was not enabled
                val permission = arrayOf(android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                //show popup to request permission
                requestPermissions(permission, PERMISSION_CODE)
            } else {
                //permission already granted
                openCamera()
            }
        } else {
            //system os is < marshmallow
            openCamera()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode) {
            PERMISSION_CODE -> {
                if(grantResults.size > 0 &&  grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //permission from popup was granted
                    openCamera()
                } else {
                    //permission from popup was denied
                    Toast.makeText(this, "Permission denies", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun getCameraPhotoOrientation(context: Context, imageUri: Uri, imagePath: String): Float {
        var rotate: Float = 0F
        try {
            context.contentResolver.notifyChange(imageUri, null)
            val imageFile = File(imagePath)
            val exif = ExifInterface(imageFile.absolutePath)
            val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)

            when(orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> rotate = 90F
                ExifInterface.ORIENTATION_ROTATE_180 -> rotate = 180F
                ExifInterface.ORIENTATION_ROTATE_270 -> rotate = 270F
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return rotate
    }

    private fun openCamera() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                photoFile = try {
                    createImageFile()
                } catch(ex: IOException) {
                    Toast.makeText(applicationContext, "Error occurred while creating the File", Toast.LENGTH_SHORT).show()
                    ex.stackTrace
                    return
                }
                photoFile?.also { file ->
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        photoURI = FileProvider.getUriForFile(this, "com.example.plantapp.fileprovider", file)
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                    } else {
                        photoURI = Uri.fromFile(file)
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                    }
                }
            }
        }
    }

    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile("JPEG_${timeStamp}_" , ".jpg" , storageDir).apply {
            currentPhotoPath = absolutePath
        }
    }

    @SuppressLint("MissingSuperCall")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == REQUEST_IMAGE_CAPTURE) {
            if(resultCode == RESULT_OK) {
                imageView.setImageURI(photoURI)
                imageView.rotation = getCameraPhotoOrientation(this, photoURI!!, photoFile!!.absolutePath)
                textView.visibility = View.INVISIBLE
            }
        }
    }
}

