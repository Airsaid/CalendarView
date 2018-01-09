package com.github.airsaid.calendarview.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.github.airsaid.calendarview.R;


/**
 * @author airsaid
 *
 * 自定义日历顶部星期 View.
 */
public class WeekView extends View {

    private String[] mWeeks = {"日", "一", "二", "三", "四", "五", "六"};

    private int mTextSize;
    private int mTextColor;
    private Typeface mTypeface;
    private final Paint mPaint;
    private float mMeasureTextWidth;

    public WeekView(Context context) {
        this(context, null);
    }

    public WeekView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WeekView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.WeekView);
        int textColor = a.getColor(R.styleable.WeekView_wv_textColor, Color.BLACK);
        setTextColor(textColor);
        int textSize = a.getDimensionPixelSize(R.styleable.WeekView_wv_textSize, -1);
        setTextSize(textSize);
        a.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        if(widthMode == MeasureSpec.AT_MOST){
            widthSize = (int) (mMeasureTextWidth * mWeeks.length) + getPaddingLeft() + getPaddingRight();
        }
        if(heightMode == MeasureSpec.AT_MOST){
            heightSize = (int) mMeasureTextWidth + getPaddingTop() + getPaddingBottom();
        }
        setMeasuredDimension(widthSize, heightSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(mTextSize != -1){
            mPaint.setTextSize(mTextSize);
        }
        if(mTypeface != null){
            mPaint.setTypeface(mTypeface);
        }
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mTextColor);
        int columnWidth = (getWidth() - getPaddingLeft() - getPaddingRight()) / 7;
        for(int i = 0; i < mWeeks.length; i++){
            String text = mWeeks[i];
            int fontWidth = (int) mPaint.measureText(text);
            int startX = columnWidth * i + (columnWidth - fontWidth) / 2 + getPaddingLeft();
            int startY = (int) ((getHeight()) / 2 - (mPaint.ascent() + mPaint.descent()) / 2) + getPaddingTop();
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
        mPaint.setTextSize(mTextSize);
        mMeasureTextWidth = mPaint.measureText(mWeeks[0]);
    }

    /**
     * 设置文字颜色.
     *
     * @param color 颜色.
     */
    public void setTextColor(@ColorInt int color){
        this.mTextColor = color;
        mPaint.setColor(mTextColor);
    }

    /**
     * 设置字体.
     *
     * @param typeface {@link Typeface}.
     */
    public void setTypeface(Typeface typeface){
        this.mTypeface = typeface;
        invalidate();
    }

    /**
     * 获取 {@link Paint} 对象.
     * @return {@link Paint}.
     */
    public Paint getPaint(){
        return mPaint;
    }
}