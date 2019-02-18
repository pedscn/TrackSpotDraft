package com.example.pedroschulze.trackspotdraft

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import FragmentAdapter

class BodyScreen : AppCompatActivity() {
    private lateinit var selectedBodySide : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_body_screen)
        setSupportActionBar(findViewById(R.id.body_screen_toolbar))
        //supportActionBar?.setDisplayHomeAsUpEnabled(true)
        selectedBodySide = if (intent.hasExtra("selectedBodySide")) {
            intent.getStringExtra("selectedBodySide")
        } else {
            "front"
        }
        createTabs(selectedBodySide)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun createTabs(selectedBodySide : String) {
        val tabLayout: TabLayout = findViewById(R.id.tab_layout) //Ignore Android Studio Error
        val viewPager: ViewPager = findViewById(R.id.view_pager)
        val adapter = FragmentAdapter(supportFragmentManager)
        viewPager.adapter = adapter
        tabLayout.setupWithViewPager(viewPager)
        tabLayout.tabMode = TabLayout.MODE_FIXED
        tabLayout.tabGravity = TabLayout.GRAVITY_FILL
        val frontTab = tabLayout.getTabAt(0)
        val backTab = tabLayout.getTabAt(1)
        if (selectedBodySide=="back") {
            backTab?.select()
        }
        frontTab?.text = "Front"
        backTab?.text = "Back"
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                //TODO
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                //TODO
            }

            override fun onTabReselected(tab: TabLayout.Tab) {
                //TODO
            }
        })
    }

    override fun onBackPressed() {
        //TODO
    }
}

