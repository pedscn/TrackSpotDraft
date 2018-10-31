package com.example.pedroschulze.trackspotdraft

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnNewSpot = findViewById<Button>(R.id.btn_new_spot) as Button //Required some googling due to sdk version not compatible with inference.
        btnNewSpot.setOnClickListener {
            Toast.makeText(this@MainActivity, "Adding a New Spot.", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, BodyScreen::class.java)
            intent.putExtra("MainMenuAction", "Newspot");
            startActivity(intent)
        }
        val btnOldSpot = findViewById<Button>(R.id.btn_old_spot) as Button //Required some googling due to sdk version not compatible with inference.
        btnOldSpot.setOnClickListener {
            Toast.makeText(this@MainActivity, "Adding an Old Spot", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, BodyScreen::class.java)
            intent.putExtra("MainMenuAction", "Oldspot");
            startActivity(intent)
        }
        val btnViewSpots = findViewById<Button>(R.id.btn_view_spot) as Button //Required some googling due to sdk version not compatible with inference.
        btnViewSpots.setOnClickListener {
            Toast.makeText(this@MainActivity, "Tracking a Spot", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, TrackSpotScreen::class.java)
            startActivity(intent)
        }
        val btnSettings = findViewById<Button>(R.id.btn_settings) as Button //Required some googling due to sdk version not compatible with inference.
        btnSettings.setOnClickListener {
            Toast.makeText(this@MainActivity, "Opening Settings", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, SettingsScreen::class.java)
            startActivity(intent)
        }
    }
}
