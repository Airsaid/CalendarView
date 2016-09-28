package com.arisaid.calendarview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import widget.CalendarView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mTextSelectMonth;
    private ImageButton mLastMonthView;
    private ImageButton mNextMonthView;
    private CalendarView mCalendarView;

    private List<String> mDatas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextSelectMonth = (TextView) findViewById(R.id.txt_select_month);
        mLastMonthView = (ImageButton) findViewById(R.id.img_select_last_month);
        mNextMonthView = (ImageButton) findViewById(R.id.img_select_next_month);
        mCalendarView = (CalendarView) findViewById(R.id.calendarView);
        mLastMonthView.setOnClickListener(this);
        mNextMonthView.setOnClickListener(this);

        // 初始化可选日期
        initData();

        // 设置可选日期
        mCalendarView.setOptionalDate(mDatas);
        // 设置已选日期
//        mCalendarView.setSelectedDates(mDatas);
        // 设置不可以被点击
//        mCalendarView.setClickable(false);

        // 设置点击事件
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

        mTextSelectMonth.setText(mCalendarView.getDate());
    }

    private void initData() {
        mDatas = new ArrayList<>();
        mDatas.add("20160801");
        mDatas.add("20160802");
        mDatas.add("20160803");
        mDatas.add("20160816");
        mDatas.add("20160817");
        mDatas.add("20160826");
        mDatas.add("20160910");
        mDatas.add("20160911");
        mDatas.add("20160912");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.img_select_last_month:
                mCalendarView.setLastMonth();
                mTextSelectMonth.setText(mCalendarView.getDate());
                break;
            case R.id.img_select_next_month:
                mCalendarView.setNextMonth();
                mTextSelectMonth.setText(mCalendarView.getDate());
                break;
        }
    }
}
