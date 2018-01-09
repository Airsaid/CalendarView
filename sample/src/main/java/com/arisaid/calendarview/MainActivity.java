package com.arisaid.calendarview;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.github.airsaid.calendarview.widget.CalendarView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


/**
 * @author airsaid
 */
public class MainActivity extends AppCompatActivity {

    private CalendarView mCalendarView;
    private TextView mTxtDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTxtDate = (TextView) findViewById(R.id.txt_date);
        mCalendarView = (CalendarView) findViewById(R.id.calendarView);

        // 设置已选的日期 (可选操作)
        mCalendarView.setSelectDate(initData());

        // 指定显示的日期, 如当前月的下个月 (可选操作)
        Calendar calendar = mCalendarView.getCalendar();
        calendar.add(Calendar.MONTH, 1);
        mCalendarView.setCalendar(calendar);

        // 设置字体
        mCalendarView.setTypeface(Typeface.SERIF);

        mCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, boolean select, int year, int month, int day) {
                Log.e("test", "select: " + select);
                Log.e("test", "year: " + year);
                Log.e("test", "month,: " + (month + 1));
                Log.e("test", "day: " + day);

                if(select){
                    Toast.makeText(getApplicationContext()
                            , "选中了：" + year + "年" + (month + 1) + "月" + day + "日", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext()
                            , "取消选中了：" + year + "年" + (month + 1) + "月" + day + "日", Toast.LENGTH_SHORT).show();
                }
            }
        });
        setCurDate();
    }

    private List<String> initData() {
        List<String> dates = new ArrayList<>();
        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        SimpleDateFormat sdf = new SimpleDateFormat(mCalendarView.getDateFormatPattern(), Locale.CHINA);
        sdf.format(calendar.getTime());
        dates.add(sdf.format(calendar.getTime()));
        return dates;
    }

    public void next(View v){
        mCalendarView.nextMonth();
        setCurDate();
    }

    public void last(View v){
        mCalendarView.lastMonth();
        setCurDate();
    }

    private void setCurDate(){
        mTxtDate.setText(mCalendarView.getYear() + "年" + (mCalendarView.getMonth() + 1) + "月");
    }
}
