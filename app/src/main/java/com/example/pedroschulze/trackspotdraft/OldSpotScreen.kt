package com.example.pedroschulze.trackspotdraft

import android.media.Image
import android.os.Bundle
import android.support.design.widget.CoordinatorLayout
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.ListView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.view.ViewGroup
import java.io.File
import android.graphics.BitmapFactory
import android.graphics.Bitmap
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy


class OldSpotScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_old_spot_screen)
        val limb = intent.getStringExtra("limb")
        var spotNames = emptyArray<String>()
        var spotDetails = emptyArray<String>()
        var spotDesc = emptyArray<String>()
        var thumbnails = emptyArray<Bitmap>()
        File("/storage/emulated/0/Pictures/trackspot/$limb/").walk().maxDepth(1).forEachIndexed { index, file ->
            Log.e("PATH", file.toString())
            if (index != 0) {
                val name = file.toString().removePrefix("/storage/emulated/0/Pictures/trackspot/$limb/")
                val imgFile =  File("/storage/emulated/0/Pictures/trackspot/$limb/$name/").walk().maxDepth(1).toList()[1]
                Log.e("imgfile", imgFile.toString())
                spotNames += name
                spotDesc += "/Pictures/trackspot/$limb/$name/"
                spotDetails += imgFile.toString().removePrefix("/storage/emulated/0/Pictures/trackspot/$limb/$name/")

                thumbnails += BitmapFactory.decodeFile(imgFile.absolutePath)
                //val name = nameAndImage.split("/")[0]
                //val image = nameAndImage.split("/")[1]
                Log.e("name", name)
                //Log.e("image", image)
                    //val imgFile = File(file.toString())
                    //val myBitmap = BitmapFactory.decodeFile(imgFile.absolutePath)
                    //thumbnails += myBitmap
                }
            }



        val adapter1 = object : ArrayAdapter<String>(this, R.layout.list_item, R.id.title, spotNames) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getView(position, convertView, parent)
                val text1 = view.findViewById<View>(R.id.title) as TextView
                val text2 = view.findViewById<View>(R.id.seconddesc) as TextView
                val text3 = view.findViewById<View>(R.id.artist) as TextView
                val view1 = view.findViewById<ImageView>(R.id.thumbn) as ImageView

                text1.text = spotNames[position]
                text2.text = spotDetails[position]
                text3.text = spotDesc[position]
                Glide.with(this@OldSpotScreen)
                        .load(thumbnails[position])
                        .into(view1)
                return view
            }
        }

        val listView = findViewById<ListView>(R.id.sampleListView)
        listView.adapter = adapter1 //Setting adapter and listener to start song level when clicked.
    }

}
