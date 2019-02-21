package com.dev.pedroschulze.trackspotdraft

import android.Manifest
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
import android.support.v7.widget.Toolbar
import com.bumptech.glide.Glide
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import com.yalantis.ucrop.UCrop
import kotlinx.android.synthetic.main.activity_old_spot_screen.*
import android.support.v4.app.ActivityCompat
import android.content.pm.PackageManager
import android.support.v4.content.ContextCompat


class OldSpotScreen : CameraOpeningActivity() { // Reduces redundancy

    companion object {
        val REQUEST_IMAGE_CAPTURE = 1
        val REQUEST_TAKE_PHOTO = 1
    }

    private lateinit var selectedBodyPart : String
    private lateinit var devicePictureDirectory : String
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
            checkPermissions()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    private fun checkPermissions() {
        val hasCameraPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
        val hasStoragePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)  == PackageManager.PERMISSION_GRANTED
        if (!hasCameraPermission || !hasStoragePermission) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE), 0)
        } else {
            startCamera()
        }
    }

    private fun startCamera() {
        val spotTempDirectory = devicePictureDirectory + "/trackyourspot/temp/" //Probably crap design, could find something better
        dispatchTakePictureIntent(spotTempDirectory)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == 0) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                startCamera()
            }
            else {
                Toast.makeText(this, "Please enable Camera and Storage permissions", Toast.LENGTH_SHORT).show();
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_old_spot_screen)
        setSupportActionBar(old_spot_screen_toolbar as Toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        initVariables()
        createSpotList()
    }

    private fun initVariables() {
        selectedBodyPart = intent.getStringExtra("selectedBodyPart")
        selectedBodySide = intent.getStringExtra("selectedBodySide")
        devicePictureDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString()
        spotListDirectory = "$devicePictureDirectory/trackyourspot/$selectedBodySide/$selectedBodyPart/" //Research good file path naming conventions
        val capitalisedTitle = selectedBodyPart.capitalize()
        title = "$capitalisedTitle Spots" //Need to substitute with labels
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

        val spotListAdapter = object : ArrayAdapter<String>(this, R.layout.test_list_item, R.id.title, spotNames) { //spotNames needs to be passed or no spots shown.
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getView(position, convertView, parent)
                val spotNameTextView = view.findViewById<View>(R.id.title) as TextView //Need to refactor and edit xml
                val spotDirectoryTextView = view.findViewById<View>(R.id.artist) as TextView
                val spotJpegTextView = view.findViewById<View>(R.id.seconddesc) as TextView
                val spotThumbnailImageView = view.findViewById<ImageView>(R.id.thumbn) as ImageView
                val spotDateText = "Added on " +spotImageNames[position].removePrefix(spotDirectories[position]).subSequence(5,15)
                spotNameTextView.text = spotNames[position]
                spotDirectoryTextView.text = spotDirectories[position] //TODO, substitute with date or number of dates
                spotJpegTextView.text = spotDirectories[position]
                spotDirectoryTextView.visibility = View.GONE
                Glide.with(this@OldSpotScreen)
                        .load(spotThumbnails[position])
                        .thumbnail( 0.1f ) //Improves memory management
                        .into(spotThumbnailImageView)
                return view
            }
        }

        val listView = old_spot_screen_spot_list
        listView.adapter = spotListAdapter
        listView.onItemClickListener = AdapterView.OnItemClickListener { _, _, index, _ -> //We only need the position index to select item from spotList
            val intent = Intent(this, SpotImageList::class.java)
            intent.apply {
                putExtra("selectedBodyPart", selectedBodyPart)
                putExtra("selectedBodySide", selectedBodySide) //Not necessarily needed, can remove in the future
                putExtra("spotName", spotNames[index])
                putExtra("spotImageName", spotImageNames[index])
                putExtra("spotDirectory", spotDirectories[index])
            }
            startActivity(intent)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == UCrop.REQUEST_CROP && resultCode == RESULT_OK) {
            val intent = Intent(this, AddSpot::class.java)
            intent.putExtra("selectedBodySide", selectedBodySide) //Not necessarily needed, can delete later
            intent.putExtra("selectedBodyPart", selectedBodyPart)
            intent.putExtra("spotListDirectory", spotListDirectory)
            intent.putExtra("spotImageName", currentFullPhotoPath.removePrefix(devicePictureDirectory+"/trackyourspot/temp/"))
            intent.putExtra("fullPhotoPath", currentFullPhotoPath)
            startActivity(intent)
        }

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            val cropOptions = UCrop.Options().apply {
                setShowCropGrid(false)
                setToolbarColor(ContextCompat.getColor(applicationContext, R.color.colorPrimary))
                setActiveWidgetColor(ContextCompat.getColor(applicationContext, R.color.colorPrimary))
                setStatusBarColor(ContextCompat.getColor(applicationContext, R.color.colorPrimary))
                setShowCropFrame(true)
                setAllowedGestures(0,0,0)
            }
            //Is this stable on every device? Bad design probably
            UCrop.of(Uri.parse("file://"+ currentFullPhotoPath), Uri.parse("file://"+ currentFullPhotoPath)) //same destination as source file
                    .withAspectRatio(1.toFloat(), 1.toFloat())
                    .withOptions(cropOptions)
                    .start(this)
        }
        //Error or back press on cropping activity
        if (resultCode != RESULT_OK) {
            File(currentFullPhotoPath).delete() //Can this cause errors?
            val intent = intent
            finish()
            startActivity(intent)
        }
    }

    override fun onBackPressed() {
        val intent = Intent(this, BodyScreen::class.java)
        intent.putExtra("selectedBodySide", selectedBodySide)
        startActivity(intent)
    }
}
