package com.example.plantapp

import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_top_nav.*
import java.io.ByteArrayOutputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException

class HomeActivity : TopNavViewActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        this.layoutInflater.inflate(R.layout.activity_home,mainLayout)

        //foto.visibility = View.GONE


        scanPlant_imgButton.setOnClickListener {
            val intent= Intent(this,TakePhotoActivity::class.java)
//            ScanPlantBtn()\
            Toast.makeText(this@HomeActivity,"Scan plant btn success",Toast.LENGTH_SHORT).show()
            startActivity(intent)

        }


        uploadPhoto_imgButton.setOnClickListener {

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if(ContextCompat.checkSelfPermission(this,
                        android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                    //permission denied
                    Toast.makeText(this@HomeActivity,"Upload photo, permission denied",Toast.LENGTH_SHORT).show()
                    ActivityCompat.requestPermissions(this,
                        arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), PERMISSION_CODE)


                }
                else {
                    //permission already granted
                    Toast.makeText(this@HomeActivity,"Upload photo, permission already granted",Toast.LENGTH_SHORT).show()
                    pickImageFromGallery()


                }
            }
            else {
                //system OS is < Marshmallow
                pickImageFromGallery()
                Toast.makeText(this@HomeActivity,"Upload photo, pick image from gallery",Toast.LENGTH_SHORT).show()

            }
        }
    }

    companion object {
        //image pick code
        public val IMAGE_PICK_CODE = 1000;
        //Permission code
        public val PERMISSION_CODE = 1001;
    }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray) {
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
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK &&
                requestCode == IMAGE_PICK_CODE &&
                    data != null) {
            val intent = Intent(this, PhotoTakenActivity::class.java)
            intent.putExtra("From", "UploadPhoto")

            val uriImage = data.data

            val bitmap = getBitmap(uriImage, this.contentResolver)
            createImageFromBitmap(bitmap)

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

    fun createImageFromBitmap(bitmap: Bitmap?): String? {
        var fileName: String? = "myImage" //no .png or .jpg needed
        try {
            val bytes = ByteArrayOutputStream()
            if (bitmap != null) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
            }
            val fo: FileOutputStream = openFileOutput(fileName, Context.MODE_PRIVATE)
            fo.write(bytes.toByteArray())
            fo.close()

        } catch (e: Exception) {
            e.printStackTrace()
            fileName = null
        }

        return fileName;
    }

}
