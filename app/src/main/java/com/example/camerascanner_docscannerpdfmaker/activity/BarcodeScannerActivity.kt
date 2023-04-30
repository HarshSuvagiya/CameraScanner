package com.example.camerascanner_docscannerpdfmaker.activity

import android.R.attr.label
import android.app.Dialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.camerascanner_docscannerpdfmaker.R
import kotlinx.android.synthetic.main.activity_barcode_scanner.*
import kotlinx.android.synthetic.main.header.*


class BarcodeScannerActivity : BaseActivity(){


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_barcode_scanner)

        headerTitle.text = "BARCODE"

        back.setOnClickListener {
            finish()
        }
        info1.setOnClickListener {
            startActivity(Intent(applicationContext,IntroActivity::class.java).putExtra("fromWhere","everyWhere"))
        }

        startScan.setOnClickListener {
            startActivityForResult(Intent(applicationContext,BarcodeActivity::class.java),1)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == 2){
            val copyScanCodeDialog = Dialog(this@BarcodeScannerActivity)
            copyScanCodeDialog.setContentView(R.layout.copy_scan_code_dialog)
            copyScanCodeDialog.show()

            val code = copyScanCodeDialog.findViewById<TextView>(R.id.code)
            val copyCode = copyScanCodeDialog.findViewById<Button>(R.id.copyCode)

            code.text = data!!.getStringExtra("code")
            copyCode.setOnClickListener {
                val clipboard: ClipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip: ClipData = ClipData.newPlainText("label", code.text)
                clipboard.setPrimaryClip(clip)
                Toast.makeText(applicationContext,"Code copied!!!",Toast.LENGTH_LONG).show()
            }

        }
    }
}