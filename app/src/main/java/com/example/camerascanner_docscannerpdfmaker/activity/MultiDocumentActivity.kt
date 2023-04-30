package com.example.camerascanner_docscannerpdfmaker.activity

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.camerascanner_docscannerpdfmaker.R
import com.example.camerascanner_docscannerpdfmaker.Utility.Utils
import com.example.camerascanner_docscannerpdfmaker.Utility.Utils.Companion.CameraOrGallery
import com.example.camerascanner_docscannerpdfmaker.adapter.DocumentAdapter
import kotlinx.android.synthetic.main.activity_multi_document.*
import kotlinx.android.synthetic.main.header.*
import java.io.File


class MultiDocumentActivity : BaseActivity() {

    lateinit var context: Context
    var fromWhere: String = ""
    var bitmapList: MutableList<String> = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_multi_document)
        context = this
        headerTitle.text = "Document"

        back.setOnClickListener {
            finish()
        }
        info1.setImageResource(R.drawable.ic_baseline_done_24)

        saveBitmap()

        add.setOnClickListener {
            if (CameraOrGallery.equals("camera")) {
                Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { intent ->
                    intent.resolveActivity(packageManager)?.also {
                        startActivityForResult(intent, 1)
                    }
                }
            } else {
                Intent(Intent.ACTION_GET_CONTENT).also { intent ->
                    intent.type = "image/*"
                    intent.resolveActivity(packageManager)?.also {
                        startActivityForResult(intent, 2)
                    }
                }
            }
        }

        info1.setOnClickListener {
            fileNameDialog.show()
            save.setOnClickListener {
                if (fileNameEditText.text.trim().length != 0){
                    saveAsPdfForMultiScanDoc(bitmapList as ArrayList<String>)
                    fileNameDialog.dismiss()
                    Toast.makeText(applicationContext,"Saved",Toast.LENGTH_LONG).show()
                    finish()
                }
                else
                    Toast.makeText(applicationContext,"Please enter name!!!",Toast.LENGTH_LONG).show()
            }
            cancel.setOnClickListener {
                fileNameDialog.dismiss()
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
                val uri = data?.getData()
                val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
                Utils.bitmap = bitmap
                startActivityForResult(
                    Intent(
                        applicationContext,
                        ImageCropperActivity::class.java
                    ).putExtra("fromWhere", "SingleScanDoc"), 11
                )
            }
        }
        if (resultCode == 2 && requestCode == 11) {
            saveBitmap()
        }
    }

    fun saveBitmap() {
        var filePath = File("${filesDir}${File.separator}Tmp")
        if (!filePath.exists())
            filePath.mkdir()
        val finalFileName =
            "${filePath.absolutePath}${File.separator}${System.currentTimeMillis()}.png"
        saveBitmap(Utils.CroppedBitmap, finalFileName)
        bitmapList.add(finalFileName)
        documentRecyclerView.apply {
            adapter = DocumentAdapter(bitmapList)
            layoutManager = GridLayoutManager(context, 2)
        }
    }

}