package com.example.camerascanner_docscannerpdfmaker.activity

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.camerascanner_docscannerpdfmaker.R
import com.example.camerascanner_docscannerpdfmaker.constant.KeyConstants
import com.itextpdf.text.Document
import com.itextpdf.text.Image
import com.itextpdf.text.PageSize
import com.itextpdf.text.pdf.PdfWriter
import kotlinx.android.synthetic.main.activity_main.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

open class BaseActivity : AppCompatActivity() {

    lateinit var sharedPreferences: SharedPreferences
    lateinit var editor: SharedPreferences.Editor
    lateinit var document: Document
    lateinit var fileNameDialog: Dialog
    lateinit var fileNameEditText: EditText
    lateinit var cancel: Button
    lateinit var save: Button
    var fileName: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)

        sharedPreferences =
            getSharedPreferences(KeyConstants.sharedPreferencesKey, Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()
        document = Document(PageSize.A4)

        fileNameDialog = Dialog(this@BaseActivity)
        fileNameDialog.setContentView(R.layout.file_name_dialog)
        fileNameEditText = fileNameDialog.findViewById(R.id.fileNameEditText)
        cancel = fileNameDialog.findViewById(R.id.cancel)
        save = fileNameDialog.findViewById(R.id.save)

        cancel.setOnClickListener { fileNameDialog.dismiss() }

        var filePath = File("${filesDir}${File.separator}Images")
        if (!filePath.exists())
            filePath.mkdir()

        var filePath2 = File("${filesDir}${File.separator}Tmp")
        if (!filePath2.exists())
            filePath2.mkdir()

        var filename3 = File("${filesDir}${File.separator}PDF")
        if (!filename3.exists())
            filename3.mkdir()
    }

    fun getBitmapFromView(view: View): Bitmap? {
        //Define a bitmap with the same size as the view
        val returnedBitmap = Bitmap.createBitmap(
            view.width,
            view.height,
            Bitmap.Config.ARGB_8888
        )
        //Bind a canvas to it
        val canvas = Canvas(returnedBitmap)
        //Get the view's background
        val bgDrawable: Drawable? = view.background
        if (bgDrawable != null) //has background drawable, then draw it on the canvas
            bgDrawable.draw(canvas) else  //does not have background drawable, then draw white background on the canvas
            canvas.drawColor(Color.WHITE)
        // draw the view on the canvas
        view.draw(canvas)
        //return the bitmap
        return returnedBitmap
    }

    fun saveBitmap(bmp: Bitmap?, filename: String) {
        try {
            FileOutputStream(filename).use { out ->
                bmp!!.compress(
                    Bitmap.CompressFormat.PNG,
                    100,
                    out
                )
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun saveAsPdfForIdCard(instance: Image, filename: String) {
        var filePath = File("${filesDir}${File.separator}PDF")

        PdfWriter.getInstance(
            document,
            FileOutputStream("${filePath.absolutePath}${File.separator}${filename}.pdf")
        )
        document.open()
        val width = document.pageSize.width - (document.leftMargin() - document.rightMargin())
        val height3 = document.pageSize.height - (document.topMargin() - document.bottomMargin())
        val pageSize = document.pageSize
        instance.scaleToFit(width, height3)
        instance.setAbsolutePosition(
            (pageSize.width - instance.getScaledWidth()) / 2.0f,
            (pageSize.height - instance.getScaledHeight()) / 2.0f
        )
        instance.setAlignment(1)
        document.add(instance)
        document.close()
    }

    fun saveAsPdfForMultiScanDoc(list: ArrayList<String>) {
        var filename = File("${filesDir}${File.separator}PDF")
        if (!filename.exists())
            filename.mkdir()
        PdfWriter.getInstance(
            document,
            FileOutputStream("${filename.absolutePath}${File.separator}${fileNameEditText.text.trim()}.pdf")
        )
        document.open()

        for (path: String in list) {
            var instance = Image.getInstance(path)
            val width = document.pageSize.width - (document.leftMargin() - document.rightMargin())
            val height3 =
                document.pageSize.height - (document.topMargin() - document.bottomMargin())
            val pageSize = document.pageSize
            instance.scaleToFit(width, height3)
            instance.setAbsolutePosition(
                (pageSize.width - instance.getScaledWidth()) / 2.0f,
                (pageSize.height - instance.getScaledHeight()) / 2.0f
            )
            instance.setAlignment(1)
            document.add(instance)
            document.newPage()
        }


        document.close()
    }

    open fun getImageUri(
        inContext: Context,
        inImage: Bitmap?
    ): Uri? {
        val bytes = ByteArrayOutputStream()
        inImage!!.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path: String = MediaStore.Images.Media.insertImage(
            inContext.contentResolver,
            inImage,
            "Title",
            null
        )
        return Uri.parse(path)
    }

}