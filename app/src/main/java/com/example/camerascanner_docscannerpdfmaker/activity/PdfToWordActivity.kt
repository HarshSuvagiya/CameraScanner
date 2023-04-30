package com.example.camerascanner_docscannerpdfmaker.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.camerascanner_docscannerpdfmaker.R
import kotlinx.android.synthetic.main.header.*

class PdfToWordActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pdf_to_word)

        headerTitle.text = "PDF TO WORD"
        info1.setOnClickListener {
            startActivity(Intent(applicationContext,IntroActivity::class.java).putExtra("fromWhere","everyWhere"))
        }

        back.setOnClickListener {
            finish()
        }
    }
}