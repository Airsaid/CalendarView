# CalendarView
这是一个Android平台上可多选的自定义日历控件

#效果图
 ![image](https://github.com/Airsaid/CalendarView/blob/master/gif/cacendarview.gif)

#使用步骤
1，初始化自定义日历View：
```
CalendarView mCalendarView = (CalendarView) findViewById(R.id.calendarView);
```

2，初始化可以被选择的天数数据：
```
List<String> mDatas = new ArrayList<>();
mDatas.add("20160801");
mDatas.add("20160802");
mDatas.add("20160803");
mDatas.add("20160816");
mDatas.add("20160817");
mDatas.add("20160826");
mDatas.add("20160910");
mDatas.add("20160911");
mDatas.add("20160912");
```
3，设置给自定义日历View：
```
// 设置可选日期
mCalendarView.setOptionalDate(mDatas);
```

#设置点击监听
```
mCalendarView.setOnClickDate(new CalendarView.OnClickListener() {
    @Override
    public void onClickDateListener(int year, int month, int day) {
        Toast.makeText(getApplication(), year + "年" + month + "月" + day + "天", Toast.LENGTH_SHORT).show();

        // 获取已选择日期
        List<String> dates = mCalendarView.getSelectedDates();
        for (String date : dates) {
            Log.e("test", "date: " + date);
        }
        
    }
});
```

如果只需要进行数据展示，而不需要点击，可以设置：
```
// 设置已选日期
mCalendarView.setSelectedDates(mDatas);
// 设置不可以被点击
mCalendarView.setClickable(false);
```

#代码片段
```
 // 绘制背景
        mPaint.setColor(mBgColor);
        canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), mPaint);

        mDays = new int[6][7];
        // 设置绘制字体大小
        mPaint.setTextSize(mDayTextSize * mMetrics.scaledDensity);
        // 设置绘制字体颜色

        String dayStr;
        // 获取当月一共有多少天
        mMonthDays = DateUtils.getMonthDays(mSelYear, mSelMonth);
        // 获取当月第一天位于周几
        mWeekNumber = DateUtils.getFirstDayWeek(mSelYear, mSelMonth);

        for(int day = 0; day < mMonthDays; day++){
            dayStr = String.valueOf(day + 1);
            int column  =  (day + mWeekNumber - 1) % 7;
            int row     =  (day + mWeekNumber - 1) / 7;
            mDays[row][column] = day + 1;
            int startX = (int) (mColumnSize * column + (mColumnSize - mPaint.measureText(dayStr)) / 2);
            int startY = (int) (mRowSize * row + mRowSize / 2 - (mPaint.ascent() + mPaint.descent()) / 2);

            // 判断当前天数是否可选
            if(mOptionalDates.contains(getSelData(mSelYear, mSelMonth, mDays[row][column]))){
                // 可选，继续判断是否是点击过的
                if(!mSelectedDates.contains(getSelData(mSelYear, mSelMonth, mDays[row][column]))){
                    // 没有点击过，绘制默认背景
                    canvas.drawBitmap(mBgNotOptBitmap, startX - 22, startY - 55, mPaint);
                    mPaint.setColor(mDayNormalColor);
                }else{
                    // 点击过，绘制点击过的背景
                    canvas.drawBitmap(mBgOptBitmap, startX - 22, startY - 55, mPaint);
                    mPaint.setColor(mDayPressedColor);
                }
                // 绘制天数
                canvas.drawText(dayStr, startX, startY - 10, mPaint);
            }else{
                mPaint.setColor(mDayNotOptColor);
                canvas.drawText(dayStr, startX, startY, mPaint);
            }
        }
```



