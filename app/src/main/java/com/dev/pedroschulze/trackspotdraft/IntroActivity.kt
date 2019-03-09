package com.dev.pedroschulze.trackspotdraft

import android.content.Intent
import android.os.Bundle
import com.github.paolorotolo.appintro.AppIntroFragment
import android.graphics.Color
import android.support.annotation.Nullable
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.Window
import com.github.paolorotolo.appintro.AppIntro
import com.github.paolorotolo.appintro.AppIntro2
import com.github.paolorotolo.appintro.model.SliderPage

class IntroActivity : AppIntro2() {

    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()

        val introSliderPage = SliderPage().apply {
            title = "Welcome to TrackYourSpot"
            description = "This app will help you closely monitor your skin spots. Swipe to start tracking."
            imageDrawable = R.drawable.icon_large
            bgColor = ContextCompat.getColor(applicationContext, R.color.colorPrimary)
        }

        val firstSliderPage = SliderPage().apply {
            title = "Skin Cancer"
            description = "Some cancers are slow growing and may not need treatment. Other cases, such as malignant melanoma, are highly dangerous and need immediate treatment."
            imageDrawable = R.drawable.melanoma
            bgColor = Color.parseColor("#9494b8")
        }

        val secondSliderPage = SliderPage().apply {
            title = "Spot Changes"
            description = "There are many normal reasons why a spot might change appearance. Similarly, an existing spot could become cancerous without much change to its appearance."
            imageDrawable = R.drawable.magnifying_glass
            bgColor = Color.parseColor("#0bcbdf")
        }

        val thirdSliderPage = SliderPage().apply {
            title = "Skin Protection"
            description = "Sun exposure is a major factor in skin cancer. Always use appropriate skin protection."
            imageDrawable = R.drawable.finalskinpatch
            bgColor = Color.parseColor("#00cc7a")
        }

        val fourthSliderPage = SliderPage().apply {
            title = "Consult Your Doctor"
            description = "You should always see your doctor if there is anything about a spot that worries you."
            imageDrawable = R.drawable.doctorclipart
            bgColor = Color.parseColor("#AB47BC")
        }
        addSlide(AppIntroFragment.newInstance(introSliderPage))
        addSlide(AppIntroFragment.newInstance(firstSliderPage))
        addSlide(AppIntroFragment.newInstance(secondSliderPage))
        addSlide(AppIntroFragment.newInstance(thirdSliderPage))
        addSlide(AppIntroFragment.newInstance(fourthSliderPage))

        //setBarColor(Color.parseColor("#3F51B5"))
        //setSeparatorColor(Color.parseColor("#2196F3"))

        showSkipButton(true)
        isProgressButtonEnabled = true
    }

    override fun onSkipPressed(currentFragment: Fragment?) {
        val intent = Intent(this, BodyScreen::class.java)
        startActivity(intent)
    }

    override fun onDonePressed(currentFragment: Fragment?) {
        val intent = Intent(this, BodyScreen::class.java)
        startActivity(intent)
    }

    override fun onSlideChanged(oldFragment: Fragment?, newFragment: Fragment?) {
        super.onSlideChanged(oldFragment, newFragment)
        // Do something when the slide changes.
    }
}