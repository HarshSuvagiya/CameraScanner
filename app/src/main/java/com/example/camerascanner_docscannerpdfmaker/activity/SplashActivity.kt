package com.example.camerascanner_docscannerpdfmaker.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import com.example.camerascanner_docscannerpdfmaker.R
import com.example.camerascanner_docscannerpdfmaker.constant.KeyConstants

class SplashActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        object : CountDownTimer(2000, 1000) {
            override fun onFinish() {
                if (sharedPreferences.getBoolean(KeyConstants.isFirstTime, false))
                    startActivity(Intent(applicationContext, StartActivity::class.java))
                else
                    startActivity(Intent(applicationContext, IntroActivity::class.java).putExtra("fromWhere","splash"))
                finish()
            }

            override fun onTick(millisUntilFinished: Long) {
            }

        }.start()

    }
}