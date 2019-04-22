package com.dev.pedscn.trackyourspot

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dev.pedscn.trackyourspot.BodySides.BACK
import com.dev.pedscn.trackyourspot.BodyParts.HEAD
import com.dev.pedscn.trackyourspot.BodyParts.LEFT_ARM
import com.dev.pedscn.trackyourspot.BodyParts.LEFT_LEG
import com.dev.pedscn.trackyourspot.BodyParts.RIGHT_ARM
import com.dev.pedscn.trackyourspot.BodyParts.RIGHT_LEG
import kotlinx.android.synthetic.main.fragment_back_body_selection.*

class BackBodySelectionFragment : Fragment() {

    private val selectedBodySide = BACK

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_back_body_selection, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        createMainMenu()
    }

    private fun openBodyPart(selectedBodyPart : String) {
        val intent = Intent(this.activity, OldSpotListActivity::class.java)
        intent.putExtra(SELECTED_BODY_PART, selectedBodyPart)
        intent.putExtra(SELECTED_BODY_SIDE, selectedBodySide)
        startActivity(intent)
    }

    private fun createMainMenu() {

        btn_left_arm_back_body.setOnClickListener {
            openBodyPart(LEFT_ARM)
        }
        btn_right_arm_back_body.setOnClickListener {
            openBodyPart(RIGHT_ARM)
        }
        btn_back_back_body.setOnClickListener {
            openBodyPart(BACK)
        }
        btn_right_leg_back_body.setOnClickListener {
            openBodyPart(RIGHT_LEG)
        }
        btn_left_leg_back_body.setOnClickListener {
            openBodyPart(LEFT_LEG)
        }
        btn_head_back_body.setOnClickListener {
            openBodyPart(HEAD)
        }
    }

    companion object {
        fun newInstance(): BackBodySelectionFragment = BackBodySelectionFragment()
        const val SELECTED_BODY_PART = "selectedBodyPart"
        const val SELECTED_BODY_SIDE = "selectedBodySide"
    }
}