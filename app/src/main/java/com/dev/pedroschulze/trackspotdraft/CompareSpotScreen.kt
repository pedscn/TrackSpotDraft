package com.dev.pedroschulze.trackspotdraft

import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_compare_spot_screen.*
import java.io.File
import android.content.Intent
import android.support.v4.content.FileProvider
import android.support.v7.widget.Toolbar
import com.bumptech.glide.request.RequestOptions


class CompareSpotScreen : AppCompatActivity() {
    private lateinit var firstSpotToCompare: String
    private lateinit var secondSpotToCompare: String
    private lateinit var selectedBodyPart: String
    private lateinit var selectedBodySide: String
    private lateinit var spotName: String
    private lateinit var spotDirectory: String

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.comparebar, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.action_email -> {
            val intent = Intent(Intent.ACTION_SEND_MULTIPLE)
            intent.apply {
                type = "text/plain"
                //putExtra(Intent.EXTRA_EMAIL, arrayOf("email@example.com"))
                putExtra(Intent.EXTRA_SUBJECT, "Skin Spots")
                putExtra(Intent.EXTRA_TEXT, "Insert your message here")
                val uris = ArrayList<Uri>()
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                uris += FileProvider.getUriForFile(this@CompareSpotScreen, "my.package.name.provider", File(firstSpotToCompare))
                uris += FileProvider.getUriForFile(this@CompareSpotScreen, "my.package.name.provider", File(secondSpotToCompare))
                putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris)
                startActivity(Intent.createChooser(intent, "Send email..."))
            }
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_compare_spot_screen)
        setSupportActionBar(track_spot_screen_toolbar as Toolbar)
        title = "Spot Comparison"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        selectedBodyPart = intent.getStringExtra("selectedBodyPart")
        selectedBodySide = intent.getStringExtra("selectedBodySide")
        spotName = intent.getStringExtra("spotName")
        spotDirectory = intent.getStringExtra("spotDirectory")
        loadSpotComparison()
    }

    private fun loadSpotComparison() {
        firstSpotToCompare = intent.getStringExtra("firstSpotToCompare")
        secondSpotToCompare = intent.getStringExtra("secondSpotToCompare")
        Glide.with(this@CompareSpotScreen)
                .load(BitmapFactory.decodeFile(File(firstSpotToCompare).absolutePath))
                .apply(RequestOptions().fitCenter())
                .into(topImage)
        Glide.with(this@CompareSpotScreen)
                .load(BitmapFactory.decodeFile(File(secondSpotToCompare).absolutePath))
                .apply(RequestOptions().fitCenter())
                .into(bottomImage)
    }

    override fun onBackPressed() {
        val intent = Intent(this, SpotImageList::class.java)
        intent.putExtra("selectedBodySide", selectedBodySide)
        intent.putExtra("selectedBodyPart", selectedBodyPart)
        intent.putExtra("spotName", spotName)
        intent.putExtra("spotDirectory", spotDirectory)
        startActivity(intent)
    }
}
