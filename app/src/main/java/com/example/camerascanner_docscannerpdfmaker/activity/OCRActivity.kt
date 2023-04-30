package com.example.camerascanner_docscannerpdfmaker.activity

import android.app.Activity
import android.app.Dialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.camerascanner_docscannerpdfmaker.R
import com.example.camerascanner_docscannerpdfmaker.Utility.Utils
import com.example.camerascanner_docscannerpdfmaker.textrecognizerlibrary.TextScanner
import com.example.camerascanner_docscannerpdfmaker.textrecognizerlibrary.callback.TextExtractCallback
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.activity_o_c_r.*
import kotlinx.android.synthetic.main.header.*
import java.io.IOException

class OCRActivity : BaseActivity() {

    var width = 0
    var height = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_o_c_r)

        headerTitle.text = "OCR READER"

        back.setOnClickListener {
            finish()
        }

        info1.setOnClickListener {
            startActivity(Intent(applicationContext,IntroActivity::class.java).putExtra("fromWhere","everyWhere"))
        }

        width = resources.displayMetrics.widthPixels
        height = resources.displayMetrics.heightPixels

        startScan.setOnClickListener {
            CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(this@OCRActivity)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == Activity.RESULT_OK) {
                try {
                    val bitmap: Bitmap = bitmapResize(
                        MediaStore.Images.Media.getBitmap(
                            contentResolver,
                            result.uri
                        )
                    )
                    getTextFromBitmap(bitmap)
                } catch (e: IOException) {
                    e.printStackTrace()
                    Log.e("Exe", e.toString())
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
            }
        }

        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                Utils.bitmap = data?.extras?.get("data") as Bitmap
                startActivityForResult(
                    Intent(
                        applicationContext,
                        ImageCropperActivity::class.java
                    ).putExtra("fromWhere", "OCR"), 11
                )
            }
        }
    }

    fun bitmapResize(bitmap: Bitmap): Bitmap {
        var i: Int = this.width
        var i2: Int = this.height
        val width = bitmap.width
        val height = bitmap.height
        if (width >= height) {
            val i3 = height * i / width
            if (i3 > i2) {
                i = i * i2 / i3
            } else {
                i2 = i3
            }
        } else {
            val i4 = width * i2 / height
            if (i4 > i) {
                i2 = i2 * i / i4
            } else {
                i = i4
            }
        }
        return Bitmap.createScaledBitmap(bitmap, i, i2, true)
    }

    var written_text = ""
    private fun getTextFromBitmap(bitmap: Bitmap) {
        TextScanner.getInstance(this).init().load(bitmap)
            .getCallback(object : TextExtractCallback {
                override fun onGetExtractText(list: List<String?>) {
                    for (i in list.indices) {
                        if (i >= list.size - 1) {
                            val cropImageAct: OCRActivity = this@OCRActivity
                            val sb = StringBuilder()
                            sb.append(this@OCRActivity.written_text)
                            sb.append(list[i])
                            written_text = sb.toString()
                        } else {
                            val cropImageAct2: OCRActivity = this@OCRActivity
                            val sb2 = StringBuilder()
                            sb2.append(this@OCRActivity.written_text)
                            sb2.append(list[i])
                            sb2.append("\n")
                            written_text = sb2.toString()
                        }
                    }
                    startActivity(Intent(applicationContext,TextActivity::class.java).putExtra("scannedText",written_text))
                }
            })

        if (written_text == "") {
            TextScanner.getInstance(this).init().load(bitmap)
                .getCallback(TextExtractCallback { list ->
                    for (i in list.indices) {
                        if (i >= list.size - 1) {
                            val cropImageAct: OCRActivity = this@OCRActivity
                            val sb = java.lang.StringBuilder()
                            sb.append(this@OCRActivity.written_text)
                            sb.append(list[i] as String)
                            cropImageAct.written_text = sb.toString()
                        } else {
                            val cropImageAct2: OCRActivity = this@OCRActivity
                            val sb2 = java.lang.StringBuilder()
                            sb2.append(this@OCRActivity.written_text)
                            sb2.append(list[i] as String)
                            sb2.append("\n")
                            cropImageAct2.written_text = sb2.toString()
                        }
                    }
                })
            return
        } else if (written_text.length == 0) {
            Toast.makeText(
                this@OCRActivity,
                "Error",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            Toast.makeText(
                this,
                "Error",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
    
    fun showDialog(data : String){
        val copyScanCodeDialog = Dialog(this@OCRActivity)
        copyScanCodeDialog.setContentView(R.layout.copy_scan_code_dialog)
        copyScanCodeDialog.show()

        val code = copyScanCodeDialog.findViewById<TextView>(R.id.code)
        val copyCode = copyScanCodeDialog.findViewById<Button>(R.id.copyCode)
        copyCode.setMovementMethod(ScrollingMovementMethod())

        code.text = data
        copyCode.setOnClickListener {
            val clipboard: ClipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip: ClipData = ClipData.newPlainText("label", code.text)
            clipboard.setPrimaryClip(clip)
            Toast.makeText(applicationContext,"Code copied!!!",Toast.LENGTH_LONG).show()
        }
    }

}