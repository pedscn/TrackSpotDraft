package com.example.pedroschulze.trackspotdraft

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.util.Log

import kotlinx.android.synthetic.main.activity_track_spot_screen.*
import swipeable.com.layoutmanager.OnItemSwiped
import swipeable.com.layoutmanager.SwipeableTouchHelperCallback
import android.support.v7.widget.AppCompatButton
import swipeable.com.layoutmanager.SwipeableLayoutManager
import swipeable.com.layoutmanager.touchelper.ItemTouchHelper


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

        var adapter = ListAdapter()
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        val swipeableTouchHelperCallback = object : SwipeableTouchHelperCallback(object : OnItemSwiped {
            override fun onItemSwiped() {
                adapter.removeTopItem()
            }

            override fun onItemSwipedLeft() {
                Log.e("SWIPE", "LEFT")
            }

            override fun onItemSwipedRight() {
                Log.e("SWIPE", "RIGHT")
            }

            override fun onItemSwipedUp() {
                Log.e("SWIPE", "UP")
            }

            override fun onItemSwipedDown() {
                Log.e("SWIPE", "DOWN")
            }
        }) {
            override fun getAllowedSwipeDirectionsMovementFlags(viewHolder: RecyclerView.ViewHolder): Int {
                return ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT
            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeableTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
        recyclerView.layoutManager = SwipeableLayoutManager().setAngle(10)
                .setAnimationDuratuion(450)
                .setMaxShowCount(3)
                .setScaleGap(0.1f)
                .setTransYGap(0)
        adapter = ListAdapter()
        recyclerView.adapter = adapter

    //    val button = findViewById(R.id.swipe)
    //    button.setOnClickListener(object : View.OnClickListener() {
    //        fun onClick(v: View) {
    //            itemTouchHelper.swipe(recyclerView.findViewHolderForAdapterPosition(0), ItemTouchHelper.DOWN)
    //        }
    //    })

    }

}
