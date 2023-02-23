package com.noahliu.horizontalrecyclerviewwithindicatorexample;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

public class DotIndicator extends View {
    private int thumbColor = Color.parseColor("#00A0AF");
    private int trackColor = Color.parseColor("#ADD0EB");

    private Paint paint;
    private int pageNum;
    private int currentPosition;
    private int gapSize;
    private float radius;
    private int colorOn;
    private int colorOff;


    public DotIndicator(Context context) {
        super(context);
        init();
    }

    public DotIndicator(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DotIndicator(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setThumbColor(int thumbColor) {
        this.thumbColor = thumbColor;
    }

    public void setTrackColor(int trackColor) {
        this.trackColor = trackColor;
    }

    private void init() {
        radius = dp2px(10f);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        colorOn = thumbColor;
        colorOff = trackColor;
        gapSize = dp2px(30f);
    }
    //獲取現在的位置
    public void onPageScrolled(int position) {
        currentPosition = position;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (pageNum <= 0) {
            return;
        }
        //計算標籤的位置
        float left = (getWidth() - (pageNum - 1) * gapSize) * 0.5f;
        float height = getHeight() * 0.5f;
        paint.setColor(colorOff);
        //根據數量來畫出點點的數量
        for (int i = 0; i < pageNum; i++) {
            canvas.drawCircle(left + i * gapSize, height, radius, paint);
        }
        paint.setColor(colorOn);
        //設置被選中的點點將之設為深色
        canvas.drawCircle(left + currentPosition * gapSize,
                height, radius, paint);
    }

    //取得item的數量
    public void setPageNum(int num) {
        pageNum = num;
    }

    private int dp2px(float dp) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dp * scale * 0.5f);
    }
}
