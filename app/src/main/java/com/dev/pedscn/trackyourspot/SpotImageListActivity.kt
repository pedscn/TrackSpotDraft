package com.dev.pedscn.trackyourspot

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.view.*
import android.widget.*
import android.widget.AdapterView
import com.bumptech.glide.Glide
import com.dev.pedscn.trackyourspot.OldSpotListActivity.Companion.REQUEST_IMAGE_CAPTURE
import com.facebook.drawee.backends.pipeline.Fresco
import com.stfalcon.frescoimageviewer.ImageViewer
import com.yalantis.ucrop.UCrop
import com.yalantis.ucrop.UCropActivity
import java.io.File
import java.util.*

class SpotImageListActivity : CameraOpeningActivity() {

    private lateinit var spotName: String
    lateinit var spotDirectory: String
    lateinit var selectedBodyPart: String
    lateinit var selectedBodySide: String
    private var mActionModeCallback: AbsListView.MultiChoiceModeListener? = null
    private var numberOfImages = 0
    lateinit var listView: ListView

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.imagelistbar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.action_add -> {
            dispatchTakePictureIntent(spotDirectory)
            true
        }
        R.id.compare -> {
            if (numberOfImages > 0) {
                startActionMode(mActionModeCallback)
                listView.setItemChecked(0, true)
            }
            true
        }


        R.id.action_info -> {
            val alertDialog = AlertDialog.Builder(this).create()
            alertDialog.setTitle("Photos of a spot")
            alertDialog.setMessage(
                "These are all the photos of a spot.\n" +
                        "\n1. Tap the compare button to select the images\n" +
                        "\n2. Select exactly two images to compare\n" +
                        "\n3. Tap the compare button again to view them"
            )
            alertDialog.setButton(
                AlertDialog.BUTTON_POSITIVE, "OK"
            ) { dialog, _ -> dialog.dismiss() }
            alertDialog.show()
            true
        }

