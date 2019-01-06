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
        val intent = Intent(this, BodyScreen::class.java)
        intent.putExtra("option", "placeholder")
        startActivity(intent)
    }
}
