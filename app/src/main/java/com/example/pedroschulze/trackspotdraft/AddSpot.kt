package com.example.pedroschulze.trackspotdraft

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_add_spot.*
import android.net.Uri
import android.util.Log
import android.widget.Button
import android.widget.EditText
import com.bumptech.glide.Glide
import java.io.File

class AddSpot : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_spot)

        val fullPhotoPath = intent.getStringExtra("fullPhotoPath")
        val selectedBodySide = intent.getStringExtra("selectedBodySide")
        val selectedBodyPart = intent.getStringExtra("selectedBodyPart")
        val spotImageName = intent.getStringExtra("spotImageName")

        Glide.with(this@AddSpot)
                .load(File(fullPhotoPath))
                .into(spot_image)

        val btnConfirmSpot = findViewById<Button>(R.id.btn_confirm_spot) as Button //SDK version not compatible with inference.
        btnConfirmSpot.setOnClickListener {
            val editText = findViewById<EditText>(R.id.spot_name) as EditText
            val editName = editText.text.toString()

            galleryAddPic(fullPhotoPath, spotImageName, editName)
            val intent = Intent(this, OldSpotScreen::class.java)
            intent.putExtra("selectedBodyPart", selectedBodyPart)
            intent.putExtra("selectedBodySide", selectedBodySide)
            startActivity(intent)
        }
    }

    private fun deleteRecursive(fileOrDirectory: File) {
        if (fileOrDirectory.isDirectory)
            for (child in fileOrDirectory.listFiles()!!)
                deleteRecursive(child)
        fileOrDirectory.delete()
    }

    //Is the galleryAddPic method even needed???? Apart from renaming
    private fun galleryAddPic(fullPhotoPath: String, spotImageName: String, editName: String) { //Need better way of dealing with temps
        val photoDirectory = fullPhotoPath.removeSuffix(spotImageName)
        val newDirPath = photoDirectory.replace("temp", editName)
        val newDir = File(newDirPath)
        if(!newDir.exists()) newDir.mkdirs()
        Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE).also { mediaScanIntent ->
            val f = File(fullPhotoPath)
            mediaScanIntent.data = Uri.fromFile(f)
            sendBroadcast(mediaScanIntent)
            f.renameTo(File(newDirPath + spotImageName))
            val tempFolder = File(photoDirectory)
            deleteRecursive(tempFolder)
        }
    }
}
