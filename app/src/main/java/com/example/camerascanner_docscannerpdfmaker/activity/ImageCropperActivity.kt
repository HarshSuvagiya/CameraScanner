package com.example.camerascanner_docscannerpdfmaker.activity

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.PointF
import android.graphics.RectF
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.camerascanner_docscannerpdfmaker.R
import com.example.camerascanner_docscannerpdfmaker.Utility.Utils
import com.scanlibrary.IScanner
import com.scanlibrary.ScanActivity
import kotlinx.android.synthetic.main.activity_image_cropper.*
import kotlinx.android.synthetic.main.header.*
import java.util.*


class ImageCropperActivity : AppCompatActivity(), IScanner {

    lateinit var bitmap: Bitmap;
    lateinit var bitready: Bitmap;
    var scanActivity: ScanActivity? = null
    var scanner: IScanner? = null
    var fromWhere: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_cropper)

        headerTitle.text = "Adjustment"

        back.setOnClickListener {
            finish()
        }

        info1.setImageResource(R.drawable.ic_baseline_done_24)
        fromWhere = intent.getStringExtra("fromWhere")
        scanner = this
        bitmap = Utils.bitmap!!
        scanActivity = ScanActivity()
        sourceFrame.post(getWidthHeightOfFrame())

        info1.setOnClickListener {
            val points: Map<Int?, PointF?> = polygonView.getPoints()
            if (isScanPointsValid(points)) {
                ScanAsyncTask(points).execute(*arrayOfNulls<Void>(0))
            }
        }

    }


    fun isScanPointsValid(map: Map<Int?, PointF?>): Boolean {
        return map.size == 4
    }

    inner class getWidthHeightOfFrame : Runnable {
        override fun run() {
            setBit(bitmap)
        }
    }

    fun setBit(bitmap: Bitmap) {
        sourceiv.setImageBitmap(
                scaledBitmap(
                        bitmap,
                        sourceFrame.getWidth(),
                        sourceFrame.getHeight()
                )
        )
//        sourceiv.setImageBitmap(bitmap)
        val bitmap3 = (sourceiv.getDrawable() as BitmapDrawable).bitmap
        polygonView.points = getEdgePoints(bitmap3)
        polygonView.visibility = View.VISIBLE
        val dimension = resources.getDimension(R.dimen.scanPadding).toInt() * 2
        val layoutParams =
                FrameLayout.LayoutParams(
                        bitmap3.width + dimension,
                        bitmap3.height + dimension
                )
        layoutParams.gravity = 17
        polygonView.setLayoutParams(layoutParams)
    }

    fun scaledBitmap(bitmap2: Bitmap, i: Int, i2: Int): Bitmap {
        Log.d("iiiiiiiiiiii1", i.toString())
        Log.d("iiiiiiiiiiii2", i2.toString())
        val matrix = Matrix()
        matrix.setRectToRect(
                RectF(
                        0.0f,
                        0.0f,
                        bitmap2.width.toFloat(),
                        bitmap2.height.toFloat()
                ),
                RectF(0.0f, 0.0f, i.toFloat(), i2.toFloat()),
                Matrix.ScaleToFit.CENTER
        )
        return Bitmap.createBitmap(bitmap2, 0, 0, bitmap2.width, bitmap2.height, matrix, true)
    }

    private fun getEdgePoints(bitmap2: Bitmap): Map<Int, PointF> {
        return orderedValidEdgePoints(bitmap2, getContourEdgePoints(bitmap2))
    }

    private fun getContourEdgePoints(bitmap2: Bitmap): List<PointF> {
        val points: FloatArray = scanActivity!!.getPoints(bitmap2)
        val f = points[0]
        val f2 = points[1]
        val f3 = points[2]
        val f4 = points[3]
        val f5 = points[4]
        val f6 = points[5]
        val f7 = points[6]
        val f8 = points[7]
        val arrayList: MutableList<PointF> = ArrayList<PointF>()
        arrayList.add(PointF(f, f5))
        arrayList.add(PointF(f2, f6))
        arrayList.add(PointF(f3, f7))
        arrayList.add(PointF(f4, f8))
        return arrayList
    }

    private fun getOutlinePoints(bitmap2: Bitmap): Map<Int, PointF> {
        var hashMap: HashMap<Int, PointF> = HashMap<Int, PointF>()
        hashMap.put(0, PointF(0.0f, 0.0f))!!
        hashMap.put(1, PointF(bitmap2.width.toFloat(), 0.0f))!!
        hashMap.put(2, PointF(0.0f, bitmap2.height.toFloat()))!!
        hashMap.put(3, PointF(bitmap2.width.toFloat(), bitmap2.height.toFloat()))!!
        return hashMap
    }

    private fun orderedValidEdgePoints(
            bitmap2: Bitmap,
            list: List<PointF>
    ): Map<Int, PointF> {
        val orderedPoints: Map<Int, PointF> =
                polygonView.getOrderedPoints(list)
        return if (polygonView.isValidShape(orderedPoints)) {
            orderedPoints
        } else getOutlinePoints(bitmap2)
    }

    @SuppressLint("StaticFieldLeak")
    inner class ScanAsyncTask(private val points: Map<Int?, PointF?>) :
            AsyncTask<Void?, Void?, Bitmap>() {
        var progressBar: ProgressDialog? = null
        var uri: Uri? = null

        public override fun onPreExecute() {
            super.onPreExecute()
            progressBar = ProgressDialog(this@ImageCropperActivity)
            progressBar!!.setMessage("Processing")
            progressBar!!.show()
            progressBar!!.setCancelable(false)
        }

        public override fun onPostExecute(bitmap: Bitmap) {
            super.onPostExecute(bitmap)
            try {
                progressBar!!.dismiss()
                scanner!!.onScanFinish(uri)
            } catch (e: Exception) {
            }
        }

        override fun doInBackground(vararg params: Void?): Bitmap {
            val createBitmap: Bitmap = Bitmap.createBitmap(getScannedBitmap(bitmap, points))
            bitready = Bitmap.createBitmap(createBitmap)
            return createBitmap
        }

    }

    fun getScannedBitmap(
            bitmap2: Bitmap,
            map: Map<Int?, PointF?>
    ): Bitmap {
        val width = bitmap2.width.toFloat() / sourceiv.width.toFloat()
        val height = bitmap2.height.toFloat() / sourceiv.height.toFloat()
        return scanActivity!!.getScannedBitmap(
                bitmap2,
                map[Integer.valueOf(0)]!!.x * width,
                map[Integer.valueOf(0)]!!.y * height,
                map[Integer.valueOf(1)]!!.x * width,
                map[Integer.valueOf(1)]!!.y * height,
                map[Integer.valueOf(2)]!!.x * width,
                map[Integer.valueOf(2)]!!.y * height,
                map[Integer.valueOf(3)]!!.x * width,
                map[Integer.valueOf(3)]!!.y * height
        )
    }

    override fun onScanFinish(uri: Uri?) {
        Utils.CroppedBitmap = scaledBitmap(
                Bitmap.createBitmap(bitready),
                sourceFrame.getWidth(),
                sourceFrame.getHeight()
        )


        if (fromWhere.equals("IdCard")) {
//            val intent = Intent()
//            setResult(2, intent)
//            finish()
            startActivityForResult(Intent(applicationContext, EditImageActivity::class.java).putExtra("fromWhere", "SingleScanDoc"), 111)
        }
        if (fromWhere.equals("passport")) {
//            val intent = Intent()
//            setResult(2, intent)
//            finish()
            startActivityForResult (Intent(applicationContext, EditImageActivity::class.java).putExtra("fromWhere", "SingleScanDoc"), 111)
        }
        if (fromWhere.equals("OCR")) {
//            val intent = Intent()
//            setResult(2, intent)
//            finish()
            startActivityForResult (Intent(applicationContext, EditImageActivity::class.java).putExtra("fromWhere", "SingleScanDoc"), 111)
        }
        if (fromWhere.equals("SingleScanDoc")) {
            startActivityForResult(Intent(applicationContext, EditImageActivity::class.java).putExtra("fromWhere", fromWhere), 111)
        }
        if (fromWhere.equals("MultiScanDoc")) {
            startActivity(Intent(applicationContext, EditImageActivity::class.java).putExtra("fromWhere", fromWhere))
            finish()
        }

    }

    override fun onBitmapSelect(uri: Uri?) {

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 111 && resultCode == 2) {
            Log.d("done123", "resultCardView.height.toString(11")
            val intent = Intent()
            setResult(2, intent)
            finish()
        }
    }

}