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
import java.io.File

class AddSpot : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_spot)

        val pathname:String = intent.getStringExtra("imgpath")
        val limb = intent.getStringExtra("limb")
        val imgname = intent.getStringExtra("imgname")
        val directorypath = intent.getStringExtra("directorypath")
        Log.e("preconfirm pathname: ", pathname)

        val bmp = BitmapFactory.decodeFile(pathname)
        spotimg.setImageBitmap(bmp)

        val btn_confirm_spot = findViewById<Button>(R.id.btn_confirm_spot) as Button //Required some googling due to sdk version not compatible with inference.
        btn_confirm_spot.setOnClickListener {
            val editText = findViewById<EditText>(R.id.spot_name) as EditText
            val editname = editText.text.toString()
            galleryAddPic(directorypath, imgname, editname, limb)
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    fun galleryAddPic(pathname: String, imgname: String, editname: String, limb: String) {
        Log.e("BodyScreen", "Adding pic to gallery")
        Log.e("mCurrentPhoroPath: ", pathname)
        Log.e("mCurrentPhoroPath: ", imgname)
        Log.e("mCurrentPhoroPath: ", editname)
        val newdir = "$pathname/$editname/".replace("pics/", limb)
        Log.e("newdir: ", newdir)
        File(newdir).mkdirs()
        Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE).also { mediaScanIntent ->
            val f = File(pathname + imgname + limb)
            Log.e("value f: ", f.toString())
            mediaScanIntent.data = Uri.fromFile(f)
            sendBroadcast(mediaScanIntent)
            Log.e("BodyScreen", "Done")
        }
    }
}
