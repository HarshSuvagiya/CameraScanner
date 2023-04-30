package com.example.camerascanner_docscannerpdfmaker.activity

import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.example.camerascanner_docscannerpdfmaker.R
import com.example.camerascanner_docscannerpdfmaker.Utility.Utils
import com.example.camerascanner_docscannerpdfmaker.Utility.Utils.Companion.CameraOrGallery
import com.itextpdf.text.Image
import kotlinx.android.synthetic.main.activity_id_card.*
import kotlinx.android.synthetic.main.activity_scan_doc.*
import kotlinx.android.synthetic.main.header.*
import java.io.File

class ScanDocActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan_doc)

        headerTitle.text = "SCAN DOC"

        back.setOnClickListener {
            finish()
        }
        info1.setOnClickListener {
            startActivity(Intent(applicationContext,IntroActivity::class.java).putExtra("fromWhere","everyWhere"))
        }

        singleDocCamera.setOnClickListener {
            Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { intent ->
                intent.resolveActivity(packageManager)?.also {
                    startActivityForResult(intent, 1)
                }
            }
        }
        multiDocCamera.setOnClickListener {
            CameraOrGallery = "camera"
            Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { intent ->
                intent.resolveActivity(packageManager)?.also {
                    startActivityForResult(intent, 2)
                }
            }
        }
        singleDocGallery.setOnClickListener {
            Intent(Intent.ACTION_GET_CONTENT).also { intent ->
                intent.type = "image/*"
                intent.resolveActivity(packageManager)?.also {
                    startActivityForResult(intent, 3)
                }
            }
        }
        multiDocGallery.setOnClickListener {
            CameraOrGallery = "gallery"
            Intent(Intent.ACTION_GET_CONTENT).also { intent ->
                intent.type = "image/*"
                intent.resolveActivity(packageManager)?.also {
                    startActivityForResult(intent, 4)
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
                    ).putExtra("fromWhere", "SingleScanDoc"), 11
                )
            } else if (requestCode == 2) {
                Utils.bitmap = data?.extras?.get("data") as Bitmap
                startActivityForResult(
                    Intent(
                        applicationContext,
                        ImageCropperActivity::class.java
                    ).putExtra("fromWhere", "MultiScanDoc"), 22
                )
            } else if (requestCode == 3) {
                val uri = data?.getData()
                val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
                Utils.bitmap = bitmap
                startActivityForResult(
                    Intent(
                        applicationContext,
                        ImageCropperActivity::class.java
                    ).putExtra("fromWhere", "SingleScanDoc"), 11
                )
            } else if (requestCode == 4) {
                val uri = data?.getData()
                val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
                Utils.bitmap = bitmap
                startActivityForResult(
                    Intent(
                        applicationContext,
                        ImageCropperActivity::class.java
                    ).putExtra("fromWhere", "MultiScanDoc"), 22
                )
            }
        }
        if (requestCode == 11 && resultCode == 2) {
            saveSingleDoc()
        }
    }

    fun saveSingleDoc() {
        var scanDocDialog = Dialog(this@ScanDocActivity)
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