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
 *
 * {@link CalendarView} 演示例子, 以下方法都是可选操作, 更多方法请查看 {@link CalendarView}.
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private CalendarView mCalendarView;
    private TextView mTxtDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTxtDate = (TextView) findViewById(R.id.txt_date);
        mCalendarView = (CalendarView) findViewById(R.id.calendarView);

        // 设置已选的日期
        mCalendarView.setSelectDate(initData());

        // 指定显示的日期, 如当前月的下个月
        Calendar calendar = mCalendarView.getCalendar();
        calendar.add(Calendar.MONTH, 1);
        mCalendarView.setCalendar(calendar);

        // 设置字体
        mCalendarView.setTypeface(Typeface.SERIF);

        // 设置日期状态改变监听
        mCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, boolean select, int year, int month, int day) {
                Log.e(TAG, "select: " + select);
                Log.e(TAG, "year: " + year);
                Log.e(TAG, "month,: " + (month + 1));
                Log.e(TAG, "day: " + day);

                if(select){
                    Toast.makeText(getApplicationContext()
                            , "选中了：" + year + "年" + (month + 1) + "月" + day + "日", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext()
                            , "取消选中了：" + year + "年" + (month + 1) + "月" + day + "日", Toast.LENGTH_SHORT).show();
                }
            }
        });
        // 设置是否能够改变日期状态
        mCalendarView.setChangeDateStatus(true);

        // 设置日期点击监听
        mCalendarView.setOnDataClickListener(new CalendarView.OnDataClickListener() {
            @Override
            public void onDataClick(@NonNull CalendarView view, int year, int month, int day) {
                Log.e(TAG, "year: " + year);
                Log.e(TAG, "month,: " + (month + 1));
                Log.e(TAG, "day: " + day);
            }
        });
        // 设置是否能够点击
        mCalendarView.setClickable(true);

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
