package com.example.camerascanner_docscannerpdfmaker.activity

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.aspose.words.Document
import com.example.camerascanner_docscannerpdfmaker.R
import com.example.camerascanner_docscannerpdfmaker.Utility.RealPathUtil
import com.example.camerascanner_docscannerpdfmaker.converter.Converter
import kotlinx.android.synthetic.main.activity_word_to_pdf.*
import kotlinx.android.synthetic.main.header.*
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException

class WordToPdfActivity : BaseActivity() {

    lateinit var intentFinal: Intent
    lateinit var outputPDF: File
    lateinit var fileNameFinal: String
    lateinit var progressDialog : ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_word_to_pdf)

        headerTitle.text = "WORD TO PDF"

        back.setOnClickListener {
            finish()
        }
        info1.setOnClickListener {
            startActivity(Intent(applicationContext,IntroActivity::class.java).putExtra("fromWhere","everyWhere"))
        }

        openFile.setOnClickListener {
            openAndConvertFile()
        }

        progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Saving...")
        progressDialog.setCancelable(false)
    }

    private fun openAndConvertFile() {
        val intent =
                Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        // mime types for MS Word documents
        val mimetypes = arrayOf(
                "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
                "application/msword"
        )
        intent.type = "*/*"
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimetypes)
        startActivityForResult(intent, 2)
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    override fun onActivityResult(
            requestCode: Int,
            resultCode: Int,
            intent: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, intent)
        if (resultCode == Activity.RESULT_OK) {
            intentFinal = intent!!
            fileNameDialog.show()

            save.setOnClickListener {
                if (fileNameEditText.text.trim().length != 0){
                    fileName = fileNameEditText.text.toString()
                    fileNameDialog.dismiss()
//                    docXToPdf()
                    MyAsync().execute()
                }
                else
                    Toast.makeText(applicationContext,"Please enter name!!!",Toast.LENGTH_LONG).show()
            }
        }
    }

    inner class MyAsync : AsyncTask<Void, Void, Void>() {

        override fun onPreExecute() {
            super.onPreExecute()
            progressDialog.show()
        }

        override fun doInBackground(vararg params: Void?): Void? {
            if (intentFinal != null) {
                var document1: Uri = intentFinal.data!!
                var filePath = File("${filesDir}${File.separator}PDF")
                outputPDF = File("${filePath.absolutePath}${File.separator}${fileName}.pdf")
                // open the selected document1 into an Input stream
                try {
                    contentResolver.openInputStream(document1).use { inputStream ->
                        val doc = Document(inputStream)
                        // save DOCX as PDF
                        doc.save(outputPDF.absolutePath)
                        // show PDF file location in toast as well as treeview (optional)
                        Toast.makeText(
                                this@WordToPdfActivity,
                                "File saved in: $outputPDF",
                                Toast.LENGTH_LONG
                        ).show()
                    }
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                } catch (e: IOException) {
                    e.printStackTrace()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            return null
        }

        override fun onPostExecute(result: Void?) {
            super.onPostExecute(result)
            progressDialog.dismiss()
            Toast.makeText(
                    this@WordToPdfActivity,
                    "Saved",
                    Toast.LENGTH_LONG
            ).show()

        }

    }

    fun docXToPdf() {
        val c = Converter()
        var filePath = File("${filesDir}${File.separator}PDF")
        val outFile = File("${filePath.absolutePath}${File.separator}converted.pdf")
        c.convert(File(RealPathUtil.getRealPath(this,intentFinal.data)).absolutePath, outFile.absolutePath)
//        try {
//            val inFile = File(RealPathUtil.getRealPath(this,intentFinal.data))
//            var filePath = File("${filesDir}${File.separator}PDF")
//            val outFile = File("${filePath.absolutePath}${File.separator}converted.pdf")
//            val docuConverter: DocuConverter = DocuConverter.getConverter(FileInputStream(inFile),
//                    DocumentType.DOCX, ConvertToType.PDF)
//            docuConverter.setVerbose(true) //optional
//            docuConverter.convertIn(FileOutputStream(outFile))
//        } catch (e: java.lang.Exception) {
//            Log.d("Exe123",e.toString())
//        }
    }

}