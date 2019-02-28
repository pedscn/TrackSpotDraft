package com.dev.pedroschulze.trackspotdraft

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button

class FragmentTwo : Fragment() {

    private val selectedBodySide = "back"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_two, container, false)
    }

    companion object {
        fun newInstance(): FragmentTwo = FragmentTwo()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        createMainMenu()
    }

    private fun openBodyPart(selectedBodyPart : String) {
        val intent = Intent(this.activity, OldSpotScreen::class.java)
        intent.putExtra("selectedBodyPart", selectedBodyPart)
        intent.putExtra("selectedBodySide", selectedBodySide)
        startActivity(intent)
    }

    private fun createMainMenu() {

        val btnLeftArm = view?.findViewById(R.id.btn_left_arm) as Button
        btnLeftArm.setOnClickListener {
            openBodyPart("leftarm")
        }
        val btnRightArm = view?.findViewById(R.id.btn_right_arm) as Button
        btnRightArm.setOnClickListener {
            openBodyPart("rightarm")
        }
        val btnBack = view?.findViewById(R.id.btn_back) as Button
        btnBack.setOnClickListener {
            openBodyPart("back")
        }
        val btnRightLeg = view?.findViewById(R.id.btn_right_leg) as Button
        btnRightLeg.setOnClickListener {
            openBodyPart("rightleg")
        }
        val btnLeftLeg = view?.findViewById(R.id.btn_left_leg) as Button
        btnLeftLeg.setOnClickListener {
            openBodyPart("leftleg")
        }
        val btnHead = view?.findViewById(R.id.btn_head) as Button
        btnHead.setOnClickListener {
            openBodyPart("head")
        }
    }
}