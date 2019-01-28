package com.example.pedroschulze.trackspotdraft

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.design.widget.Snackbar
import android.support.v4.content.FileProvider
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.bumptech.glide.Glide
import com.facebook.drawee.backends.pipeline.Fresco

import kotlinx.android.synthetic.main.activity_spot_image_list.*
import java.io.File
import com.stfalcon.frescoimageviewer.ImageViewer
import com.yalantis.ucrop.UCrop
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class SpotImageList : AppCompatActivity() {

    var mCurrentPhotoPath = ""
    var limb = ""
    var spotName = ""
    var spotPath = ""
    var spotPicname = ""
    var currentPath = ""

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menuitems, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.action_add -> {
            // do stuff
            val spotName = "temp"
            val spotDesc = spotPath
            val spotDetail = "placeholder"
            dispatchTakePictureIntent(spotDesc)
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spot_image_list)
        setSupportActionBar(findViewById(R.id.toolbar))
        Fresco.initialize(this)

        limb = intent.getStringExtra("limb")
        spotName = intent.getStringExtra("spotName")
        spotPath = intent.getStringExtra("spotPath")
        spotPicname = intent.getStringExtra("spotPicname")
        title = "Spot Images of "+spotName
        Log.e("spotName", spotName)
        Log.e("spotPath", spotPath)
        Log.e("spotPicname", spotPicname)
        Log.e("limb", limb)

        //var imgNames = emptyArray<String>()
        var imgPaths = emptyArray<String>()
        var imgDesc = emptyArray<String>()
        var thumbnails = emptyArray<Bitmap>()
        val imgFiles =  File(spotPath).walk().maxDepth(1).toList()
        //Log.e("name", name)
        Log.e("imgfiles", imgFiles.toString())

        for (i in 2..imgFiles.size) {
            Log.e("imgfile", imgFiles[i-1].toString())
            imgPaths += imgFiles[i-1].toString()
            //imgNames += name
            //imgDesc += "/storage/emulated/0/Pictures/trackspot/$limb/$name/"
            //imgDetails += imgFile.toString().removePrefix("/storage/emulated/0/Pictures/trackspot/$limb/$name/")
            thumbnails += BitmapFactory.decodeFile(imgFiles[i-1].absolutePath)
        }

        val adapter1 = object : ArrayAdapter<String>(this, R.layout.list_item, R.id.title, imgPaths) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getView(position, convertView, parent)
                val text1 = view.findViewById<View>(R.id.title) as TextView
                val text2 = view.findViewById<View>(R.id.seconddesc) as TextView
                val text3 = view.findViewById<View>(R.id.artist) as TextView
                val view1 = view.findViewById<ImageView>(R.id.thumbn) as ImageView

                text1.text = imgPaths[position].removePrefix(spotPath)
                text1.textSize = 14.toFloat()
                text2.text = spotPath
                text3.visibility = View.INVISIBLE
                Glide.with(this@SpotImageList)
                        .load(thumbnails[position])
                        .thumbnail( 0.1f )
                        .into(view1)
                return view
            }
        }

        var pics = emptyArray<Uri>()
        for (uri in imgPaths) {
            Log.e("URI", uri)
            pics += Uri.parse("file://"+uri)
        }
        //pics += Uri.parse("file://"+imgFiles[arg3.toInt()+1].toString())

        val listView = findViewById<ListView>(R.id.sampleListView)
        listView.adapter = adapter1 //Setting adapter and listener to start song level when clicked.
        listView.onItemClickListener = AdapterView.OnItemClickListener { arg0, arg1, arg2, arg3 ->
            Log.e("arg3", arg3.toString())
            //Log.e("spotNames", spotNames[arg2])
            //Log.e("spotDetails", spotDetails[arg2])
            //Log.e("spotDesc", spotDesc[arg2])
            //val intent = Intent(this, TrackSpotScreen::class.java)
            //intent.putExtra("limb", limb)
            //intent.putExtra("spotName", spotNames[arg2])
            //intent.putExtra("spotPicname", spotDetails[arg2])
            //intent.putExtra("spotPath", spotDesc[arg2])
            //startActivity(intent)

            ImageViewer.Builder(this, pics)
                    .setStartPosition(arg3.toInt())
                    .hideStatusBar(false)
                    .allowSwipeToDismiss(true)
                    .show()
        }
    }
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
                            "com.example.pedroschulze.trackspotdraft.android.fileprovider",
                            it
                    )
                    Log.e("BodyScreen", "startingIntent")
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, BodyScreen.REQUEST_TAKE_PHOTO)
                }
            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.e("BodyScreen", "onActivityResult")

        if (requestCode == UCrop.REQUEST_CROP) {
                val intent = Intent(this, SpotImageList::class.java)
                intent.putExtra("limb", limb)
                intent.putExtra("spotName", spotName)
                intent.putExtra("spotPath", spotPath)
                intent.putExtra("spotPicname", "placeholder")
                startActivity(intent)
            }

        if (requestCode == BodyScreen.REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {

            val cropOptions = UCrop.Options().apply {
                setAllowedGestures(0,0,0)
            }

            UCrop.of(Uri.parse("file://"+mCurrentPhotoPath), Uri.parse("file://"+mCurrentPhotoPath))
                    .withAspectRatio(1.toFloat(),1.toFloat())
                    .withOptions(cropOptions)
                    .start(this)
        }
    }
}