# CalendarView
这是一个 Android 平台上可多选的自定义日历控件。

# 效果图
 ![image](https://github.com/Airsaid/CalendarView/blob/master/gif/cacendarview.gif)

# 使用步骤
1，初始化自定义日历 View：
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
3，设置给自定义日历 View：
```
// 设置可选日期
mCalendarView.setOptionalDate(mDatas);
```

# 设置点击监听
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

# 联系我
* 博客：http://blog.csdn.net/airsaid
* QQ 群：5707887
