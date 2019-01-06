package com.example.pedroschulze.trackspotdraft

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.gtomato.android.ui.transformer.CoverFlowViewTransformer
import kotlinx.android.synthetic.main.activity_track_spot_screen.*
import android.view.Gravity
import com.gtomato.android.ui.transformer.LinearViewTransformer


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
        var adapter1 = ListAdapter()
        Log.e("adapter items", adapter.items.toString())
        Log.e("adapter count", adapter.itemCount.toString())

        carousel1.transformer = CoverFlowViewTransformer()
        carousel1.adapter = adapter
        carousel1.gravity = Gravity.CENTER
        carousel1.isScrollingAlignToViews = true
        carousel1.clipChildren = true
        carousel1.extraVisibleChilds = 1
        carousel1.isEnableFling = false
        carousel1.smoothScrollToPosition(carousel1.adapter.itemCount - 1);

        carousel2.transformer = CoverFlowViewTransformer()
        carousel2.adapter = adapter1
        carousel2.setGravity(Gravity.CENTER)
        carousel2.setScrollingAlignToViews(true);
        carousel2.setClipChildren(false);
        carousel2.setExtraVisibleChilds(4);
        carousel2.setEnableFling(false);


    }

}
