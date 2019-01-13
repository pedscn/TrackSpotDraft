package com.example.pedroschulze.trackspotdraft

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.view.ViewGroup
import java.io.File
import android.graphics.BitmapFactory
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.os.Environment.*
import android.provider.MediaStore
import com.bumptech.glide.Glide
import android.support.v4.content.FileProvider
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class OldSpotScreen : AppCompatActivity() {

    var selectedLimb = ""
    var isOldSpotSelected = false
    var currentPath = ""
    private var mCurrentPhotoPath: String = ""


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menuitems, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.action_add -> {
            // do stuff
            val spotName = "temp"
            val spotDesc = "/storage/emulated/0/Pictures/trackspot/$selectedLimb/temp/"
            currentPath = spotDesc
            val spotDetail = "placeholder"
            dispatchTakePictureIntent(spotName, spotDetail, spotDesc)
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_old_spot_screen)
        setSupportActionBar(findViewById(R.id.my_toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        isOldSpotSelected = false


        selectedLimb = intent.getStringExtra("limb")
        val limb = intent.getStringExtra("limb")
        title = limb+" Spots"
        val MainMenuAction = intent.getStringExtra("option")
        var spotNames = emptyArray<String>()
        var spotDetails = emptyArray<String>()
        var spotDesc = emptyArray<String>()
        var thumbnails = emptyArray<Bitmap>()
        File("/storage/emulated/0/Pictures/trackspot/$limb/").walk().maxDepth(1).forEachIndexed { index, file ->
            Log.e("PATH", file.toString())
            if (index != 0) {
                val name = file.toString().removePrefix("/storage/emulated/0/Pictures/trackspot/$limb/")
                Log.e("imgFile walk", File("/storage/emulated/0/Pictures/trackspot/$limb/$name/").walk().maxDepth(1).toList().toString())
                val imgFile =  File("/storage/emulated/0/Pictures/trackspot/$limb/$name/").walk().maxDepth(1).toList()[1]
                Log.e("name", name)
                Log.e("imgfile", imgFile.toString())
                spotNames += name
                spotDesc += "/storage/emulated/0/Pictures/trackspot/$limb/$name/"
                spotDetails += imgFile.toString().removePrefix("/storage/emulated/0/Pictures/trackspot/$limb/$name/")
                thumbnails += BitmapFactory.decodeFile(imgFile.absolutePath)
                //val name = nameAndImage.split("/")[0]
                //val image = nameAndImage.split("/")[1]
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
                text3.text = spotDesc[position].removePrefix("/storage/emulated/0")
                Glide.with(this@OldSpotScreen)
                        .load(thumbnails[position])
                        .thumbnail( 0.1f )
                        .into(view1)
                return view
            }
        }

        val listView = findViewById<ListView>(R.id.sampleListView)
        listView.adapter = adapter1 //Setting adapter and listener to start song level when clicked.
        listView.onItemClickListener = AdapterView.OnItemClickListener { arg0, arg1, arg2, arg3 ->
            Log.e("arg3", arg3.toString())
            Log.e("spotNames", spotNames[arg2])
            Log.e("spotDetails", spotDetails[arg2])
            Log.e("spotDesc", spotDesc[arg2])
            isOldSpotSelected = false
            if (MainMenuAction=="oldspot") {
                dispatchTakePictureIntent(spotNames[arg2], spotDetails[arg2], spotDesc[arg2])
            }
            else {
                val intent = Intent(this, TrackSpotScreen::class.java)
                intent.putExtra("limb", limb)
                intent.putExtra("spotName", spotNames[arg2])
                intent.putExtra("spotPicname", spotDetails[arg2])
                intent.putExtra("spotPath", spotDesc[arg2])
                startActivity(intent)
            }

        }
    }

    @Throws(IOException::class)
    fun createImageFile(spotDesc: String): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File = getExternalStoragePublicDirectory(DIRECTORY_PICTURES)
        val directorypath = spotDesc
        val newDir = File(spotDesc)
        if(!newDir.exists()) newDir.mkdirs()

        return File.createTempFile(
                "JPEG_${timeStamp}_", /* prefix */
                ".jpg", /* suffix */
                newDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            mCurrentPhotoPath = absolutePath
            //Log.e("mCurrentPhotoPath IM", mCurrentPhotoPath)
        }
    }

    fun dispatchTakePictureIntent(spotName: String, spotDetail: String, spotDesc: String) {
        Log.e("BodyScreen", "dispatchTakePictureIntent")
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(packageManager)?.also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    createImageFile(spotDesc)
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

        if (requestCode == BodyScreen.REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            //val intent = Intent(this, OldSpotScreen::class.java)
            //startActivity(intent)
            if (isOldSpotSelected) {
                Toast.makeText(this@OldSpotScreen, "Spot updated", Toast.LENGTH_SHORT).show()
                finish()
                startActivity(intent)
            } else {
                val intent = Intent(this, AddSpot::class.java)
                intent.putExtra("limb", selectedLimb)
                intent.putExtra("directorypath", currentPath)
                intent.putExtra("imgpath", mCurrentPhotoPath)
                intent.putExtra("imgname", "placeholder")
                startActivity(intent)
            }
        }
    }
    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}
