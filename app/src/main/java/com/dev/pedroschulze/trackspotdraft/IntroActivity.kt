package com.dev.pedroschulze.trackspotdraft

import android.content.Intent
import android.os.Bundle
import com.github.paolorotolo.appintro.AppIntroFragment
import android.graphics.Color
import android.support.annotation.Nullable
import android.support.v4.app.Fragment
import android.view.Window
import com.github.paolorotolo.appintro.AppIntro
import com.github.paolorotolo.appintro.model.SliderPage

class IntroActivity : AppIntro() {

    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()

        val firstSliderPage = SliderPage().apply {
            title = "Skin Cancer"
            description = "There are many different types of skin cancer. You probably have heard of malignant melanoma, which is a highly dangerous cancer and needs immediate treatment. Some cancers are slow growing and may not need treatment."
            //descColor = resources.getColor(R.color.black)
            imageDrawable = R.drawable.melanoma
            bgColor = Color.parseColor("#ff9999")
        }

        val secondSliderPage = SliderPage().apply {
            title = "Spot Changes"
            description = "There are many normal reasons why a spot might change appearance and a change does not mean cancer. Similarly, an existing spot could become cancerous without much change to its appearance."
            //descColor = resources.getColor(R.color.black)
            imageDrawable = R.drawable.magnifying_glass
            bgColor = resources.getColor(R.color.colorPrimary)
        }

        val thirdSliderPage = SliderPage().apply {
            title = "Skin Protection"
            description = "Sun exposure is a major factor in skin cancer. Always use appropriate skin protection."
            //descColor = resources.getColor(R.color.black)
            imageDrawable = R.drawable.finalskinpatch
            bgColor = Color.parseColor("#00cc7a")
        }

        val fourthSliderPage = SliderPage().apply {
            title = "Consult Your Doctor"
            description = "You should see your doctor if there is anything about a spot that worries you."
            //descColor = resources.getColor(R.color.black)
            imageDrawable = R.drawable.doctorclipart
            bgColor = Color.parseColor("#9494b8")
        }
        addSlide(AppIntroFragment.newInstance(firstSliderPage))
        addSlide(AppIntroFragment.newInstance(secondSliderPage))
        addSlide(AppIntroFragment.newInstance(thirdSliderPage))
        addSlide(AppIntroFragment.newInstance(fourthSliderPage))

        //setBarColor(Color.parseColor("#3F51B5"))
        //setSeparatorColor(Color.parseColor("#2196F3"))

        // Hide Skip/Done button.
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