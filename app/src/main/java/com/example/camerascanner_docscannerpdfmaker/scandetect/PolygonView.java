package com.example.camerascanner_docscannerpdfmaker.scandetect;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.example.camerascanner_docscannerpdfmaker.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PolygonView extends FrameLayout {
    protected Context context;
    private ImageView midPointer12;
    private ImageView midPointer13;
    private ImageView midPointer24;
    private ImageView midPointer34;
    public Paint paint;
    private ImageView pointer1;
    private ImageView pointer2;
    private ImageView pointer3;
    private ImageView pointer4;
    public PolygonView polygonView;

    private class MidPointTouchListenerImpl implements OnTouchListener {
        PointF DownPT = new PointF();
        PointF StartPT = new PointF();
        private ImageView mainPointer1;
        private ImageView mainPointer2;

        public MidPointTouchListenerImpl(ImageView imageView, ImageView imageView2) {
            mainPointer1 = imageView;
            mainPointer2 = imageView2;
        }

        public boolean onTouch(View view, MotionEvent motionEvent) {
            int i;
            switch (motionEvent.getAction()) {
                case 0:
                    DownPT.x = motionEvent.getX();
                    DownPT.y = motionEvent.getY();
                    StartPT = new PointF(view.getX(), view.getY());
                    break;
                case 1:
                    if (isValidShape(getPoints())) {
                        i = getResources().getColor(R.color.blue);
                    } else {
                        i = getResources().getColor(R.color.orange);
                    }
                    paint.setColor(i);
                    break;
                case 2:
                    PointF pointF = new PointF(motionEvent.getX() - DownPT.x, motionEvent.getY() - DownPT.y);
                    if (Math.abs(mainPointer1.getX() - mainPointer2.getX()) <= Math.abs(mainPointer1.getY() - mainPointer2.getY())) {
                        if (mainPointer2.getX() + pointF.x + ((float) view.getWidth()) < ((float) polygonView.getWidth()) && mainPointer2.getX() + pointF.x > 0.0f) {
                            view.setX((float) ((int) (StartPT.x + pointF.x)));
                            StartPT = new PointF(view.getX(), view.getY());
                            mainPointer2.setX((float) ((int) (mainPointer2.getX() + pointF.x)));
                        }
                        if (mainPointer1.getX() + pointF.x + ((float) view.getWidth()) < ((float) polygonView.getWidth()) && mainPointer1.getX() + pointF.x > 0.0f) {
                            view.setX((float) ((int) (StartPT.x + pointF.x)));
                            StartPT = new PointF(view.getX(), view.getY());
                            mainPointer1.setX((float) ((int) (mainPointer1.getX() + pointF.x)));
                            break;
                        }
                    }
                    if (mainPointer2.getY() + pointF.y + ((float) view.getHeight()) < ((float) polygonView.getHeight()) && mainPointer2.getY() + pointF.y > 0.0f) {
                        view.setX((float) ((int) (StartPT.y + pointF.y)));
                        StartPT = new PointF(view.getX(), view.getY());
                        mainPointer2.setY((float) ((int) (mainPointer2.getY() + pointF.y)));
                    }
                    if (mainPointer1.getY() + pointF.y + ((float) view.getHeight()) < ((float) polygonView.getHeight()) && mainPointer1.getY() + pointF.y > 0.0f) {
                        view.setX((float) ((int) (StartPT.y + pointF.y)));
                        StartPT = new PointF(view.getX(), view.getY());
                        mainPointer1.setY((float) ((int) (mainPointer1.getY() + pointF.y)));
                        break;
                    }
            }
            polygonView.invalidate();
            return true;
        }
    }

    private class TouchListenerImpl implements OnTouchListener {
        PointF DownPT;
        PointF StartPT;

        private TouchListenerImpl() {
            DownPT = new PointF();
            StartPT = new PointF();
        }

        public boolean onTouch(View view, MotionEvent motionEvent) {
            int i;
            switch (motionEvent.getAction()) {
                case 0:
                    DownPT.x = motionEvent.getX();
                    DownPT.y = motionEvent.getY();
                    StartPT = new PointF(view.getX(), view.getY());
                    break;
                case 1:
                    if (isValidShape(getPoints())) {
                        i = getResources().getColor(R.color.blue);
                    } else {
                        i = getResources().getColor(R.color.orange);
                    }
                    paint.setColor(i);
                    break;
                case 2:
                    PointF pointF = new PointF(motionEvent.getX() - DownPT.x, motionEvent.getY() - DownPT.y);
                    if (StartPT.x + pointF.x + ((float) view.getWidth()) < ((float) polygonView.getWidth()) && StartPT.y + pointF.y + ((float) view.getHeight()) < ((float) polygonView.getHeight()) && StartPT.x + pointF.x > 0.0f && StartPT.y + pointF.y > 0.0f) {
                        view.setX((float) ((int) (StartPT.x + pointF.x)));
                        view.setY((float) ((int) (StartPT.y + pointF.y)));
                        StartPT = new PointF(view.getX(), view.getY());
                        break;
                    }
            }
            polygonView.invalidate();
            return true;
        }
    }

    public PolygonView(Context context2) {
        super(context2);
        context = context2;
        init();
    }

    public PolygonView(Context context2, AttributeSet attributeSet) {
        super(context2, attributeSet);
        context = context2;
        init();
    }

    public PolygonView(Context context2, AttributeSet attributeSet, int i) {
        super(context2, attributeSet, i);
        context = context2;
        init();
    }

    private void init() {
        polygonView = this;
        pointer1 = getImageView(0, 0);
        pointer2 = getImageView(getWidth(), 0);
        pointer3 = getImageView(0, getHeight());
        pointer4 = getImageView(getWidth(), getHeight());
        midPointer13 = getImageView(0, getHeight() / 2);
        midPointer13.setOnTouchListener(new MidPointTouchListenerImpl(pointer1, pointer3));
        midPointer12 = getImageView(0, getWidth() / 2);
        midPointer12.setOnTouchListener(new MidPointTouchListenerImpl(pointer1, pointer2));
        midPointer34 = getImageView(0, getHeight() / 2);
        midPointer34.setOnTouchListener(new MidPointTouchListenerImpl(pointer3, pointer4));
        midPointer24 = getImageView(0, getHeight() / 2);
        midPointer24.setOnTouchListener(new MidPointTouchListenerImpl(pointer2, pointer4));
        addView(pointer1);
        addView(pointer2);
        addView(midPointer13);
        addView(midPointer12);
        addView(midPointer34);
        addView(midPointer24);
        addView(pointer3);
        addView(pointer4);
        initPaint();
    }

    public void attachViewToParent(View view, int i, LayoutParams layoutParams) {
        super.attachViewToParent(view, i, layoutParams);
    }

    private void initPaint() {
        paint = new Paint();
        paint.setColor(Color.parseColor("#ff59a9ff"));
        paint.setStrokeWidth(2.0f);
        paint.setAntiAlias(true);
    }

    public Map<Integer, PointF> getPoints() {
        ArrayList arrayList = new ArrayList();
        arrayList.add(new PointF(pointer1.getX(), pointer1.getY()));
        arrayList.add(new PointF(pointer2.getX(), pointer2.getY()));
        arrayList.add(new PointF(pointer3.getX(), pointer3.getY()));
        arrayList.add(new PointF(pointer4.getX(), pointer4.getY()));
        return getOrderedPoints(arrayList);
    }

    public Map<Integer, PointF> getOrderedPoints(List<PointF> list) {
        PointF pointF = new PointF();
        int size = list.size();
        for (PointF pointF2 : list) {
            float f = (float) size;
            pointF.x += pointF2.x / f;
            pointF.y += pointF2.y / f;
        }
        HashMap hashMap = new HashMap();
        for (PointF pointF3 : list) {
            int i = -1;
            if (pointF3.x < pointF.x && pointF3.y < pointF.y) {
                i = 0;
            } else if (pointF3.x > pointF.x && pointF3.y < pointF.y) {
                i = 1;
            } else if (pointF3.x < pointF.x && pointF3.y > pointF.y) {
                i = 2;
            } else if (pointF3.x > pointF.x && pointF3.y > pointF.y) {
                i = 3;
            }
            hashMap.put(Integer.valueOf(i), pointF3);
        }
        return hashMap;
    }

    public void setPoints(Map<Integer, PointF> map) {
        if (map.size() == 4) {
            setPointsCoordinates(map);
        }
    }

    private void setPointsCoordinates(Map<Integer, PointF> map) {
        pointer1.setX(((PointF) map.get(Integer.valueOf(0))).x);
        pointer1.setY(((PointF) map.get(Integer.valueOf(0))).y);
        pointer2.setX(((PointF) map.get(Integer.valueOf(1))).x);
        pointer2.setY(((PointF) map.get(Integer.valueOf(1))).y);
        pointer3.setX(((PointF) map.get(Integer.valueOf(2))).x);
        pointer3.setY(((PointF) map.get(Integer.valueOf(2))).y);
        pointer4.setX(((PointF) map.get(Integer.valueOf(3))).x);
        pointer4.setY(((PointF) map.get(Integer.valueOf(3))).y);
    }

    public void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        Canvas canvas2 = canvas;
        canvas2.drawLine(((float) (pointer1.getWidth() / 2)) + pointer1.getX(), ((float) (pointer1.getHeight() / 2)) + pointer1.getY(), ((float) (pointer3.getWidth() / 2)) + pointer3.getX(), ((float) (pointer3.getHeight() / 2)) + pointer3.getY(), paint);
        canvas2.drawLine(((float) (pointer1.getWidth() / 2)) + pointer1.getX(), ((float) (pointer1.getHeight() / 2)) + pointer1.getY(), ((float) (pointer2.getWidth() / 2)) + pointer2.getX(), ((float) (pointer2.getHeight() / 2)) + pointer2.getY(), paint);
        canvas2.drawLine(((float) (pointer2.getWidth() / 2)) + pointer2.getX(), ((float) (pointer2.getHeight() / 2)) + pointer2.getY(), ((float) (pointer4.getWidth() / 2)) + pointer4.getX(), ((float) (pointer4.getHeight() / 2)) + pointer4.getY(), paint);
        canvas2.drawLine(((float) (pointer3.getWidth() / 2)) + pointer3.getX(), ((float) (pointer3.getHeight() / 2)) + pointer3.getY(), ((float) (pointer4.getWidth() / 2)) + pointer4.getX(), ((float) (pointer4.getHeight() / 2)) + pointer4.getY(), paint);
        midPointer13.setX(pointer3.getX() - ((pointer3.getX() - pointer1.getX()) / 2.0f));
        midPointer13.setY(pointer3.getY() - ((pointer3.getY() - pointer1.getY()) / 2.0f));
        midPointer24.setX(pointer4.getX() - ((pointer4.getX() - pointer2.getX()) / 2.0f));
        midPointer24.setY(pointer4.getY() - ((pointer4.getY() - pointer2.getY()) / 2.0f));
        midPointer34.setX(pointer4.getX() - ((pointer4.getX() - pointer3.getX()) / 2.0f));
        midPointer34.setY(pointer4.getY() - ((pointer4.getY() - pointer3.getY()) / 2.0f));
        midPointer12.setX(pointer2.getX() - ((pointer2.getX() - pointer1.getX()) / 2.0f));
        midPointer12.setY(pointer2.getY() - ((pointer2.getY() - pointer1.getY()) / 2.0f));
    }

    private ImageView getImageView(int i, int i2) {
        ImageView imageView = new ImageView(context);
        imageView.setLayoutParams(new LayoutParams(-2, -2));
        imageView.setImageResource(R.drawable.circle);
        imageView.setX((float) i);
        imageView.setY((float) i2);
        imageView.setOnTouchListener(new TouchListenerImpl());
        return imageView;
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        return super.onTouchEvent(motionEvent);
    }

    public boolean isValidShape(Map<Integer, PointF> map) {
        return map.size() == 4;
    }
}
