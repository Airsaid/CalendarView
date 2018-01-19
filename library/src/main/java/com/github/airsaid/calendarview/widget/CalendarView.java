package com.github.airsaid.calendarview.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import com.github.airsaid.calendarview.R;
import com.github.airsaid.calendarview.util.DateUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * @author airsaid
 *
 * 自定义可多选日历 View.
 */
public class CalendarView extends View {

    /** 默认的日期格式化格式 */
    private static final String DATE_FORMAT_PATTERN = "yyyyMMdd";

    /** 默认文字颜色 */
    private int mTextColor;
    /** 选中后文字颜色 */
    private int mSelectTextColor;
    /** 默认文字大小 */
    private float mTextSize;
    /** 选中后文字大小 */
    private float mSelectTextSize;
    /** 默认天的背景 */
    private Drawable mDayBackground;
    /** 选中后天的背景 */
    private Drawable mSelectDayBackground;
    /** 日期格式化格式 */
    private String mDateFormatPattern;
    /** 字体 */
    private Typeface mTypeface;
    /** 日期状态是否能够改变 */
    private boolean mIsChangeDateStatus;

    /** 每列宽度 */
    private int mColumnWidth;
    /** 每行高度 */
    private int mRowHeight;
    /** 已选择日期数据 */
    private List<String> mSelectDate;
    /** 存储对应列行处的天 */
    private int[][] mDays = new int[6][7];

    private OnDataClickListener  mOnDataClickListener;
    private OnDateChangeListener mChangeListener;
    private SimpleDateFormat mDateFormat;
    private Calendar mSelectCalendar;
    private Calendar mCalendar;
    private Paint mPaint;
    private int mSlop;

    public interface OnDataClickListener{

        /**
         * 日期点击监听.
         * @param view     与次监听器相关联的 View.
         * @param year     对应的年.
         * @param month    对应的月.
         * @param day      对应的日.
         */
        void onDataClick(@NonNull CalendarView view, int year, int month, int day);
    }

    public interface OnDateChangeListener {

        /**
         * 选中的天发生了改变监听回调, 改变有 2 种, 分别是选中和取消选中.
         * @param view     与次监听器相关联的 View.
         * @param select   true 表示是选中改变, false 是取消改变.
         * @param year     对应的年.
         * @param month    对应的月.
         * @param day      对应的日.
         */
        void onSelectedDayChange(@NonNull CalendarView view, boolean select, int year, int month, int day);
    }

    public CalendarView(Context context) {
        this(context, null);
    }

