package com.example.pedroschulze.trackspotdraft

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_add_spot.*
import android.graphics.BitmapFactory
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

        val pathname:String = intent.getStringExtra("imgpath")
        val limb = intent.getStringExtra("limb")
        val imgname = pathname.removePrefix("/storage/emulated/0/Pictures/trackspot/$limb/temp/")
        val directorypath = intent.getStringExtra("directorypath")
        Log.e("preconfirm pathname: ", pathname)

        //val bmp = BitmapFactory.decodeFile(pathname)
        //spotimg.setImageBitmap(bmp)

        Glide.with(this@AddSpot)
                .load(File(pathname)) // Uri of the picture
                .into(spotimg)

        val btnConfirmSpot = findViewById<Button>(R.id.btn_confirm_spot) as Button //Required some googling due to sdk version not compatible with inference.
        btnConfirmSpot.setOnClickListener {
            val editText = findViewById<EditText>(R.id.spot_name) as EditText
            val editname = editText.text.toString()
            Log.e("directorypath", directorypath)
            Log.e("imgname", imgname)
            Log.e("editname", editname)

            galleryAddPic(directorypath, imgname, editname, limb)
            val intent = Intent(this, OldSpotScreen::class.java)
            intent.putExtra("limb", limb)
            startActivity(intent)
        }
    }

    fun deleteRecursive(fileOrDirectory: File) {
        if (fileOrDirectory.isDirectory)
            for (child in fileOrDirectory.listFiles()!!)
                deleteRecursive(child)

        fileOrDirectory.delete()
    }

    fun galleryAddPic(pathname: String, imgname: String, editname: String, limb: String) {
        Log.e("BodyScreen", "Adding pic to gallery")
        Log.e("mCurrentPhoroPath: ", pathname)
        Log.e("mCurrentPhoroPath: ", imgname)
        Log.e("mCurrentPhoroPath: ", editname)
        val newDirPath = "$pathname".replace("temp", editname)
        val newDir = File(newDirPath)
        Log.e("newdir: ", newDirPath)

        if(!newDir.exists()) newDir.mkdirs()

        Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE).also { mediaScanIntent ->
            val f = File(pathname + imgname)
            Log.e("value f: ", f.toString())
            mediaScanIntent.data = Uri.fromFile(f)
            sendBroadcast(mediaScanIntent)
            f.renameTo(File(newDirPath + imgname))
            val tempFolder = File(pathname)
            deleteRecursive(tempFolder)

            Log.e("BodyScreen", "Done")
        }
    }
}
