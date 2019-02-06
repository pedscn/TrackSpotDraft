package com.example.pedroschulze.trackspotdraft
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import com.bumptech.glide.Glide
import com.example.pedroschulze.trackspotdraft.OldSpotScreen.Companion.REQUEST_IMAGE_CAPTURE
import com.facebook.drawee.backends.pipeline.Fresco
import java.io.File
import com.stfalcon.frescoimageviewer.ImageViewer
import com.yalantis.ucrop.UCrop

class SpotImageList : CameraOpeningActivity() {

    var selectedBodyPart = ""
    var spotName = ""
    var spotPath = ""
    var spotImageName = ""


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menuitems, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.action_add -> {
            dispatchTakePictureIntent(spotPath)
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

        selectedBodyPart = intent.getStringExtra("selectedBodyPart")
        spotName = intent.getStringExtra("spotName")
        spotPath = intent.getStringExtra("spotPath")
        spotImageName = intent.getStringExtra("spotImageName")
        title = "Spot Images of "+spotName
        
        var imgPaths = emptyArray<String>()
        var thumbnails = emptyArray<Bitmap>()
        val imgFiles =  File(spotPath).walk().maxDepth(1).toList()

        for (i in 2..imgFiles.size) {
            imgPaths += imgFiles[i-1].toString()
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
                text3.visibility = View.GONE
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

        val listView = findViewById<ListView>(R.id.sampleListView)
        listView.adapter = adapter1 //Setting adapter and listener to start song level when clicked.
        listView.onItemClickListener = AdapterView.OnItemClickListener { arg0, arg1, arg2, arg3 ->
            Log.e("arg3", arg3.toString())
            ImageViewer.Builder(this, pics)
                    .setStartPosition(arg3.toInt())
                    .hideStatusBar(false)
                    .allowSwipeToDismiss(true)
                    .show()
        }

        with(listView) {
            choiceMode = ListView.CHOICE_MODE_MULTIPLE_MODAL
            setMultiChoiceModeListener(object : AbsListView.MultiChoiceModeListener {
                override fun onItemCheckedStateChanged(mode: ActionMode, position: Int,
                                                       id: Long, checked: Boolean) {
                    if (checkedItemCount <2 || checkedItemCount >2) {
                        mode.title = "Select images"
                        mode.menu.findItem(R.id.compare).isEnabled = false
                    } else {
                        mode.menu.findItem(R.id.compare).isEnabled = true
                    }
                }

                override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean {
                    return when (item.itemId) {
                        R.id.compare -> {
                            var selectedSpotsPaths = emptyArray<String>()
                            for (i in 0..checkedItemPositions.size()) {
                                Log.e("comparecheck", checkedItemPositions.get(i).toString())
                                if (checkedItemPositions.get(i)) {
                                    selectedSpotsPaths += imgPaths[i]
                                }
                            }

                            val firstSpot = selectedSpotsPaths[0]
                            val secondSpot = selectedSpotsPaths[1]

                            val intent = Intent(this@SpotImageList, TrackSpotScreen::class.java)
                            intent.putExtra("firstSpot", firstSpot)
                            intent.putExtra("secondSpot", secondSpot)
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
                }

                override fun onPrepareActionMode(mode: ActionMode, menu: Menu): Boolean {
                    return false
                }
            })
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.e("BodyScreen", "onActivityResult")

        if (requestCode == UCrop.REQUEST_CROP) {
                val intent = Intent(this, SpotImageList::class.java)
                intent.putExtra("selectedBodyPart", selectedBodyPart)
                intent.putExtra("spotName", spotName)
                intent.putExtra("spotPath", spotPath)
                intent.putExtra("spotImageName", "placeholder")
                startActivity(intent)
            }

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {

            val cropOptions = UCrop.Options().apply {
                setAllowedGestures(0,0,0)
            }

            UCrop.of(Uri.parse("file://"+mCurrentPhotoPath), Uri.parse("file://"+mCurrentPhotoPath))
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