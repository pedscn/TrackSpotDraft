package com.example.pedroschulze.trackspotdraft

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Environment.getExternalStoragePublicDirectory
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Button
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class BodyScreen : AppCompatActivity() {
    val REQUEST_IMAGE_CAPTURE = 1
    val REQUEST_TAKE_PHOTO = 1
    var mCurrentPhotoPath: String = ""
    var limb = ""
    var directorypath = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_spot_screen)
        Log.e("BodyScreen", "onCreate")
        val MainMenuAction = intent.getStringExtra("MainMenuAction")

        val btn_left_arm = findViewById<Button>(R.id.btn_left_arm) as Button //Required some googling due to sdk version not compatible with inference.
        btn_left_arm.setOnClickListener {
            limb="leftarm"
            if (MainMenuAction.equals( "Newspot")) {
                dispatchTakePictureIntent()
            }
            else {
                val intent = Intent(this, OldSpotScreen::class.java)
                intent.putExtra("limb", limb)
                startActivity(intent)
            }
        }
        val btn_right_arm = findViewById<Button>(R.id.btn_right_arm) as Button //Required some googling due to sdk version not compatible with inference.
        btn_right_arm.setOnClickListener {
            limb="rightarm"
            if (MainMenuAction.equals( "Newspot")) {
                dispatchTakePictureIntent()
            }
            else {
                val intent = Intent(this, OldSpotScreen::class.java)
                intent.putExtra("limb", limb)
                startActivity(intent)
            }
        }
        val btn_right_leg = findViewById<Button>(R.id.btn_right_leg) as Button //Required some googling due to sdk version not compatible with inference.
        btn_right_leg.setOnClickListener {
            limb="rightleg"
            if (MainMenuAction.equals( "Newspot")) {
                dispatchTakePictureIntent()
            }
            else {
                val intent = Intent(this, OldSpotScreen::class.java)
                intent.putExtra("limb", limb)
                startActivity(intent)
            }
        }
        val btn_left_leg = findViewById<Button>(R.id.btn_left_leg) as Button //Required some googling due to sdk version not compatible with inference.
        btn_left_leg.setOnClickListener {
            limb="leftleg"
            if (MainMenuAction.equals( "Newspot")) {
                limb="leftleg"
                dispatchTakePictureIntent()
            }
            else {
                val intent = Intent(this, OldSpotScreen::class.java)
                intent.putExtra("limb", limb)
                startActivity(intent)
            }
        }
        val btn_torso = findViewById<Button>(R.id.btn_torso) as Button //Required some googling due to sdk version not compatible with inference.
        btn_torso.setOnClickListener {
            limb="torso"
            if (MainMenuAction.equals( "Newspot")) {
                dispatchTakePictureIntent()
            }
            else {
                val intent = Intent(this, OldSpotScreen::class.java)
                intent.putExtra("limb", limb)
                startActivity(intent)
            }
        }
        val btn_head = findViewById<Button>(R.id.btn_head) as Button //Required some googling due to sdk version not compatible with inference.
        btn_head.setOnClickListener {
            limb="head"
            if (MainMenuAction.equals( "Newspot")) {
                dispatchTakePictureIntent()
            }
            else {
                val intent = Intent(this, OldSpotScreen::class.java)
                intent.putExtra("limb", limb)
                startActivity(intent)
            }
        }
    }

    @Throws(IOException::class)
    fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File = getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        directorypath = storageDir.absolutePath + "/trackspot/pics/"
        val newDir = File(directorypath)
        if(!newDir.exists()) newDir.mkdir()

        return File.createTempFile(
                "JPEG_${timeStamp}_", /* prefix */
                ".jpg", /* suffix */
                newDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            mCurrentPhotoPath = absolutePath
            Log.e("mCurrentPhotoPath IM", mCurrentPhotoPath)
        }
    }

    fun dispatchTakePictureIntent() {
        Log.e("BodyScreen", "dispatchTakePictureIntent")
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(packageManager)?.also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    // Error occurred while creating the File
                    Log.e("BodyScreen", "IOException")
                    null
                }
                // Continue only if the File was successfully created
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                            this,
                            "com.example.pedroschulze.trackspotdraft.android.fileprovider",
                            it
                    )
                    Log.e("BodyScreen", "startingIntent")
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.e("BodyScreen", "onActivityResult")

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            Log.e("BodyScreen", "request code and result code ok")
            //do whatever you need with taken photo using file or fileUri
            //galleryAddPic()
            val intent = Intent(this, AddSpot::class.java)
            intent.putExtra("imgpath", mCurrentPhotoPath)
            intent.putExtra("directorypath", directorypath)
            intent.putExtra("imgname", mCurrentPhotoPath.removePrefix(directorypath))
            intent.putExtra("limb", limb)
            startActivity(intent)
        }
    }
}
