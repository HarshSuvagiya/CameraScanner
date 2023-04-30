package com.example.camerascanner_docscannerpdfmaker.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.Color.blue
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.camerascanner_docscannerpdfmaker.R
import com.example.camerascanner_docscannerpdfmaker.constant.KeyConstants
import com.github.appintro.AppIntro
import com.github.appintro.AppIntro2
import com.github.appintro.AppIntroFragment
import com.github.appintro.AppIntroPageTransformerType

class IntroActivity : AppIntro2() {

    lateinit var sharedPreferences: SharedPreferences
    lateinit var editor: SharedPreferences.Editor
    lateinit var fromWhere: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPreferences = getSharedPreferences(KeyConstants.sharedPreferencesKey, Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()

        fromWhere = intent.getStringExtra("fromWhere")

        if (fromWhere.equals("splash"))
            if (sharedPreferences.getBoolean(KeyConstants.isFirstTime, false))
                CallMainActivity()

        setTransformer(AppIntroPageTransformerType.Fade)
        addSlide(
                AppIntroFragment.newInstance(
                        backgroundDrawable = R.drawable.intro1
                )
        )
        addSlide(
                AppIntroFragment.newInstance(
                        backgroundDrawable = R.drawable.intro2
                )
        )
        addSlide(
                AppIntroFragment.newInstance(
                        backgroundDrawable = R.drawable.intro3
                )
        )
        setImmersiveMode()

        setIndicatorColor(
                selectedIndicatorColor = resources.getColor(R.color.greyDark),
                unselectedIndicatorColor = resources.getColor(R.color.grey)
        )
    }

    override fun onSkipPressed(currentFragment: Fragment?) {
        super.onSkipPressed(currentFragment)
        CallMainActivity()
    }

    override fun onDonePressed(currentFragment: Fragment?) {
        super.onDonePressed(currentFragment)
        CallMainActivity()
    }

    fun CallMainActivity() {
        if (fromWhere.equals("splash")) {
            startActivity(Intent(applicationContext, StartActivity::class.java))
            finish()
        } else
            finish()
    }

}