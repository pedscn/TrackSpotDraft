package com.example.pedroschulze.trackspotdraft

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.bumptech.glide.Glide

import kotlinx.android.synthetic.main.activity_track_spot_screen.*
import java.io.File
import java.net.URI
import android.content.Intent
import android.support.v4.content.FileProvider
import android.support.v7.widget.Toolbar


class TrackSpotScreen : AppCompatActivity() {
    lateinit var firstSpot : String
    lateinit var secondSpot : String

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.comparebar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.action_email -> {
            val intent = Intent(Intent.ACTION_SEND_MULTIPLE)
            intent.apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_EMAIL, arrayOf("trackspotandroid@inf.ed.ac.uk"))
                putExtra(Intent.EXTRA_SUBJECT, "Skin Spots")
                putExtra(Intent.EXTRA_TEXT, "Insert your message here")
                Log.e("absolute on File",File(firstSpot).absoluteFile.toString())
                Log.e("File, no absolute", File(firstSpot).toString())
                Log.e("asboluteFile", File(firstSpot).absoluteFile.toString())
                Log.e("No file", firstSpot)
                val uris = ArrayList<Uri>()
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                uris += FileProvider.getUriForFile(this@TrackSpotScreen, "my.package.name.provider", File(firstSpot))
                uris += FileProvider.getUriForFile(this@TrackSpotScreen, "my.package.name.provider", File(secondSpot))
                putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris)
                startActivity(Intent.createChooser(intent, "Send email..."))
            }
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_track_spot_screen)
        setSupportActionBar(toolbar as Toolbar)
        title = "Spot Comparison"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)



        //val spots = intent.getStringExtra("selectedSpots")
        firstSpot = intent.getStringExtra("firstSpot")
        secondSpot = intent.getStringExtra("secondSpot")

        //for (spot in spots) {
        //    Log.e("spots", spot.toString())
        //}

         Glide.with(this@TrackSpotScreen)
                .load(BitmapFactory.decodeFile(File(firstSpot).absolutePath))
                 .into(topImage)
        Glide.with(this@TrackSpotScreen)
                .load(BitmapFactory.decodeFile(File(secondSpot).absolutePath))
                .into(bottomImage)
    }
}