    public CalendarView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CalendarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mSelectCalendar = Calendar.getInstance(Locale.CHINA);
        mCalendar = Calendar.getInstance(Locale.CHINA);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mSelectDate = new ArrayList<>();
        setClickable(true);
        initAttrs(attrs);
    }

    private void initAttrs(AttributeSet attrs){
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.CalendarView);

        int textColor = a.getColor(R.styleable.CalendarView_cv_textColor, Color.BLACK);
        setTextColor(textColor);

        int selectTextColor = a.getColor(R.styleable.CalendarView_cv_selectTextColor, Color.BLACK);
        setSelectTextColor(selectTextColor);

        float textSize = a.getDimension(R.styleable.CalendarView_cv_textSize, sp2px(14));
        setTextSize(textSize);

        float selectTextSize = a.getDimension(R.styleable.CalendarView_cv_selectTextSize, sp2px(14));
        setSelectTextSize(selectTextSize);

        Drawable dayBackground = a.getDrawable(R.styleable.CalendarView_cv_dayBackground);
        setDayBackground(dayBackground);

        Drawable selectDayBackground = a.getDrawable(R.styleable.CalendarView_cv_selectDayBackground);
        setSelectDayBackground(selectDayBackground);

        String pattern = a.getString(R.styleable.CalendarView_cv_dateFormatPattern);
        setDateFormatPattern(pattern);

        boolean isChange = a.getBoolean(R.styleable.CalendarView_cv_isChangeDateStatus, false);
        setChangeDateStatus(isChange);

        a.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mColumnWidth = getWidth() / 7;
        mRowHeight = getHeight() / 6;
        mPaint.setTextSize(mTextSize);

        int year  = mCalendar.get(Calendar.YEAR);
        // 获取的月份要少一月, 所以这里 + 1
        int month = mCalendar.get(Calendar.MONTH) + 1;
        // 获取当月的天数
        int days  = DateUtils.getMonthDays(year, month);
        // 获取当月第一天位于周几
        int week  = DateUtils.getFirstDayWeek(year, month);
        // 绘制每天
        for (int day = 1; day <= days; day++) {
            // 获取天在行、列的位置
            int column  =  (day + week - 1) % 7;
            int row     =  (day + week - 1) / 7;
            // 存储对应天
            mDays[row][column] = day;

            String dayStr = String.valueOf(day);
            float textWidth = mPaint.measureText(dayStr);
            int x = (int) (mColumnWidth * column + (mColumnWidth - textWidth) / 2);
            int y = (int) (mRowHeight * row + mRowHeight / 2 - (mPaint.ascent() + mPaint.descent()) / 2);

            // 判断 day 是否在选择日期内
            if(mSelectDate == null || mSelectDate.size() == 0 ||
                    !mSelectDate.contains(getFormatDate(year, month - 1, day))){
                // 没有则绘制默认背景和文字颜色
                drawBackground(canvas, mDayBackground, column, row);
                drawText(canvas, dayStr, mTextColor, mTextSize, x, y);
            }else{
                // 否则绘制选择后的背景和文字颜色
                drawBackground(canvas, mSelectDayBackground, column, row);
                drawText(canvas, dayStr, mSelectTextColor, mSelectTextSize, x, y);
            }
        }
    }

    private void drawBackground(Canvas canvas, Drawable background, int column, int row){
        if(background != null){
            canvas.save();
            int dx = (mColumnWidth * column) + (mColumnWidth / 2) -
                    (background.getIntrinsicWidth() / 2);
            int dy = (mRowHeight * row) + (mRowHeight / 2) -
                    (background.getIntrinsicHeight() / 2);
            canvas.translate(dx, dy);
            background.draw(canvas);
            canvas.restore();
        }
    }

    private void drawText(Canvas canvas, String text, @ColorInt int color, float size, int x, int y){
        mPaint.setColor(color);
        mPaint.setTextSize(size);
        if(mTypeface != null){
            mPaint.setTypeface(mTypeface);
        }
        canvas.drawText(text, x, y, mPaint);
    }

    private int mDownX = 0, mDownY = 0;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(!isClickable()){
            return false;
        }
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                mDownX = (int) event.getX();
                mDownY = (int) event.getY();
                return true;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                int upX = (int) event.getX();
                int upY = (int) event.getY();
                int diffX = Math.abs(upX - mDownX);
                int diffY = Math.abs(upY - mDownY);
                if(diffX < mSlop && diffY < mSlop){
                    int column = upX / mColumnWidth;
                    int row    = upY / mRowHeight;
                    onClick(mDays[row][column]);
                }
                break;
            default:
        }
        return super.onTouchEvent(event);
    }

    private void onClick(int day){
        if(day < 1){
            return;
        }

        int year = mCalendar.get(Calendar.YEAR);
        int month = mCalendar.get(Calendar.MONTH);
        if(mOnDataClickListener != null){
            mOnDataClickListener.onDataClick(this, year, month, day);
        }

        if(mIsChangeDateStatus){
            // 如果选中的天已经选择则取消选中
            String date = getFormatDate(year, month, day);
            if(mSelectDate != null && mSelectDate.contains(date)){
                mSelectDate.remove(date);
                if(mChangeListener != null){
                    mChangeListener.onSelectedDayChange(this, false, year, month, day);
                }
            }else{
                if(mSelectDate == null){
                    mSelectDate = new ArrayList<>();
                }
                mSelectDate.add(date);
                if(mChangeListener != null){
                    mChangeListener.onSelectedDayChange(this, true, year, month, day);
                }
            }
            invalidate();
        }
    }

    /**
     * 设置选中的日期数据.
     *
     * @param days 日期数据, 日期格式为 {@link #setDateFormatPattern(String)} 方法所指定,
     * 如果没有设置则以默认的格式 {@link #DATE_FORMAT_PATTERN} 进行格式化.
     */
    public void setSelectDate(List<String> days){
        this.mSelectDate = days;
        invalidate();
    }

    /**
     * 获取选中的日期数据.
     *
     * @return 日期数据.
     */
    public List<String> getSelectDate(){
        return mSelectDate;
    }

    /**
     * 切换到下一个月.
     */
    public void nextMonth(){
        mCalendar.add(Calendar.MONTH, 1);
        invalidate();
    }

    /**
     * 切换到上一个月.
     */
    public void lastMonth(){
        mCalendar.add(Calendar.MONTH, -1);
        invalidate();
    }

    /**
     * 获取当前年份.
     *
     * @return year.
     */
    public int getYear(){
        return mCalendar.get(Calendar.YEAR);
    }

    /**
     * 获取当前月份.
     *
     * @return month. (思考后, 决定这里直接按 Calendar 的 API 进行返回, 不进行 +1 处理)
     */
    public int getMonth(){
        return mCalendar.get(Calendar.MONTH);
    }

    /**
     * 设置当前显示的 Calendar 对象.
     *
     * @param calendar 对象.
     */
    public void setCalendar(Calendar calendar){
        this.mCalendar = calendar;
        invalidate();
    }

    /**
     * 获取当前显示的 Calendar 对象.
     *
     * @return Calendar 对象.
     */
    public Calendar getCalendar(){
        return mCalendar;
    }

    /**
     * 设置文字颜色.
     *
     * @param textColor 文字颜色 {@link ColorInt}.
     */
    public void setTextColor(@ColorInt int textColor){
        this.mTextColor = textColor;
    }

    /**
     * 设置选中后的的文字颜色.
     *
     * @param textColor 文字颜色 {@link ColorInt}.
     */
    public void setSelectTextColor(@ColorInt int textColor){
        this.mSelectTextColor = textColor;
    }

    /**
     * 设置文字大小.
     *
     * @param textSize 文字大小 (sp).
     */
    public void setTextSize(float textSize){
        this.mTextSize = textSize;
    }

    /**
     * 设置选中后的的文字大小.
     *
     * @param textSize 文字大小 (sp).
     */
    public void setSelectTextSize(float textSize){
        this.mSelectTextSize = textSize;
    }

    /**
     * 设置天的背景.
     *
     * @param background 背景 drawable.
     */
    public void setDayBackground(Drawable background){
        if(background != null && mDayBackground != background){
            this.mDayBackground = background;
            setCompoundDrawablesWithIntrinsicBounds(mDayBackground);
        }
    }

    /**
     * 设置选择后天的背景.
     *
     * @param background 背景 drawable.
     */
    public void setSelectDayBackground(Drawable background){
        if(background != null && mSelectDayBackground != background){
            this.mSelectDayBackground = background;
            setCompoundDrawablesWithIntrinsicBounds(mSelectDayBackground);
        }
    }

    /**
     * 设置日期格式化格式.
     *
     * @param pattern 格式化格式, 如: yyyy-MM-dd.
     */
    public void setDateFormatPattern(String pattern){
        if(!TextUtils.isEmpty(pattern)){
            this.mDateFormatPattern = pattern;
        }else{
            this.mDateFormatPattern = DATE_FORMAT_PATTERN;
        }
        this.mDateFormat = new SimpleDateFormat(mDateFormatPattern, Locale.CHINA);
    }

    /**
     * 获取日期格式化格式.
     *
     * @return 格式化格式.
     */
    public String getDateFormatPattern(){
        return mDateFormatPattern;
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

    /**
     * 设置点击是否能够改变日期状态 (默认或选中状态).
     *
     * 默认是 false, 即点击只会响应点击事件 {@link OnDataClickListener}, 日期状态而不会做出任何改变.
     *
     * @param isChanged 是否能改变日期状态.
     */
    public void setChangeDateStatus(boolean isChanged){
        this.mIsChangeDateStatus = isChanged;
    }

    /**
     * 获取是否能改变日期状态.
     *
     * @return {@link #mIsChangeDateStatus}.
     */
    public boolean isChangeDateStatus(){
        return mIsChangeDateStatus;
    }

    /**
     * 设置日期点击监听.
     *
     * @param listener 被通知的监听器.
     */
    public void setOnDataClickListener(OnDataClickListener listener){
        this.mOnDataClickListener = listener;
    }

    /**
     * 设置选中日期改变监听器.
     *
     * @param listener 被通知的监听器.
     */
    public void setOnDateChangeListener(OnDateChangeListener listener) {
        this.mChangeListener = listener;
    }

    /**
     * 根据指定的年月日按当前日历的格式格式化后返回.
     *
     * @param year  年.
     * @param month 月.
     * @param day   日.
     * @return 格式化后的日期.
     */
    public String getFormatDate(int year, int month, int day){
        mSelectCalendar.set(year, month, day);
        return mDateFormat.format(mSelectCalendar.getTime());
    }

    private void setCompoundDrawablesWithIntrinsicBounds(Drawable drawable){
        if(drawable != null){
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        }
    }

    private int sp2px(float spVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                spVal, getContext().getResources().getDisplayMetrics());
    }
}
