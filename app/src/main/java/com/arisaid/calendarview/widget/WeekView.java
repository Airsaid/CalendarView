package com.arisaid.calendarview.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.arisaid.calendarview.R;

/**
 * @author airsaid
 *
 * 自定义日历顶部星期 View.
 */
public class WeekView extends View {

    private String[] mWeeks = {"日", "一", "二", "三", "四", "五", "六"};

    private int mTextSize;
    private int mTextColor;
    private final Paint mPaint;

    public WeekView(Context context) {
        this(context, null);
    }

    public WeekView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WeekView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.WeekView);
        int textColor = a.getColor(R.styleable.WeekView_wv_textColor, Color.BLACK);
        setTextColor(textColor);
        int textSize = a.getDimensionPixelSize(R.styleable.WeekView_wv_textSize, -1);
        setTextSize(textSize);
        a.recycle();

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(mTextSize != -1){
            mPaint.setTextSize(mTextSize);
        }
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mTextColor);
        int columnWidth = getWidth() / 7;
        for(int i = 0; i < mWeeks.length; i++){
            String text = mWeeks[i];
            int fontWidth = (int) mPaint.measureText(text);
            int startX = columnWidth * i + (columnWidth - fontWidth) / 2;
            int startY = (int) (getHeight() / 2 - (mPaint.ascent() + mPaint.descent()) / 2);
            canvas.drawText(text, startX, startY, mPaint);
        }
    }

    /**
     * 设置字体大小.
     *
     * @param size text size.
     */
    public void setTextSize(int size){
        this.mTextSize = size;
    }

    /**
     * 设置文字颜色.
     *
     * @param color 颜色.
     */
    public void setTextColor(@ColorInt int color){
        this.mTextColor = color;
    }
}
