package com.example.camerascanner_docscannerpdfmaker.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.camerascanner_docscannerpdfmaker.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.math.sign

class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkPermission()
        info.setOnClickListener {
            startActivity(Intent(applicationContext,IntroActivity::class.java).putExtra("fromWhere","everyWhere"))
        }

        idCard.setOnClickListener {
            startActivity(Intent(applicationContext,IdCardActivity::class.java))
        }
        scanDoc.setOnClickListener {
            startActivity(Intent(applicationContext,ScanDocActivity::class.java))
        }
        passport.setOnClickListener {
            startActivity(Intent(applicationContext,PassportActivity::class.java))
        }
        barcode.setOnClickListener {
            startActivity(Intent(applicationContext,BarcodeScannerActivity::class.java))
        }
        qrScan.setOnClickListener {
            startActivity(Intent(applicationContext,QRScanActivity::class.java))
        }
        ocr.setOnClickListener {
            startActivity(Intent(applicationContext,OCRActivity::class.java))
        }
        files.setOnClickListener {
            startActivity(Intent(applicationContext,MyDocsActivity::class.java))
        }
        myDocs.setOnClickListener {
            startActivity(Intent(applicationContext,MyDocsActivity::class.java))
        }
        signature.setOnClickListener {
            startActivity(Intent(applicationContext,SignatureActivity::class.java))
        }
        wordToPdf.setOnClickListener {
            startActivity(Intent(applicationContext,WordToPdfActivity::class.java))
        }
        pdfToWord.setOnClickListener {
            startActivity(Intent(applicationContext,PdfToWordActivity::class.java))
        }

        rateUs.setOnClickListener {
            try {
                startActivity(
                    Intent(
                        "android.intent.action.VIEW",
                        Uri.parse("market://details?id=$packageName")
                    )
                )
            } catch (e: Exception) {
                startActivity(
                    Intent(
                        "android.intent.action.VIEW",
                        Uri.parse("http://play.google.com/store/apps/details?id=$packageName")
                    )
                )
            }
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

    fun checkPermission() {
        if (ContextCompat.checkSelfPermission(
                this@MainActivity,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this@MainActivity,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
            ) {
                ActivityCompat.requestPermissions(
                    this@MainActivity,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA), 1
                )
            } else {
                ActivityCompat.requestPermissions(
                    this@MainActivity,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA), 1
                )
            }
        }
    }


}