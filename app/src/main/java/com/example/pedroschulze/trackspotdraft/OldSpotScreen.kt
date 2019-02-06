package com.example.pedroschulze.trackspotdraft

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import java.io.File
import android.graphics.BitmapFactory
import android.graphics.Bitmap
import android.net.Uri
import com.bumptech.glide.Glide
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import com.yalantis.ucrop.UCrop

class OldSpotScreen : CameraOpeningActivity() { // Reduce redundancy

    companion object {
        val REQUEST_IMAGE_CAPTURE = 1
        val REQUEST_TAKE_PHOTO = 1
    }

    lateinit var selectedBodyPart : String
    lateinit var currentPath : String

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menuitems, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.action_add -> {
            val spotDirectory = "/storage/emulated/0/Pictures/trackspot/$selectedBodyPart/temp/" //Is this stable with every device?
            currentPath = spotDirectory
            dispatchTakePictureIntent(spotDirectory)
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_old_spot_screen)
        setSupportActionBar(findViewById(R.id.my_toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        selectedBodyPart = intent.getStringExtra("selectedBodyPart")
        title = "$selectedBodyPart Spots"
        var spotNames = emptyArray<String>()
        var spotImageName = emptyArray<String>() //JPG only filenames
        var spotPaths = emptyArray<String>() //Path without jpg
        var thumbnails = emptyArray<Bitmap>()
        File("/storage/emulated/0/Pictures/trackspot/$selectedBodyPart/").walk().maxDepth(1).forEachIndexed { index, file ->
            Log.e("PATH", file.toString())
            if (index != 0) {
                val spotName = file.toString().removePrefix("/storage/emulated/0/Pictures/trackspot/$selectedBodyPart/")
                Log.e("imgFile walk", File("/storage/emulated/0/Pictures/trackspot/$selectedBodyPart/$spotName/").walk().maxDepth(1).toList().toString())
                val imgFile =  File("/storage/emulated/0/Pictures/trackspot/$selectedBodyPart/$spotName/").walk().maxDepth(1).toList()[1]
                spotNames += spotName
                spotPaths += "/storage/emulated/0/Pictures/trackspot/$selectedBodyPart/$spotName/"
                spotImageName += imgFile.toString().removePrefix("/storage/emulated/0/Pictures/trackspot/$selectedBodyPart/$spotName/")
                thumbnails += BitmapFactory.decodeFile(imgFile.absolutePath)
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
                text2.text = spotImageName[position]
                text3.text = spotPaths[position].removePrefix("/storage/emulated/0") //Just for cleaner UI purposes
                Glide.with(this@OldSpotScreen)
                        .load(thumbnails[position])
                        .thumbnail( 0.1f ) //Improve memory management
                        .into(view1)
                return view
            }
        }

        val listView = findViewById<ListView>(R.id.sampleListView)
        listView.adapter = adapter1
        listView.onItemClickListener = AdapterView.OnItemClickListener { arg0, arg1, arg2, arg3 ->
            Log.e("arg3", arg3.toString())
            Log.e("spotNames", spotNames[arg2]) //arg2 is position in the list
            Log.e("spotDetails", spotImageName[arg2])
            Log.e("spotDesc", spotPaths[arg2])
            val intent = Intent(this, SpotImageList::class.java)
            intent.putExtra("selectedBodyPart", selectedBodyPart)
            intent.putExtra("spotName", spotNames[arg2])
            intent.putExtra("spotImageName", spotImageName[arg2])
            intent.putExtra("spotPath", spotPaths[arg2])
            startActivity(intent)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.e("BodyScreen", "onActivityResult")

        if (requestCode == UCrop.REQUEST_CROP) {
            val intent = Intent(this, AddSpot::class.java)
            intent.putExtra("selectedBodyPart", selectedBodyPart)
            intent.putExtra("directorypath", currentPath)
            intent.putExtra("imgpath", mCurrentPhotoPath)
            intent.putExtra("imgname", "placeholder")
            startActivity(intent)
        }

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            val cropOptions = UCrop.Options().apply {
                //setActiveWidgetColor(Color.BLUE)
                //setToolbarColor(Color.BLUE)
                setAllowedGestures(0,0,0)
            }

            UCrop.of(Uri.parse("file://"+mCurrentPhotoPath), Uri.parse("file://"+mCurrentPhotoPath)) //same destination as source file
                    .withAspectRatio(1.toFloat(),1.toFloat())
                    .withOptions(cropOptions)
                    .start(this)
        }
    }
    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}
