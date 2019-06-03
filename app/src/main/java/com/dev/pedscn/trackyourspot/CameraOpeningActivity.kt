package com.dev.pedscn.trackyourspot

import android.content.ClipData
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import android.support.v7.app.AppCompatActivity
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


abstract class CameraOpeningActivity : AppCompatActivity() {
    lateinit var currentFullPhotoPath: String

    private fun createImageFile(spotDirectory: String): File {
        val timeStamp: String =
            SimpleDateFormat("dd-MM-yyyy_HHmmss", Locale.getDefault()).format(Date())
        val newDir = File(spotDirectory)
        if (!newDir.exists()) newDir.mkdirs()
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            newDir
        ).apply {
            currentFullPhotoPath = absolutePath
        }
    }

    fun dispatchTakePictureIntent(spotDirectory: String) {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                val photoFile: File? = try {
                    createImageFile(spotDirectory)
                } catch (ex: IOException) {
                    null
                }
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        this,
                        "my.package.name.provider",
                        it
                    )
                    val clip = ClipData.newUri(contentResolver, "clipData", photoURI)
                    takePictureIntent.clipData = clip
                    takePictureIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                    takePictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(
                        takePictureIntent,
                        OldSpotListActivity.REQUEST_TAKE_PHOTO
                    )
                }
            }
        }
    }

    fun isValidPhotoPath(photoPath: String): Boolean {
        return photoPath.contains("JPEG")
    }
}