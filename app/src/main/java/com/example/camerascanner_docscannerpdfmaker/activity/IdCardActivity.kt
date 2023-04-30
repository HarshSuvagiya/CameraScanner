package com.example.camerascanner_docscannerpdfmaker.activity

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.camerascanner_docscannerpdfmaker.R
import com.example.camerascanner_docscannerpdfmaker.Utility.Utils
import com.itextpdf.text.Image
import kotlinx.android.synthetic.main.activity_id_card.*
import kotlinx.android.synthetic.main.header.*
import java.io.File

class IdCardActivity : BaseActivity() {

    private val REQUEST_IMAGE_CAPTURE = 1
    private val REQUEST_PICK_IMAGE = 2
    var count: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_id_card)

        headerTitle.text = "ID CARD"

        back.setOnClickListener {
            finish()
        }
        info1.setOnClickListener {
            startActivity(Intent(applicationContext,IntroActivity::class.java).putExtra("fromWhere","everyWhere"))
        }

        frontPageCamera.setOnClickListener {
            Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { intent ->
                intent.resolveActivity(packageManager)?.also {
                    startActivityForResult(intent, 1)
                }
            }
        }

        frontPageGallery.setOnClickListener {
            Intent(Intent.ACTION_GET_CONTENT).also { intent ->
                intent.type = "image/*"
                intent.resolveActivity(packageManager)?.also {
                    startActivityForResult(intent, 2)
                }
            }
        }

        backPageCamera.setOnClickListener {
            Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { intent ->
                intent.resolveActivity(packageManager)?.also {
                    startActivityForResult(intent, 3)
                }
            }
        }

        backPageGallery.setOnClickListener {
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
                    ).putExtra("fromWhere", "IdCard"), 11
                )
            } else if (requestCode == 2) {
                val uri = data?.getData()
                val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
                Utils.bitmap = bitmap
                startActivityForResult(
                    Intent(
                        applicationContext,
                        ImageCropperActivity::class.java
                    ).putExtra("fromWhere", "IdCard"), 11
                )
            } else if (requestCode == 3) {
                Utils.bitmap = data?.extras?.get("data") as Bitmap
                startActivityForResult(
                    Intent(
                        applicationContext,
                        ImageCropperActivity::class.java
                    ).putExtra("fromWhere", "IdCard"), 22
                )
            } else if (requestCode == 4) {
                val uri = data?.getData()
                val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
                Utils.bitmap = bitmap
                startActivityForResult(
                    Intent(
                        applicationContext,
                        ImageCropperActivity::class.java
                    ).putExtra("fromWhere", "IdCard"), 22
                )
            }
        }
        if (requestCode == 11 && resultCode == 2) {
            count++
            hideFrontPage()
            resultCardView.visibility = View.VISIBLE
            frontPageImage.setImageBitmap(Utils.CroppedBitmap)
            resultCardView.invalidate()

        }
        if (requestCode == 22 && resultCode == 2) {
            count++
            hideBackPage()
            resultCardView.visibility = View.VISIBLE
            backPageImage.setImageBitmap(Utils.CroppedBitmap)
        }
        if (count == 2) {
            saveOptions.visibility = View.VISIBLE
            frontPageImage.visibility = View.VISIBLE
            backPageImage.visibility = View.VISIBLE
            saveAsImage.setOnClickListener{
                var finalBitmap = getBitmapFromView(resultCardView)
                var filePath = File("${filesDir}${File.separator}Images")
                if (!filePath.exists())
                    filePath.mkdir()
                fileNameDialog.show()

                save.setOnClickListener {
                    if (fileNameEditText.text.trim().length != 0){
                        fileName = fileNameEditText.text.toString()
                        saveBitmap(finalBitmap,"${filePath.absolutePath}${File.separator}${fileName}.png")
                        fileNameDialog.dismiss()
                        finish()
                        Toast.makeText(applicationContext,"Saved",Toast.LENGTH_LONG).show()
                    }
                    else
                        Toast.makeText(applicationContext,"Please enter name!!!",Toast.LENGTH_LONG).show()
                }
            }
            saveAsPdf.setOnClickListener {
                var finalBitmap = getBitmapFromView(resultCardView)
                var filePath = File("${filesDir}${File.separator}Tmp")
                if (!filePath.exists())
                    filePath.mkdir()
                fileNameDialog.show()
                val finalFileName = "${filePath.absolutePath}${File.separator}${System.currentTimeMillis()}.png"
                saveBitmap(finalBitmap,finalFileName)
                fileNameDialog.show()
                save.setOnClickListener {
                    saveAsPdfForIdCard(Image.getInstance(finalFileName),fileNameEditText.text.trim().toString())
                    fileNameDialog.dismiss()
                    finish()
                    Toast.makeText(applicationContext,"Saved",Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    fun hideFrontPage() {
        frontPage.visibility = View.GONE
        frontPageCamera.visibility = View.GONE
        frontPageGallery.visibility = View.GONE
    }

    fun hideBackPage() {
        backPage.visibility = View.GONE
        backPageCamera.visibility = View.GONE
        backPageGallery.visibility = View.GONE
    }
}