        else -> super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spot_image_list)
        setSupportActionBar(findViewById(R.id.toolbar))
        Fresco.initialize(this)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        spotName = intent.getStringExtra("spotName")
        spotDirectory = intent.getStringExtra("spotDirectory")
        selectedBodyPart = intent.getStringExtra("selectedBodyPart")
        selectedBodySide = intent.getStringExtra("selectedBodySide")
        title = spotName
        createSpotImageLists()
    }

    private fun createSpotImageLists() { //Same as OldSpotListActivity, there's a better way.
        //Initialise arrays for each photo's path and thumbnails
        var fullImagePaths = emptyArray<String>()
        var imageThumbnails = emptyArray<Bitmap>()
        //Loop through images in the selected spot's directory
        val imageFiles = File(spotDirectory).walk().maxDepth(1).toList()
        for (i in 2..imageFiles.size) {
            fullImagePaths += imageFiles[i - 1].toString()
            imageThumbnails += BitmapFactory.decodeFile(imageFiles[i - 1].absolutePath)
        }
        numberOfImages = fullImagePaths.size
        //Initialise dynamic array to keep track of which images are selected
        val selectedSpotBooleanList = arrayOfNulls<Boolean>(fullImagePaths.size)
        //By default, no images are selected
        Arrays.fill(selectedSpotBooleanList, java.lang.Boolean.FALSE)

        //Initialise contextual action toolbar to allow user to select images
        mActionModeCallback = object : AbsListView.MultiChoiceModeListener { //Contextual Action Bar
            //This method is called whenever an item is selected or deselected
            override fun onItemCheckedStateChanged(
                mode: ActionMode,
                position: Int,
                id: Long,
                checked: Boolean
            ) {
                //Update list of selected spots array
                selectedSpotBooleanList[position] = checked
                val checkedItemCount = listView.checkedItemCount
                //Allow pressing the compare button if exactly 2 images are selected
                mode.menu.findItem(R.id.compare).isEnabled =
                    checkedItemCount == 2 //Needed so that compare button is only available if 2 images are selected
            }

            override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
                val menuInflater: MenuInflater = mode.menuInflater
                menuInflater.inflate(R.menu.contextual_bar, menu)
                mode.title = "Select two images"
                return true
            }

            override fun onDestroyActionMode(mode: ActionMode) {
            }

            override fun onPrepareActionMode(mode: ActionMode, menu: Menu): Boolean {
                return false
            }

            //This method is called when the user presses a button in the
            //contextual action toolbar.
            override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean {
                return when (item.itemId) {
                    //When the user presses the "compare" button
                    R.id.compare -> {
                        //Initialise array of indices of the images to compare
                        var spotIndicesToCompare = emptyArray<Int>()
                        //Loop through array of selected spots
                        (0 until selectedSpotBooleanList.size)
                            //If a spot is selected, save it's index
                            .filter { selectedSpotBooleanList[it] == true }
                            .forEach { spotIndicesToCompare += it }
                        //Get image paths for both spots to be compared
                        val firstSpotToCompare = fullImagePaths[spotIndicesToCompare[0]]
                        val secondSpotToCompare = fullImagePaths[spotIndicesToCompare[1]]
                        //Open the comparison screen and send info of selected sports
                        val intent =
                            Intent(this@SpotImageListActivity, CompareSpotActivity::class.java)
                        intent.putExtra("firstSpotToCompare", firstSpotToCompare)
                        intent.putExtra("secondSpotToCompare", secondSpotToCompare)
                        intent.putExtra("spotName", spotName)
                        intent.putExtra("spotDirectory", spotDirectory)
                        intent.putExtra("selectedBodyPart", selectedBodyPart)
                        intent.putExtra("selectedBodySide", selectedBodySide)
                        startActivity(intent)
                        true
                    }
                    else -> false
                }
            }
        }

        val spotImagesAdapter = object : ArrayAdapter<String>(
            this,
            R.layout.list_row,
            R.id.list_row_title,
            fullImagePaths
        ) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getView(position, convertView, parent)
                val photoJpegName = view.findViewById<View>(R.id.list_row_title) as TextView
                val photoThumbnail = view.findViewById(R.id.list_row_thumbnail) as ImageView
                /*val photoDescription = view.findViewById<View>(R.id.list_row_description) as TextView
                val spotDateText =
                    "Added on " + fullImagePaths[position].removePrefix(spotDirectory).subSequence(
                        5,
                        15
                    ).toString().replace("-", "/")*/
                photoJpegName.text = fullImagePaths[position].removePrefix(spotDirectory)
                photoJpegName.textSize = 14.toFloat()
                //photoDescription.text = spotDateText
                Glide.with(this@SpotImageListActivity)
                    .load(imageThumbnails[position])
                    .thumbnail(0.1f)
                    .into(photoThumbnail)
                return view
            }
        }

        var arrayOfImageUris = emptyArray<Uri>()
        for (uri in fullImagePaths) {
            arrayOfImageUris += Uri.parse("file://$uri") //Again, is this the best design?
        }

        listView = findViewById(R.id.ListView)
        listView.apply {
            adapter = spotImagesAdapter
            choiceMode = ListView.CHOICE_MODE_MULTIPLE_MODAL
            setMultiChoiceModeListener(mActionModeCallback)
            onItemClickListener = AdapterView.OnItemClickListener { _, _, _, arg3 ->
                ImageViewer.Builder(context, arrayOfImageUris)
                    .setStartPosition(arg3.toInt())
                    .hideStatusBar(false)
                    .allowSwipeToDismiss(true)
                    .show()
            }
            onItemLongClickListener = object : AdapterView.OnItemLongClickListener {
                override fun onItemLongClick(
                    arg0: AdapterView<*>, view: View,
                    position: Int, id: Long
                ): Boolean {
                    startActionMode(mActionModeCallback)
                    return true
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        //If Cropping has been done successfully
        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            //Open Spot Image list screen for that specific spot
            val intent = Intent(this, SpotImageListActivity::class.java)
            //Pass on spot name and directory to the image list
            intent.putExtra("spotName", spotName)
            intent.putExtra("spotDirectory", spotDirectory)
            intent.putExtra("selectedBodyPart", selectedBodyPart)
            intent.putExtra("selectedBodySide", selectedBodySide)
            startActivity(intent)
        }

        //If Photo has been taken successfully
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            //Check if the photopath is valid (eg. is a jpg file in storage)
            if (isValidPhotoPath(currentFullPhotoPath)) {
                //Start uCrop cropping screen with specific UI properties
                val cropOptions = UCrop.Options().apply {
                    setAllowedGestures(UCropActivity.NONE, UCropActivity.NONE, UCropActivity.NONE)
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
                }
                //Choose the same source and destination for the cropped image (Overwrite it)
                UCrop.of(
                    Uri.parse("file://$currentFullPhotoPath"),
                    Uri.parse("file://$currentFullPhotoPath")
                ) //Fix if needed
                    .withAspectRatio(1.toFloat(), 1.toFloat())
                    .withOptions(cropOptions)
                    .start(this)
            }
        }

        //If photo hasn't been taken (eg. user presses the back button)
        if (resultCode != RESULT_OK && isValidPhotoPath(currentFullPhotoPath)) {
            //Delete the corrupt image file
            File(currentFullPhotoPath).delete() //Can this cause errors?
            val intent = intent
            finish()
            startActivity(intent)
        }
    }

    override fun onBackPressed() {
        val intent = Intent(this, OldSpotListActivity::class.java)
        intent.putExtra("selectedBodySide", selectedBodySide)
        intent.putExtra("selectedBodyPart", selectedBodyPart)
        startActivity(intent)
    }
}