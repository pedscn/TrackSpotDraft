package com.example.pedroschulze.trackspotdraft

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import kotlinx.android.synthetic.main.fragment_one.*

class FragmentOne : Fragment() {

    lateinit var selectedBodyPart : String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        //createMainMenu()
        return inflater!!.inflate(R.layout.fragment_one, container, false)
    }


    companion object {
        fun newInstance(): FragmentOne = FragmentOne()
    }

    /*private fun createMainMenu() {
        val btnLeftArm = btn_left_arm//Required some googling due to sdk version not compatible with inference.
        btnLeftArm.setOnClickListener {
            selectedBodyPart = "leftarm"
            val intent = Intent(context, OldSpotScreen::class.java)
            intent.putExtra("selectedBodyPart", selectedBodyPart)
            startActivity(intent)
        }

        val btnTorso = btn_torso
        btnTorso.setOnClickListener {
            selectedBodyPart = "torso"
            val intent = Intent(context, OldSpotScreen::class.java)
            intent.putExtra("selectedBodyPart", selectedBodyPart)
            startActivity(intent)
        }
    }*/
}