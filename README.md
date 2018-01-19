# CalendarView
Android 平台上继承 View 实现的自定义日历控件.

# 效果图
 ![image](https://github.com/Airsaid/CalendarView/blob/master/preview/preview.gif)

# 快速开始
在你的 ```build.gradle``` 文件里添加：
```
compile 'com.github.airsaid:calendarview:1.0.2'
```

# 用法示例
1、布局中：
```
<com.github.airsaid.library.widget.WeekView
    android:layout_width="match_parent"
    android:layout_height="30dp"
    android:layout_marginTop="10dp"
    android:background="@android:color/white"
    app:wv_textColor="#333333"
    app:wv_textSize="14sp"/>
    
<com.github.airsaid.library.widget.CalendarView
    android:id="@+id/calendarView"
    android:layout_width="match_parent"
    android:layout_height="300dp"
    android:background="@android:color/white"
    app:cv_dayBackground="@drawable/bg_day_un_selected"
    app:cv_selectDayBackground="@drawable/bg_day_selected"
    app:cv_selectTextColor="@android:color/white"
    app:cv_textColor="#333333"
    app:cv_textSize="14sp"/>
```

2、设置监听器：
- 设置点击监听：
```
mCalendarView.setOnDataClickListener(new CalendarView.OnDataClickListener() {
            @Override
            public void onDataClick(@NonNull CalendarView view, int year, int month, int day) {
                Log.e("test", "year: " + year);
                Log.e("test", "month,: " + (month + 1));
                Log.e("test", "day: " + day);
            }
        });
```
- 设置日期选中或取消选中监听：
```
mCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
    @Override
    public void onSelectedDayChange(@NonNull CalendarView view, boolean select, int year, int month, int day) {
        if(select){
            Toast.makeText(getApplicationContext()
                    , "选中了：" + year + "年" + (month + 1) + "月" + day + "日", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getApplicationContext()
                    , "取消选中了：" + year + "年" + (month + 1) + "月" + day + "日", Toast.LENGTH_SHORT).show();
        }
    }
});
```

除此之外，还有一些其他的额外方法：

- 设置已选天数数据：
```
List<String> data = new ArrayList();
// 这里的日期格式可以通过 setDateFormatPattern() 方法设置，默认是 yyyyMMdd
data.add("20171229");
data.add("20171230");
mCalendarView.setSelectDate(data);
```

- 设置显示指定的日期（如当前月的下个月）：
```
Calendar calendar = mCalendarView.getCalendar();
calendar.add(Calendar.MONTH, 1);
mCalendarView.setCalendar(calendar);
```

- 设置字体：
```
mCalendarView.setTypeface(typeface);
```

- 设置是否可以点击：

```
mCalendarView.setClickable(boolean);

```
- 设置是否可以改变日期状态：
```
mCalendarView.setChangeDateStatus(boolean);
```

# 属性 & 方法
| 属性名| 方法| 作用 |
|------------|-----------|--------|
| cv_textColor| setTextColor(@ColorInt int textColor)|设置默认文字颜色 |
| cv_selectTextColor| setSelectTextColor(@ColorInt int textColor)|设置选中后文字颜色 |
| cv_textSize| setTextSize(float textSize)|设置默认文字大小 |
| cv_selectTextSize | setSelectTextSize(float textSize)|设置选中后文字大小 |
| cv_dayBackground | setDayBackground(Drawable background)|设置默认天的背景 |
| cv_selectDayBackground | setSelectDayBackground(Drawable background)|设置选中后天的背景 |
| cv_dateFormatPattern | setDateFormatPattern(String pattern)|设置日期格式化格式 |
| cv_isChangeDateStatus | setChangeDateStatus(boolean isChanged)|设置是否能够改变日期状态 (默认或选中状态)|

# TODO
- [ ] 添加长按事件。
- [ ] 自定义天的视图。
- [ ] 添加当前天的自定义配置。

# 联系我
- **QQ 群：** 5707887
- **Email：** airsaid1024@gmail.com
- **Blog：**[http://blog.csdn.net/airsaid](http://blog.csdn.net/airsaid)