package com.example.pedroschulze.trackspotdraft

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Button

class BodyScreen : AppCompatActivity() {

    private var selectedBodyPart = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_body_screen)
        setSupportActionBar(findViewById(R.id.my_toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        createMainMenu()
    }

    private fun createMainMenu() {
        val btnLeftArm = findViewById<Button>(R.id.btn_left_arm) as Button //Required some googling due to sdk version not compatible with inference.
        btnLeftArm.setOnClickListener {
            selectedBodyPart = "leftarm"
            val intent = Intent(this, OldSpotScreen::class.java)
            intent.putExtra("selectedBodyPart", selectedBodyPart)
            startActivity(intent)
        }

        val btnTorso = findViewById<Button>(R.id.btn_torso) as Button
        btnTorso.setOnClickListener {
            selectedBodyPart = "torso"
            val intent = Intent(this, OldSpotScreen::class.java)
            intent.putExtra("selectedBodyPart", selectedBodyPart)
            startActivity(intent)
        }
    }

    override fun onBackPressed() {
        //TODO
    }
}

