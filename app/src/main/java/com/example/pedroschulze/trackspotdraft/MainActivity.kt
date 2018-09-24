package com.example.pedroschulze.trackspotdraft

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btn_new_spot = findViewById<Button>(R.id.btn_new_spot) as Button //Required some googling due to sdk version not compatible with inference.
        btn_new_spot.setOnClickListener {
            Toast.makeText(this@MainActivity, "Adding a New Spot.", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, NewSpotScreen::class.java)
            startActivity(intent)
        }
        val btn_old_spot = findViewById<Button>(R.id.btn_old_spot) as Button //Required some googling due to sdk version not compatible with inference.
        btn_old_spot.setOnClickListener {
            Toast.makeText(this@MainActivity, "Adding an Old Spot", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, OldSpotScreen::class.java)
            startActivity(intent)
        }
        val btn_view_spots = findViewById<Button>(R.id.btn_view_spot) as Button //Required some googling due to sdk version not compatible with inference.
        btn_view_spots.setOnClickListener {
            Toast.makeText(this@MainActivity, "Tracking a Spot", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, TrackSpotScreen::class.java)
            startActivity(intent)
        }
        val btn_settings = findViewById<Button>(R.id.btn_settings) as Button //Required some googling due to sdk version not compatible with inference.
        btn_settings.setOnClickListener {
            Toast.makeText(this@MainActivity, "Opening Settings", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, SettingsScreen::class.java)
            startActivity(intent)
        }
    }
}
