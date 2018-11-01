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
    companion object {
        val REQUEST_IMAGE_CAPTURE = 1
        val REQUEST_TAKE_PHOTO = 1
    }
    private var mCurrentPhotoPath: String = ""
    private var limb = ""
    var directorypath = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_spot_screen)
        Log.e("BodyScreen", "onCreate")
        val mainMenuAction = intent.getStringExtra("MainMenuAction")
        createMainMenu(mainMenuAction)

    }

    @Throws(IOException::class)
    fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File = getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        directorypath = storageDir.absolutePath + "/trackspot/temp/"
        val newDir = File(directorypath)
        if(!newDir.exists()) newDir.mkdirs()

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

    private fun createMainMenu(mainMenuAction: String?) {
        val btnLeftArm = findViewById<Button>(R.id.btn_left_arm) as Button //Required some googling due to sdk version not compatible with inference.
        btnLeftArm.setOnClickListener {
            limb="leftarm"
            when {
                mainMenuAction.equals( "Newspot") -> dispatchTakePictureIntent()
                mainMenuAction.equals("Oldspot") -> {
                    val intent = Intent(this, OldSpotScreen::class.java)
                    intent.putExtra("limb", limb)
                    intent.putExtra("option", "oldspot")
                    startActivity(intent)
                }
                mainMenuAction.equals("Viewspot") -> {
                    val intent = Intent(this, TrackSpotScreen::class.java)
                    intent.putExtra("option", "viewspot")
                    intent.putExtra("limb", limb)
                }
            }
        }
        val btnRightArm = findViewById<Button>(R.id.btn_right_arm) as Button //Required some googling due to sdk version not compatible with inference.
        btnRightArm.setOnClickListener {
            limb="rightarm"
            when {
                mainMenuAction.equals( "Newspot") -> dispatchTakePictureIntent()
                mainMenuAction.equals("Oldspot") -> {
                    val intent = Intent(this, OldSpotScreen::class.java)
                    intent.putExtra("limb", limb)
                    intent.putExtra("option", "oldspot")
                    startActivity(intent)
                }
                mainMenuAction.equals("Viewspot") -> {
                    val intent = Intent(this, TrackSpotScreen::class.java)
                    intent.putExtra("option", "viewspot")
                    intent.putExtra("limb", limb)
                }
            }
        }
        val btnRightLeg = findViewById<Button>(R.id.btn_right_leg) as Button //Required some googling due to sdk version not compatible with inference.
        btnRightLeg.setOnClickListener {
            limb="rightleg"
            when {
                mainMenuAction.equals( "Newspot") -> dispatchTakePictureIntent()
                mainMenuAction.equals("Oldspot") -> {
                    val intent = Intent(this, OldSpotScreen::class.java)
                    intent.putExtra("limb", limb)
                    intent.putExtra("option", "oldspot")
                    startActivity(intent)
                }
                mainMenuAction.equals("Viewspot") -> {
                    val intent = Intent(this, TrackSpotScreen::class.java)
                    intent.putExtra("option", "viewspot")
                    intent.putExtra("limb", limb)
                }
            }
        }
        val btnLeftLeg = findViewById<Button>(R.id.btn_left_leg) as Button //Required some googling due to sdk version not compatible with inference.
        btnLeftLeg.setOnClickListener {
            limb="leftleg"
            when {
                mainMenuAction.equals( "Newspot") -> dispatchTakePictureIntent()
                mainMenuAction.equals("Oldspot") -> {
                    val intent = Intent(this, OldSpotScreen::class.java)
                    intent.putExtra("limb", limb)
                    intent.putExtra("option", "oldspot")
                    startActivity(intent)
                }
                mainMenuAction.equals("Viewspot") -> {
                    val intent = Intent(this, TrackSpotScreen::class.java)
                    intent.putExtra("option", "viewspot")
                    intent.putExtra("limb", limb)
                }
            }
        }
        val btnTorso = findViewById<Button>(R.id.btn_torso) as Button //Required some googling due to sdk version not compatible with inference.
        btnTorso.setOnClickListener {
            limb="torso"
            when {
                mainMenuAction.equals( "Newspot") -> dispatchTakePictureIntent()
                mainMenuAction.equals("Oldspot") -> {
                    val intent = Intent(this, OldSpotScreen::class.java)
                    intent.putExtra("limb", limb)
                    intent.putExtra("option", "oldspot")
                    startActivity(intent)
                }
                mainMenuAction.equals("Viewspot") -> {
                    val intent = Intent(this, TrackSpotScreen::class.java)
                    intent.putExtra("option", "viewspot")
                    intent.putExtra("limb", limb)
                }
            }
        }
        val btnHead = findViewById<Button>(R.id.btn_head) as Button //Required some googling due to sdk version not compatible with inference.
        btnHead.setOnClickListener {
            limb="head"
            when {
                mainMenuAction.equals( "Newspot") -> dispatchTakePictureIntent()
                mainMenuAction.equals("Oldspot") -> {
                    val intent = Intent(this, OldSpotScreen::class.java)
                    intent.putExtra("limb", limb)
                    intent.putExtra("option", "oldspot")
                    startActivity(intent)
                }
                mainMenuAction.equals("Viewspot") -> {
                    val intent = Intent(this, TrackSpotScreen::class.java)
                    intent.putExtra("option", "viewspot")
                    intent.putExtra("limb", limb)
                }
            }
        }
    }
}
