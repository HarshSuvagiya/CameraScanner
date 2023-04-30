package com.example.camerascanner_docscannerpdfmaker.activity

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.camerascanner_docscannerpdfmaker.R
import kotlinx.android.synthetic.main.activity_text.*
import kotlinx.android.synthetic.main.header.*

class TextActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_text)

        headerTitle.text = "SCANNED TEXT"

        back.setOnClickListener {
            finish()
        }
        info1.setOnClickListener {
            startActivity(Intent(applicationContext,IntroActivity::class.java).putExtra("fromWhere","everyWhere"))
        }

        code.text = intent.getStringExtra("scannedText")
        copyCode.setOnClickListener {
            val clipboard: ClipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip: ClipData = ClipData.newPlainText("label", code.text)
            clipboard.setPrimaryClip(clip)
            Toast.makeText(applicationContext,"Code copied!!!",Toast.LENGTH_LONG).show()
        }
    }
}