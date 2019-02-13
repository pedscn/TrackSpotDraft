package com.example.pedroschulze.trackspotdraft

import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_track_spot_screen.*
import java.io.File
import android.content.Intent
import android.support.v4.content.FileProvider
import android.support.v7.widget.Toolbar


class TrackSpotScreen : AppCompatActivity() {
    lateinit var firstSpotToCompare: String
    lateinit var secondSpotToCompare: String

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
                val uris = ArrayList<Uri>()
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                uris += FileProvider.getUriForFile(this@TrackSpotScreen, "my.package.name.provider", File(firstSpotToCompare))
                uris += FileProvider.getUriForFile(this@TrackSpotScreen, "my.package.name.provider", File(secondSpotToCompare))
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
        loadSpotComparison()
    }

    private fun loadSpotComparison() {
        firstSpotToCompare = intent.getStringExtra("firstSpotToCompare")
        secondSpotToCompare = intent.getStringExtra("secondSpotToCompare")
        Glide.with(this@TrackSpotScreen)
                .load(BitmapFactory.decodeFile(File(firstSpotToCompare).absolutePath))
                .into(topImage)
        Glide.with(this@TrackSpotScreen)
                .load(BitmapFactory.decodeFile(File(secondSpotToCompare).absolutePath))
                .into(bottomImage)
    }
}
