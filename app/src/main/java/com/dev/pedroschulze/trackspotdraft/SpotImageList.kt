package com.dev.pedroschulze.trackspotdraft
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.*
import android.widget.*
import com.bumptech.glide.Glide
import com.dev.pedroschulze.trackspotdraft.OldSpotScreen.Companion.REQUEST_IMAGE_CAPTURE
import com.facebook.drawee.backends.pipeline.Fresco
import java.io.File
import com.stfalcon.frescoimageviewer.ImageViewer
import com.yalantis.ucrop.UCrop
import java.util.*

class SpotImageList : CameraOpeningActivity() {

    private lateinit var spotName : String
    lateinit var spotDirectory: String
    lateinit var selectedBodyPart: String
    lateinit var selectedBodySide: String

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menuitems, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.action_add -> {
            dispatchTakePictureIntent(spotDirectory)
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
        title = "Images of " + spotName
        createSpotImageLists()
    }

    private fun createSpotImageLists() { //Same as OldSpotScreen, there's a better way.
        var fullImagePaths = emptyArray<String>()
        var imageThumbnails = emptyArray<Bitmap>()
        val imageFiles =  File(spotDirectory).walk().maxDepth(1).toList()

        for (i in 2..imageFiles.size) {
            fullImagePaths += imageFiles[i-1].toString()
            imageThumbnails += BitmapFactory.decodeFile(imageFiles[i-1].absolutePath)
        }

        val spotImagesAdapter = object : ArrayAdapter<String>(this, R.layout.test_list_item, R.id.title, fullImagePaths) { //Again, can refactor xml
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getView(position, convertView, parent)
                val photoJpegName = view.findViewById<View>(R.id.title) as TextView //TODO Refactor these names
                val photoDescription = view.findViewById<View>(R.id.seconddesc) as TextView
                val text3 = view.findViewById<View>(R.id.artist) as TextView
                val photoThumbnail = view.findViewById<ImageView>(R.id.thumbn) as ImageView
                val spotDateText = "Added on " + fullImagePaths[position].removePrefix(spotDirectory).subSequence(5,15).toString().replace("-", "/")
                photoJpegName.text = fullImagePaths[position].removePrefix(spotDirectory)
                photoJpegName.textSize = 14.toFloat()
                photoDescription.text = spotDateText
                text3.visibility = View.GONE
                Glide.with(this@SpotImageList)
                        .load(imageThumbnails[position])
                        .thumbnail( 0.1f )
                        .into(photoThumbnail)
                return view
            }
        }

        var arrayOfImageUris = emptyArray<Uri>()
        for (uri in fullImagePaths) {
            arrayOfImageUris += Uri.parse("file://" + uri) //Again, is this the best design?
        }
        val listView = findViewById<ListView>(R.id.ListView)
        listView.adapter = spotImagesAdapter
        listView.onItemClickListener = AdapterView.OnItemClickListener { _, _, _, arg3 ->
            ImageViewer.Builder(this, arrayOfImageUris)
                    .setStartPosition(arg3.toInt())
                    .hideStatusBar(false)
                    .allowSwipeToDismiss(true)
                    .show()
        }
        val selectedSpotBooleanList = arrayOfNulls<Boolean>(fullImagePaths.size)
        Arrays.fill(selectedSpotBooleanList, java.lang.Boolean.FALSE)

        with(listView) {
            choiceMode = ListView.CHOICE_MODE_MULTIPLE_MODAL
            setMultiChoiceModeListener(object : AbsListView.MultiChoiceModeListener {
                override fun onItemCheckedStateChanged(mode: ActionMode, position: Int,
                                                       id: Long, checked: Boolean) {
                    selectedSpotBooleanList[position] = checked
                    if (checkedItemCount <2 || checkedItemCount >2) { //Can we make it so no more than 2 can be selected?
                        mode.title = "Select images"
                        mode.menu.findItem(R.id.compare).isEnabled = false
                    } else {
                        mode.menu.findItem(R.id.compare).isEnabled = true
                    }
                }

                override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean {
                    return when (item.itemId) {
                        R.id.compare -> {
                            var spotIndicesToCompare = emptyArray<Int>()
                            (0 until selectedSpotBooleanList.size)
                                    .filter { selectedSpotBooleanList[it]==true }
                                    .forEach { spotIndicesToCompare+= it }
                            val firstSpotToCompare = fullImagePaths[spotIndicesToCompare[0]]
                            val secondSpotToCompare = fullImagePaths[spotIndicesToCompare[1]]

                            val intent = Intent(this@SpotImageList, CompareSpotScreen::class.java)
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

                override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
                    val menuInflater: MenuInflater = mode.menuInflater
                    menuInflater.inflate(R.menu.context_bar, menu)
                    return true
                }

                override fun onDestroyActionMode(mode: ActionMode) {
                    //TODO
                }

                override fun onPrepareActionMode(mode: ActionMode, menu: Menu): Boolean {
                    //TODO
                    return false
                }
            })
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            val intent = Intent(this, SpotImageList::class.java)
            intent.putExtra("spotName", spotName)
            intent.putExtra("spotDirectory", spotDirectory)
            intent.putExtra("selectedBodyPart", selectedBodyPart)
            intent.putExtra("selectedBodySide", selectedBodySide)
            startActivity(intent)
        }

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            if (isValidPhotoPath(currentFullPhotoPath)) {
                val cropOptions = UCrop.Options().apply {
                    setAllowedGestures(0,0,0)
                    setShowCropGrid(false)
                    setToolbarColor(ContextCompat.getColor(applicationContext, R.color.colorPrimary))
                    setActiveWidgetColor(ContextCompat.getColor(applicationContext, R.color.colorPrimary))
                    setStatusBarColor(ContextCompat.getColor(applicationContext, R.color.colorPrimary))
                    setShowCropFrame(true)
                }
                UCrop.of(Uri.parse("file://"+ currentFullPhotoPath), Uri.parse("file://"+ currentFullPhotoPath)) //Fix if needed
                        .withAspectRatio(1.toFloat(),1.toFloat())
                        .withOptions(cropOptions)
                        .start(this)
            }
        }
        if (resultCode != RESULT_OK && isValidPhotoPath(currentFullPhotoPath)) { //Error or back press on cropping activity
            File(currentFullPhotoPath).delete() //Can this cause errors?
            val intent = intent
            finish()
            startActivity(intent)
        }
    }

    override fun onBackPressed() {
        val intent = Intent(this, OldSpotScreen::class.java)
        intent.putExtra("selectedBodySide", selectedBodySide)
        intent.putExtra("selectedBodyPart", selectedBodyPart)
        startActivity(intent)
    }
}