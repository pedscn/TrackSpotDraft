package com.example.pedroschulze.trackspotdraft

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import java.io.File
import android.graphics.BitmapFactory
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import com.bumptech.glide.Glide
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import com.yalantis.ucrop.UCrop

class OldSpotScreen : CameraOpeningActivity() { // Reduces redundancy

    companion object {
        val REQUEST_IMAGE_CAPTURE = 1
        val REQUEST_TAKE_PHOTO = 1
    }

    private lateinit var selectedBodyPart : String
    private lateinit var selectedBodySide : String
    private lateinit var spotListDirectory: String

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menuitems, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.action_add -> {
            val spotTempDirectory = spotListDirectory + "temp/" //Probably crap design, could find something better
            dispatchTakePictureIntent(spotTempDirectory)
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_old_spot_screen)
        setSupportActionBar(findViewById(R.id.my_toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        initVariables()
        createSpotList()
    }

    private fun initVariables() {
        selectedBodyPart = intent.getStringExtra("selectedBodyPart")
        selectedBodySide = intent.getStringExtra("selectedBodySide")
        val devicePictureDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        spotListDirectory = devicePictureDirectory.toString() + "/trackyourspot/$selectedBodySide/$selectedBodyPart/" //Research good file path naming conventions
        title = "$selectedBodyPart Spots" //Need to substitute with labels
    }

    private fun createSpotList() {
        var spotNames = emptyArray<String>()
        var spotImageNames = emptyArray<String>() //JPG only filenames
        var spotDirectories = emptyArray<String>() //Path without jpg
        var spotThumbnails = emptyArray<Bitmap>()
        File(spotListDirectory).walk().maxDepth(1).forEachIndexed { index, file -> //Look into SQLLite
            if (index != 0) {
                val spotName = file.toString().removePrefix(spotListDirectory)
                val imgFile =  File("$spotListDirectory/$spotName/").walk().maxDepth(1).toList()[1]
                spotNames += spotName
                spotDirectories += "$spotListDirectory$spotName/"
                spotImageNames += imgFile.toString().removePrefix("$spotListDirectory/$spotName/")
                spotThumbnails += BitmapFactory.decodeFile(imgFile.absolutePath)
            }
        }

        val spotListAdapter = object : ArrayAdapter<String>(this, R.layout.list_item, R.id.title, spotNames) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getView(position, convertView, parent)
                val spotNameTextview = view.findViewById<View>(R.id.title) as TextView //Need to refactor and edit xml
                val spotDirectoryTextview = view.findViewById<View>(R.id.artist) as TextView
                val spotJpegTextview = view.findViewById<View>(R.id.seconddesc) as TextView
                val spotThumbnailImageview = view.findViewById<ImageView>(R.id.thumbn) as ImageView
                spotNameTextview.text = spotNames[position]
                spotDirectoryTextview.text = spotDirectories[position] //TODO, substitute with date or number of dates
                spotJpegTextview.text = spotImageNames[position].removePrefix(spotDirectories[position]).subSequence(5,15) //Shows picture date
                Glide.with(this@OldSpotScreen)
                        .load(spotThumbnails[position])
                        .thumbnail( 0.1f ) //Improves memory management
                        .into(spotThumbnailImageview)
                return view
            }
        }

        val listView = findViewById<ListView>(R.id.ListView)
        listView.adapter = spotListAdapter
        listView.onItemClickListener = AdapterView.OnItemClickListener { _, _, arg2, _ -> //We only need the position index to select item from spotList
            val intent = Intent(this, SpotImageList::class.java)
            intent.putExtra("selectedBodyPart", selectedBodyPart)
            intent.putExtra("selectedBodySide", selectedBodySide) //Not necessarily needed, can remove in the future
            intent.putExtra("spotName", spotNames[arg2])
            intent.putExtra("spotImageName", spotImageNames[arg2])
            intent.putExtra("spotDirectory", spotDirectories[arg2])
            startActivity(intent)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == UCrop.REQUEST_CROP) {
            val intent = Intent(this, AddSpot::class.java)
            intent.putExtra("selectedBodySide", selectedBodySide) //Not necessarily needed, can delete later
            intent.putExtra("selectedBodyPart", selectedBodyPart)
            intent.putExtra("spotListDirectory", spotListDirectory)
            intent.putExtra("spotImageName", currentFullPhotoPath.removePrefix(spotListDirectory+"temp/"))
            intent.putExtra("fullPhotoPath", currentFullPhotoPath)
            startActivity(intent)
        }

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            val cropOptions = UCrop.Options().apply { //Could customize better for app UI
                //setActiveWidgetColor(Color.BLUE)
                //setToolbarColor(Color.BLUE)
                setAllowedGestures(0,0,0)
            }
            //Is this stable on every device? Bad design probably
            UCrop.of(Uri.parse("file://"+ currentFullPhotoPath), Uri.parse("file://"+ currentFullPhotoPath)) //same destination as source file
                    .withAspectRatio(1.toFloat(),1.toFloat())
                    .withOptions(cropOptions)
                    .start(this)
        }
    }

    override fun onBackPressed() {
        val intent = Intent(this, BodyScreen::class.java)
        intent.putExtra("selectedBodySide", selectedBodySide)
        startActivity(intent)
    }
}
