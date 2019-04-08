package com.dev.pedscn.trackyourspot

import android.content.ClipData
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import android.support.v7.app.AppCompatActivity
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


abstract class CameraOpeningActivity : AppCompatActivity() {
    lateinit var currentFullPhotoPath: String //Probably bad design, could instead return path with createImage file or dispatchTPI?

    private fun createImageFile(spotDirectory: String): File {
        //Used for creating a unique name for the image file
        val timeStamp: String = SimpleDateFormat("dd-MM-yyyy_HHmmss", Locale.getDefault()).format(Date()) //Better way?
        //If needed, create the folder for the spot.
        val newDir = File(spotDirectory)
        if (!newDir.exists()) newDir.mkdirs()
        //Create a temporary image file
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            newDir
        ).apply {
            //Saves the image path in String format for later use.
            currentFullPhotoPath = absolutePath
        }
    }

    fun dispatchTakePictureIntent(spotDirectory: String) {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(packageManager)?.also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    createImageFile(spotDirectory)
                } catch (ex: IOException) {
                    null
                }
                // Continue only if the File was successfully created
                photoFile?.also {
                    //Get the image file's URI
                    val photoURI: Uri = FileProvider.getUriForFile(
                        this,
                        "my.package.name.provider",
                        it
                    )
                    //For compatibility issues, we need to check the device's
                    //Android version and add write permissions accordingly.
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        takePictureIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                    } else {
                        val clip = ClipData.newUri(contentResolver, "clipData", photoURI)
                        takePictureIntent.clipData = clip
                        takePictureIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                    }
                    //Open Camera App
                    takePictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, OldSpotListActivity.REQUEST_TAKE_PHOTO)
                }
            }
        }
    }

    fun isValidPhotoPath(photoPath: String): Boolean {
        return photoPath.contains("JPEG")
    }
}