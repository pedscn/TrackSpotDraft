package com.dev.pedscn.trackyourspot

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.bumptech.glide.Glide
import com.dev.pedscn.trackyourspot.BodyParts.LEFT_ARM
import com.dev.pedscn.trackyourspot.BodyParts.LEFT_LEG
import com.dev.pedscn.trackyourspot.BodyParts.RIGHT_ARM
import com.dev.pedscn.trackyourspot.BodyParts.RIGHT_LEG
import com.yalantis.ucrop.UCrop
import com.yalantis.ucrop.UCropActivity
import kotlinx.android.synthetic.main.activity_old_spot_screen.*
import java.io.File


class OldSpotListActivity : CameraOpeningActivity() {

    companion object {
        const val REQUEST_IMAGE_CAPTURE = 1
        const val REQUEST_TAKE_PHOTO = 1
    }

    private lateinit var selectedBodyPart: String
    private lateinit var devicePictureDirectory: String
    private lateinit var selectedBodySide: String
    private lateinit var spotListDirectory: String

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.addbar, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.action_add -> {
            checkPermissionsAndStartCamera()
            true
        }

        R.id.action_info -> {
            val alertDialog = AlertDialog.Builder(this).create()
            alertDialog.setTitle("List of Spots")
            alertDialog.setMessage(
                "This is a list of all your spots.\n" +
                        "\n1. Add a spot by tapping the + button\n" +
                        "\n2. View a spot by selecting it from the list"
            )
            alertDialog.setButton(
                AlertDialog.BUTTON_POSITIVE, "OK"
            ) { dialog, _ -> dialog.dismiss() }
            alertDialog.show()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    private fun checkPermissionsAndStartCamera() {
        val hasCameraPermission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
        val hasStoragePermission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
        if (!hasCameraPermission || !hasStoragePermission) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                0
            )
        } else {
            deployCamera()
        }
    }

    private fun deployCamera() {
        val spotTempDirectory =
            "$devicePictureDirectory/trackyourspot/temp/"
        dispatchTakePictureIntent(spotTempDirectory)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == 0) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED
                && grantResults[1] == PackageManager.PERMISSION_GRANTED
            ) {
                deployCamera()
            } else {
                Toast.makeText(
                    this,
                    "Please enable Camera and Storage permissions",
                    Toast.LENGTH_SHORT
                ).show()
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
        devicePictureDirectory =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString()
        spotListDirectory =
            "$devicePictureDirectory/trackyourspot/$selectedBodySide/$selectedBodyPart/" //Research good file path naming conventions
        val capitalisedTitle = when (selectedBodyPart) { //Hacky way to display title correctly
            LEFT_ARM -> "Left Arm"
            RIGHT_ARM -> "Right Arm"
            RIGHT_LEG -> "Right Leg"
            LEFT_LEG -> "Left Leg"
            else -> selectedBodyPart.capitalize()
        }
        title = "$capitalisedTitle Spots"
    }

    private fun createSpotList() {
        var spotNames = emptyArray<String>() //Spot Names
        //var spotImageNames = emptyArray<String>() //JPG only filenames
        var spotDirectories = emptyArray<String>() //Spot paths without jpg file
        var spotThumbnails = emptyArray<Bitmap>() //Spot thumbnail paths
        var numberOfSpots = 0 //Counter for later use
        File(spotListDirectory).walk().maxDepth(1).forEachIndexed { index, file ->
            if (index != 0) { //Ignore the root folder
                numberOfSpots++
                val spotName = file.toString().removePrefix(spotListDirectory)
                spotNames += spotName
                spotDirectories += "$spotListDirectory$spotName/"
                val filesInFolder =
                    File("$spotListDirectory/$spotName/").walk().maxDepth(1).toList()
                spotThumbnails += if (filesInFolder.size > 1) {
                    BitmapFactory.decodeFile(filesInFolder[1].absolutePath)
                } else {
                    BitmapFactory.decodeResource(this.resources, R.drawable.questionmark)
                }
            }
        }

        val hasCameraBeenClosed = intent.getBooleanExtra(
            "cameraClosed",
            false
        ) //Opens Camera straight away if there are no old spots
        if (numberOfSpots == 0 && !hasCameraBeenClosed) {
            checkPermissionsAndStartCamera()
        }

        //spotNames needs to be passed to adapter or no spots are shown.
        val spotListAdapter = object : ArrayAdapter<String>(
            this,
            R.layout.list_row,
            R.id.list_row_title,
            spotNames
        ) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getView(position, convertView, parent)
                val spotNameTextView =
                    view.findViewById<View>(R.id.list_row_title) as TextView
                val spotThumbnailImageView = view.findViewById(R.id.list_row_thumbnail) as ImageView
                spotNameTextView.text = spotNames[position]
                /*
                val spotJpegTextView = view.findViewById<View>(R.id.list_row_description) as TextView
                spotJpegTextView.text = spotDirectories[position]
                */
                Glide.with(this@OldSpotListActivity)
                    .load(spotThumbnails[position])
                    .thumbnail(0.1f) //Improves memory management
                    .into(spotThumbnailImageView)
                return view
            }
        }

        val listView = old_spot_screen_spot_list
        listView.adapter = spotListAdapter
        listView.onItemClickListener = AdapterView.OnItemClickListener { _, _, index, _ ->
            val intent = Intent(this, SpotImageListActivity::class.java)
            intent.apply {
                putExtra("selectedBodyPart", selectedBodyPart)
                putExtra(
                    "selectedBodySide",
                    selectedBodySide
                )
                putExtra("spotName", spotNames[index])
                putExtra("spotDirectory", spotDirectories[index])
            }
            startActivity(intent)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == UCrop.REQUEST_CROP && resultCode == RESULT_OK) {
            val intent = Intent(this, AddSpotActivity::class.java)
            intent.putExtra(
                "selectedBodySide",
                selectedBodySide
            )
            intent.putExtra("selectedBodyPart", selectedBodyPart)
            intent.putExtra("spotListDirectory", spotListDirectory)
            intent.putExtra(
                "spotImageName",
                currentFullPhotoPath.removePrefix("$devicePictureDirectory/trackyourspot/temp/")
            )
            intent.putExtra("fullPhotoPath", currentFullPhotoPath)
            startActivity(intent)
        }

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            if (isValidPhotoPath(currentFullPhotoPath)) {
                val cropOptions = UCrop.Options().apply {
                    setShowCropGrid(false)
                    setToolbarColor(
                        ContextCompat.getColor(
                            applicationContext,
                            R.color.colorPrimary
                        )
                    )
                    setActiveWidgetColor(
                        ContextCompat.getColor(
                            applicationContext,
                            R.color.colorPrimary
                        )
                    )
                    setStatusBarColor(
                        ContextCompat.getColor(
                            applicationContext,
                            R.color.colorPrimary
                        )
                    )
                    setShowCropFrame(true)
                    setAllowedGestures(UCropActivity.NONE, UCropActivity.NONE, UCropActivity.NONE)
                }
                UCrop.of(
                    Uri.parse("file://$currentFullPhotoPath"),
                    Uri.parse("file://$currentFullPhotoPath")
                ) //same destination as source file
                    .withAspectRatio(1.toFloat(), 1.toFloat())
                    .withOptions(cropOptions)
                    .start(this)
            }
        }
        if (resultCode != RESULT_OK && isValidPhotoPath(currentFullPhotoPath)) {
            File(currentFullPhotoPath).delete() //Can this cause errors?
            val intent = intent
            intent.putExtra("cameraClosed", true)
            finish()
            startActivity(intent)
        }
    }

    override fun onBackPressed() {
        val intent = Intent(this, BodyScreenActivity::class.java)
        intent.putExtra("selectedBodySide", selectedBodySide)
        startActivity(intent)
    }
}
