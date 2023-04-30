package com.example.camerascanner_docscannerpdfmaker.Utility

import android.graphics.Bitmap

class Utils {
    companion object{
        var bitmap : Bitmap ?= null
        lateinit var CroppedBitmap : Bitmap
        var FilteredBitmap : Bitmap ?= null
        var CameraOrGallery : String ?= null
    }
}