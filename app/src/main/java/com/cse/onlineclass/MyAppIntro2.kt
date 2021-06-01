package com.cse.onlineclass

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.github.appintro.AppIntro
import com.github.appintro.AppIntroFragment
import com.github.appintro.AppIntroPageTransformerType
import kotlin.system.exitProcess


class MyAppIntro2 : AppIntro() {

    private val PREF_NAME = "Starting"


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sharedPreferences: SharedPreferences = this.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE)
        if (sharedPreferences.getBoolean("Intro", false)) {
            val homeIntent = Intent(this@MyAppIntro2, MainActivity::class.java)
            startActivity(homeIntent)
            finish()}
        else {
            addSlide(AppIntroFragment.newInstance(
                    title = "Welcome Engineer",
                    description = "Join Class Using Nandha Domain",
                    imageDrawable = R.drawable.firstscreen,
                    backgroundDrawable = R.drawable.back1,
                    titleColor = Color.MAGENTA,
                    descriptionColor = Color.RED,
                    backgroundColor = Color.GREEN,
                    titleTypefaceFontRes = R.font.title,
                    descriptionTypefaceFontRes = R.font.description
            ))
            addSlide(AppIntroFragment.newInstance(
                    title = "Thank You",
                    description = "For Download Our App",
                    imageDrawable = R.drawable.secondscreen,
                    backgroundDrawable = R.drawable.back2,
                    titleColor = Color.RED,
                    descriptionColor = Color.GREEN,
                    backgroundColor = Color.RED,
                    titleTypefaceFontRes = R.font.description,
                    descriptionTypefaceFontRes = R.font.title
            ))
            isIndicatorEnabled = true
            setIndicatorColor(
                    selectedIndicatorColor = getColor(R.color.firstscreen),
                    unselectedIndicatorColor = getColor(R.color.secondscreen)
            )
            isVibrate = true
            vibrateDuration = 50L
            setTransformer(AppIntroPageTransformerType.Parallax(
                    titleParallaxFactor = 1.0,
                    imageParallaxFactor = -1.0,
                    descriptionParallaxFactor = 2.0
            ))
        }
    }

    override fun onSkipPressed(currentFragment: Fragment?) {
        super.onSkipPressed(currentFragment)
        exitProcess(0)
    }

    override fun onDonePressed(currentFragment: Fragment?) {
        super.onDonePressed(currentFragment)
        val sharedPreferences: SharedPreferences = this.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE)
        val editor:SharedPreferences.Editor =  sharedPreferences.edit()
        editor.putBoolean("Intro",true)
        editor.commit()
        val intent = Intent(this@MyAppIntro2, MainActivity::class.java) // (1) (2)
        startActivity(intent)
        finish()
    }
}
