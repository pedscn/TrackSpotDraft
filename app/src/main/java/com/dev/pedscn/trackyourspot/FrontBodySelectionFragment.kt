package com.dev.pedscn.trackyourspot

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dev.pedscn.trackyourspot.BodyParts.HEAD
import com.dev.pedscn.trackyourspot.BodyParts.LEFT_ARM
import com.dev.pedscn.trackyourspot.BodyParts.LEFT_LEG
import com.dev.pedscn.trackyourspot.BodyParts.RIGHT_ARM
import com.dev.pedscn.trackyourspot.BodyParts.RIGHT_LEG
import com.dev.pedscn.trackyourspot.BodyParts.TORSO
import com.dev.pedscn.trackyourspot.BodySides.FRONT
import kotlinx.android.synthetic.main.fragment_front_body_selection.*

class FrontBodySelectionFragment : Fragment() {

    private val selectedBodySide = FRONT

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_front_body_selection, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        createMainMenu()
    }

    private fun openBodyPart(selectedBodyPart: String) {
        val intent = Intent(this.activity, OldSpotListActivity::class.java)
        intent.putExtra(SELECTED_BODY_PART, selectedBodyPart)
        intent.putExtra(SELECTED_BODY_SIDE, selectedBodySide)
        startActivity(intent)
    }

    private fun createMainMenu() {

        val btnLeftArm = btn_left_arm_front_body
        btnLeftArm.setOnClickListener {
            openBodyPart(LEFT_ARM)
        }
        val btnRightArm = btn_right_arm_front_body
        btnRightArm.setOnClickListener {
            openBodyPart(RIGHT_ARM)
        }
        val btnTorso = btn_torso_front_body
        btnTorso.setOnClickListener {
            openBodyPart(TORSO)
        }
        val btnLeftLeg = btn_left_leg_front_body
        btnLeftLeg.setOnClickListener {
            openBodyPart(LEFT_LEG)
        }
        val btnRightLeg = btn_right_leg_front_body
        btnRightLeg.setOnClickListener {
            openBodyPart(RIGHT_LEG)
        }
        val btnHead = btn_head_front_body
        btnHead.setOnClickListener {
            openBodyPart(HEAD)
        }
    }

    companion object {
        fun newInstance(): FrontBodySelectionFragment = FrontBodySelectionFragment()
        const val SELECTED_BODY_PART = "selectedBodyPart"
        const val SELECTED_BODY_SIDE = "selectedBodySide"
    }
}