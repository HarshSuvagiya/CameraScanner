package com.example.camerascanner_docscannerpdfmaker.activity

import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.example.camerascanner_docscannerpdfmaker.R
import com.example.camerascanner_docscannerpdfmaker.Utility.Utils
import com.itextpdf.text.Image
import kotlinx.android.synthetic.main.activity_passport.*
import kotlinx.android.synthetic.main.header.*
import java.io.File

class PassportActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_passport)

        headerTitle.text = "PASSPORT"

        back.setOnClickListener {
            finish()
        }
        info1.setOnClickListener {
            startActivity(Intent(applicationContext,IntroActivity::class.java).putExtra("fromWhere","everyWhere"))
        }

        startScan.setOnClickListener {
            Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { intent ->
                intent.resolveActivity(packageManager)?.also {
                    startActivityForResult(intent, 1)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                Utils.bitmap = data?.extras?.get("data") as Bitmap
                startActivityForResult(
                    Intent(
                        applicationContext,
                        ImageCropperActivity::class.java
                    ).putExtra("fromWhere", "passport"), 11
                )
            }
        }
        if (requestCode == 11 && resultCode == 2) {
            saveSingleDoc()
        }
    }

    fun saveSingleDoc() {
        var scanDocDialog = Dialog(this@PassportActivity)
        scanDocDialog.setContentView(R.layout.scan_doc_name_dialog)
        scanDocDialog.show()

        var fileNameEditText: EditText = scanDocDialog.findViewById(R.id.fileNameEditText)
        var cancel: ImageView = scanDocDialog.findViewById(R.id.cancel)
        var saveAsPdf: ImageView = scanDocDialog.findViewById(R.id.saveAsPdf)
        var saveAsImage: ImageView = scanDocDialog.findViewById(R.id.saveAsImage)

        var filePath = File("${filesDir}${File.separator}Images")
        saveAsImage.setOnClickListener {
            if (fileNameEditText.text.trim().length != 0) {
                fileName = fileNameEditText.text.toString()
                saveBitmap(
                    Utils.CroppedBitmap,
                    "${filePath.absolutePath}${File.separator}${fileName}.png"
                )
                scanDocDialog.dismiss()
                finish()
                Toast.makeText(applicationContext, "Saved", Toast.LENGTH_LONG).show()
            } else
                Toast.makeText(applicationContext, "Please enter name!!!", Toast.LENGTH_LONG).show()
        }

        var filePathPDF = File("${filesDir}${File.separator}Tmp")
        val finalFileName = "${filePathPDF.absolutePath}${File.separator}${System.currentTimeMillis()}.png"
        saveBitmap(Utils.CroppedBitmap, finalFileName)

        saveAsPdf.setOnClickListener {

            if (fileNameEditText.text.trim().length != 0) {
                saveAsPdfForIdCard(Image.getInstance(finalFileName),fileNameEditText.text.trim().toString())
                scanDocDialog.dismiss()
                finish()
                Toast.makeText(applicationContext, "Saved", Toast.LENGTH_LONG).show()
            } else
                Toast.makeText(applicationContext, "Please enter name!!!", Toast.LENGTH_LONG).show()
        }

        cancel.setOnClickListener {
            scanDocDialog.dismiss()
        }
    }

}