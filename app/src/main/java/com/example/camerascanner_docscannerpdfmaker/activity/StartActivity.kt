package com.example.camerascanner_docscannerpdfmaker.activity

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.camerascanner_docscannerpdfmaker.R
import com.example.camerascanner_docscannerpdfmaker.constant.KeyConstants
import kotlinx.android.synthetic.main.activity_start.*

class StartActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        editor.putBoolean(KeyConstants.isFirstTime,true)
        editor.apply()

        start.setOnClickListener {
            startActivity(Intent(applicationContext,Start2Activity::class.java))
        }

        privacy.setOnClickListener{
            val browserIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://www.google.com")
            )
            startActivity(browserIntent)
        }

        share.setOnClickListener {
            try {
                val intent = Intent("android.intent.action.SEND")
                intent.type = "text/plain"
                intent.putExtra(
                    "android.intent.extra.SUBJECT", "Have a look at " +
                            getString(R.string.app_name) + " app "
                )
                intent.putExtra(
                    "android.intent.extra.TEXT",
                    """Hey Check out this new ${getString(R.string.app_name)} App - ${getString(R.string.app_name)} 
                    Available on Google play store,You can also download it from "https://play.google.com/store/apps/details?id=$packageName""""
                )
                startActivity(Intent.createChooser(intent, "Share via"))
            } catch (e: java.lang.Exception) {
                Toast.makeText(
                    applicationContext, "Something went wrong while sharing our app!.Sorry " +
                            "for the inconvenience .Please try again", Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}