package com.example.camerascanner_docscannerpdfmaker.activity

import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.camerascanner_docscannerpdfmaker.R
import com.itextpdf.text.Image
import com.mukesh.DrawingView
import kotlinx.android.synthetic.main.activity_signature.*
import kotlinx.android.synthetic.main.header.*
import java.io.File


class SignatureActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signature)

        headerTitle.text = "SIGNATURE"

        back.setOnClickListener {
            finish()
        }

        val drawingView = findViewById<View>(R.id.scratch_pad) as DrawingView
        drawingView.initializePen()
        drawingView.setBackgroundColor(resources.getColor(R.color.white))
        drawingView.setPenColor(resources.getColor(R.color.black))

        clear.setOnClickListener {
            drawingView.clear()
        }

        saveAsPdf.setOnClickListener {
            var filePath = File("${filesDir}${File.separator}Tmp")
            if (!filePath.exists())
                filePath.mkdir()
            fileNameDialog.show()

            save.setOnClickListener {
                if (fileNameEditText.text.trim().length != 0){
                    fileName = fileNameEditText.text.toString()
                    drawingView.saveImage(filePath.absolutePath, fileName, Bitmap.CompressFormat.PNG, 100)
                    saveAsPdfForIdCard(Image.getInstance("${filePath.absolutePath}${File.separator}${fileName}.png"),fileName)
                    fileNameDialog.dismiss()
                    finish()
                    Toast.makeText(applicationContext,"Saved",Toast.LENGTH_LONG).show()
                }
                else
                    Toast.makeText(applicationContext,"Please enter name!!!",Toast.LENGTH_LONG).show()
            }
        }

        saveAsImage.setOnClickListener {
            var filePath = File("${filesDir}${File.separator}Images")
            if (!filePath.exists())
                filePath.mkdir()
            fileNameDialog.show()

            save.setOnClickListener {
                if (fileNameEditText.text.trim().length != 0){
                    fileName = fileNameEditText.text.toString()
                    drawingView.saveImage(filePath.absolutePath, fileName, Bitmap.CompressFormat.PNG, 100)
                    fileNameDialog.dismiss()
                    finish()
                    Toast.makeText(applicationContext,"Saved",Toast.LENGTH_LONG).show()
                }
                else
                    Toast.makeText(applicationContext,"Please enter name!!!",Toast.LENGTH_LONG).show()
            }
        }

    }
}