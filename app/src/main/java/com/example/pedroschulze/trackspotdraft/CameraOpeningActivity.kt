package com.example.pedroschulze.trackspotdraft

import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import android.support.v7.app.AppCompatActivity
import android.util.Log
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by pedroschulze on 28/01/2019.
 */
abstract class CameraOpeningActivity : AppCompatActivity() {

    var mCurrentPhotoPath = ""

    @Throws(IOException::class)
    fun createImageFile(spotDirectory: String): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        val newDir = File(spotDirectory)
        if(!newDir.exists()) newDir.mkdirs()
        return File.createTempFile(
                "JPEG_${timeStamp}_", /* prefix */
                ".jpg", /* suffix */
                newDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            mCurrentPhotoPath = absolutePath
        }
    }

    fun dispatchTakePictureIntent(spotDirectory: String) {
        Log.e("BodyScreen", "dispatchTakePictureIntent")
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also {
            takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(packageManager)?.also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    createImageFile(spotDirectory)
                } catch (ex: IOException) {
                    // Error occurred while creating the File
                    Log.e("BodyScreen", "IOException")
                    null
                }
                // Continue only if the File was successfully created
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                            this,
                            "my.package.name.provider",
                            it
                    )
                    Log.e("BodyScreen", "startingIntent")
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, OldSpotScreen.REQUEST_TAKE_PHOTO)
                }
            }
        }
    }
}