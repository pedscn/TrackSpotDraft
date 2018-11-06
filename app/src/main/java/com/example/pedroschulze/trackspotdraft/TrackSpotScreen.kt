package com.example.pedroschulze.trackspotdraft

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.util.Log

import kotlinx.android.synthetic.main.activity_track_spot_screen.*

class TrackSpotScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_track_spot_screen)

        val limb = intent.getStringExtra("limb")
        val spotName = intent.getStringExtra("spotName")
        val spotPath = intent.getStringExtra("spotPath")
        val spotPicname = intent.getStringExtra("spotPicname")
        Log.e("spotName", spotName)
        Log.e("spotPath", spotPath)
        Log.e("spotPicname", spotPicname)
        Log.e("limb", limb)

    }

}
