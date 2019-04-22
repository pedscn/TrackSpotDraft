package com.dev.pedscn.trackyourspot

import FragmentAdapter
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.dev.pedscn.trackyourspot.BodySides.FRONT
import kotlinx.android.synthetic.main.activity_body_screen.*

class BodyScreenActivity : AppCompatActivity() {
    private lateinit var selectedBodySide : String

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.bodybar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.action_info -> {
            val intent = Intent(this, IntroActivity::class.java)
            startActivity(intent)
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_body_screen)
        val bodyScreenRelLayout = bodyscreen_rellayout
        bodyScreenRelLayout.setBackgroundColor(resources.getColor(R.color.white))
        setSupportActionBar(findViewById(R.id.body_screen_toolbar))
        selectedBodySide = if (intent.hasExtra("selectedBodySide")) {
            intent.getStringExtra("selectedBodySide")
        } else {
            FRONT
        }
        createTabs(selectedBodySide)
        val sharedPref = this.getPreferences(Context.MODE_PRIVATE) ?: return
        val isTutorialDone = sharedPref.getBoolean("TutorialDone", false)
        if (!isTutorialDone) {
            sharedPref.edit().putBoolean("TutorialDone", true).apply()
            val intent = Intent(this, IntroActivity::class.java)
            startActivity(intent)
        }
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

