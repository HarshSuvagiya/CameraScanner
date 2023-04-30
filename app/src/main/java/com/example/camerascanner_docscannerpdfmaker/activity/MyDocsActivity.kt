package com.example.camerascanner_docscannerpdfmaker.activity

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.camerascanner_docscannerpdfmaker.R
import com.example.camerascanner_docscannerpdfmaker.fragment.AllDocFragment
import com.example.camerascanner_docscannerpdfmaker.fragment.ImageDocFragment
import com.example.camerascanner_docscannerpdfmaker.fragment.PdfDocFragment
import kotlinx.android.synthetic.main.activity_my_docs.*
import kotlinx.android.synthetic.main.header.*
import java.io.File

class MyDocsActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_docs)

        headerTitle.text = "MY DOCUMENT"

        back.setOnClickListener {
            finish()
        }
        info1.setOnClickListener {
            startActivity(Intent(applicationContext,IntroActivity::class.java).putExtra("fromWhere","everyWhere"))
        }

        val manager: FragmentManager = supportFragmentManager
        val transaction: FragmentTransaction = manager.beginTransaction()
        transaction.replace(R.id.container, AllDocFragment())
        transaction.commit()

        all.setOnClickListener{
            all.setImageResource(R.drawable.ic_all_highlight_11)
            pdf.setImageResource(R.drawable.ic_pdf_11)
            image.setImageResource(R.drawable.ic_image)
            val manager: FragmentManager = supportFragmentManager
            val transaction: FragmentTransaction = manager.beginTransaction()
            transaction.replace(R.id.container, AllDocFragment())
            transaction.commit()
        }
        pdf.setOnClickListener{
            all.setImageResource(R.drawable.ic_all_11)
            pdf.setImageResource(R.drawable.ic_pdf_highlight_11)
            image.setImageResource(R.drawable.ic_image)
            val manager: FragmentManager = supportFragmentManager
            val transaction: FragmentTransaction = manager.beginTransaction()
            transaction.replace(R.id.container, PdfDocFragment())
            transaction.commit()
        }
        image.setOnClickListener{
            all.setImageResource(R.drawable.ic_all_11)
            pdf.setImageResource(R.drawable.ic_pdf_11)
            image.setImageResource(R.drawable.ic_image_highlight_11)
            val manager: FragmentManager = supportFragmentManager
            val transaction: FragmentTransaction = manager.beginTransaction()
            transaction.replace(R.id.container, ImageDocFragment())
            transaction.commit()
        }

    }

}