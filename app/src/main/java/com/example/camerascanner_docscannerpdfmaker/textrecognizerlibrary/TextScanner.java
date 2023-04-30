package com.example.camerascanner_docscannerpdfmaker.textrecognizerlibrary;

import android.content.Context;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore.Images.Media;
import android.util.Log;
import android.util.SparseArray;
import android.widget.Toast;

import com.example.camerascanner_docscannerpdfmaker.textrecognizerlibrary.callback.TextExtractCallback;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.Text;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.google.android.gms.vision.text.TextRecognizer.Builder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TextScanner {
    private static TextScanner sTextScanner;
    private Bitmap mBitmap;
    private TextExtractCallback mCallback;
    private Context mContext;
    private TextRecognizer mTextRecognizer;

    public static TextScanner getInstance(Context context) {
        if (sTextScanner == null) {
            sTextScanner = new TextScanner(context);
        }
        return sTextScanner;
    }

    private TextScanner(Context context) {
        this.mContext = context;
    }

    public TextScanner init() {
        this.mTextRecognizer = new Builder(this.mContext).build();
        return sTextScanner;
    }

    public TextScanner load(Bitmap bitmap) {
        this.mBitmap = bitmap;
        read(this.mBitmap);
        return sTextScanner;
    }

    public TextScanner load(Uri uri) {
        try {
            this.mBitmap = Media.getBitmap(this.mContext.getContentResolver(), uri);
            read(this.mBitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sTextScanner;
    }

    public void getCallback(TextExtractCallback textExtractCallback) {
        this.mCallback = textExtractCallback;
    }

    private void read(Bitmap bitmap) {
        if (isInitialized()) {
            sortTextBlock(this.mTextRecognizer.detect(new Frame.Builder().setBitmap(bitmap).build()));
        }
    }

    private void sortTextBlock(SparseArray<TextBlock> sparseArray) {
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < sparseArray.size(); i++) {
            arrayList.add(sparseArray.valueAt(i));
        }
        Collections.sort(arrayList, new Comparator<TextBlock>() {
            public int compare(TextBlock textBlock, TextBlock textBlock2) {
                return textBlock.getBoundingBox().top - textBlock2.getBoundingBox().top;
            }
        });
        parseText(arrayList);
    }

    private void parseText(List<TextBlock> list) {
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < list.size(); i++) {
            for (Text value : ((TextBlock) list.get(i)).getComponents()) {
                arrayList.add(value.getValue());
            }
        }
        if (this.mCallback != null) {
            this.mCallback.onGetExtractText(arrayList);
        }
    }

    private boolean isInitialized() {
        if (!this.mTextRecognizer.isOperational()) {
            Log.d("ScannerTest", "Detector dependencies are not yet available.");
            if (this.mContext.registerReceiver(null, new IntentFilter("android.intent.action.DEVICE_STORAGE_LOW")) != null) {
                Toast.makeText(this.mContext, "Low Storage", Toast.LENGTH_LONG).show();
            }
        }
        return this.mTextRecognizer.isOperational();
    }
}
