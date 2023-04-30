package com.example.camerascanner_docscannerpdfmaker.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.drawable.BitmapDrawable
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.camerascanner_docscannerpdfmaker.R
import com.example.camerascanner_docscannerpdfmaker.Utility.Utils
import com.example.camerascanner_docscannerpdfmaker.adapter.FilterAdapter
import jp.co.cyberagent.android.gpuimage.GPUImage
import jp.co.cyberagent.android.gpuimage.filter.*
import kotlinx.android.synthetic.main.activity_edit_image.*
import kotlinx.android.synthetic.main.header.*

class EditImageActivity : AppCompatActivity() {

    var filter: MutableList<GPUImageFilter> = ArrayList<GPUImageFilter>()
    var albitmap: MutableList<Bitmap> = ArrayList<Bitmap>()
    lateinit var filterAdapter: FilterAdapter
    lateinit var context: Context
    var rotate : Float = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_image)
        context = this
        info1.setImageResource(R.drawable.ic_baseline_done_24)

        filter.add(GPUImageSketchFilter())
        filter.add(GPUImageOverlayBlendFilter())
        filter.add(GPUImageHueBlendFilter())
        filter.add(GPUImageHazeFilter())
        filter.add(GPUImageDilationFilter())
        filter.add(GPUImageColorInvertFilter())

        Afilter().execute()

        gpuimageview.setImageBitmap(Utils.CroppedBitmap)
//        gpuimageview.setImage(Utils.CroppedBitmap)
//        gpuimageview.filter = GPUImageSketchFilter()

        info1.setOnClickListener {
            Utils.CroppedBitmap = getBitmapFromView(this,gpuimageview)

            if (intent.getStringExtra("fromWhere").equals("SingleScanDoc")) {
                val intent = Intent()
                setResult(2, intent)
                finish()
            }
            if (intent.getStringExtra("fromWhere").equals("MultiScanDoc")) {
                startActivity(Intent(applicationContext, MultiDocumentActivity::class.java))
                finish()
            }
        }

        rotateLeft.setOnClickListener {
            rotate += 90
            gpuimageview.setImageBitmap(RotateBitmap((gpuimageview.getDrawable() as BitmapDrawable).bitmap, 90f))
        }

        rotateRight.setOnClickListener {
            rotate -=  90
            gpuimageview.setImageBitmap(RotateBitmap((gpuimageview.getDrawable() as BitmapDrawable).bitmap, -90f))
        }
    }

    fun getBitmapFromView(activity: Activity, view: View): Bitmap {
        val createBitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(createBitmap)
        activity.runOnUiThread { view.draw(canvas) }
        try {
            Thread.sleep(100)
        } catch (e: InterruptedException) {
            e.printStackTrace()
            Log.d("ERRORRRR", e.message)
        }
        return createBitmap
    }


    fun RotateBitmap(bitmap: Bitmap, f: Float): Bitmap? {
        val matrix = Matrix()
        matrix.postRotate(f)
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

    inner class Afilter : AsyncTask<Void, Void, Void>() {

        var gimage: GPUImage? = null

        override fun onPreExecute() {
            super.onPreExecute()
            gimage = GPUImage(context)
            gimage!!.setImage(Utils.CroppedBitmap)
        }

        override fun doInBackground(vararg params: Void?): Void? {
            for (i in filter.indices) {
                gimage!!.setFilter(filter[i])
                gimage!!.requestRender()
                albitmap.add(Bitmap.createBitmap(gimage!!.bitmapWithFilterApplied))
            }
            return null
        }

        override fun onPostExecute(result: Void?) {
            super.onPostExecute(result)
            if (albitmap.size != 0) {
                filterAdapter = FilterAdapter(albitmap, object : FilterAdapter.OnCLickItem {
                    override fun getClick(position: Int) {
                        gpuimageview.setImageBitmap(albitmap.get(position))
                    }
                })

                filterRecyclerView.apply {
                    layoutManager = LinearLayoutManager(this@EditImageActivity, LinearLayoutManager.HORIZONTAL, false)
                    adapter = filterAdapter
                }
            }

        }

    }

}