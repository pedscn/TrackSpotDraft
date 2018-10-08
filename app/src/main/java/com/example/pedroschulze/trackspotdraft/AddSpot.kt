package com.example.pedroschulze.trackspotdraft

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity

import kotlinx.android.synthetic.main.activity_add_spot.*
import java.io.File.separator
import android.os.Environment.getExternalStorageDirectory
import android.graphics.BitmapFactory
import android.graphics.Bitmap
import android.os.Environment
import android.util.Log
import android.widget.Button
import java.io.File


class AddSpot : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_spot)

        val pathname:String = intent.getStringExtra("imgpath")
        Log.e("FULL PATH ", pathname)

        val bmp = BitmapFactory.decodeFile(pathname)
        spotimg.setImageBitmap(bmp)

        val btn_confirm_spot = findViewById<Button>(R.id.btn_confirm_spot) as Button //Required some googling due to sdk version not compatible with inference.
        btn_confirm_spot.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}
