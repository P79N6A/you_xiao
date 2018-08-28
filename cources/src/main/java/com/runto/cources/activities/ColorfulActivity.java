package com.runto.cources.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.runto.cources.R;
import com.runto.cources.adapter.ArticleAdapter;
import com.runto.cources.bean.Article;
import com.runto.cources.databinding.ActivityCourceBinding;
import com.runto.cources.group.GroupItemDecoration;
import com.runto.cources.group.GroupRecyclerView;
import com.runto.cources.ui.Calendar;
import com.runto.cources.ui.CalendarLayout;
import com.runto.cources.ui.CalendarView;

import java.util.ArrayList;
import java.util.List;
@Route(path = "/cources/colorfulActivity")
public class ColorfulActivity extends BaseActivity implements
        CalendarView.OnDateSelectedListener,
        CalendarView.OnYearChangeListener,
        View.OnClickListener {

    //TextView mTextMonthDay;

    TextView mTextYear;

    //TextView mTextLunar;

    //TextView mTextCurrentDay;

    CalendarView mCalendarView;

    RelativeLayout mRelativeTool;
    private int mYear;
    CalendarLayout mCalendarLayout;
    GroupRecyclerView mRecyclerView;
    public ImageView imgMenu;
    public ActivityCourceBinding binding;
    public static void show(Context context) {
        context.startActivity(new Intent(context, ColorfulActivity.class));
    }

//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
//        super.onCreate(savedInstanceState, persistentState);
//        binding = DataBindingUtil.setContentView(this, R.layout.activity_cource);
//        initView();
//        initData();
//    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_cource);
        setStatusBarDarkMode();
        mTextYear = (TextView) findViewById(R.id.tv_year);
        mRelativeTool = (RelativeLayout) findViewById(R.id.rl_tool);
        mCalendarView = (CalendarView) findViewById(R.id.calendarView);
        imgMenu = findViewById(R.id.course_message_menu);
        imgMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build("/information/informationActivity").navigation();
            }
        });
        mTextYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mCalendarLayout.isExpand()) {
                    mCalendarView.showYearSelectLayout(mYear);
                    return;
                }
                mCalendarView.showYearSelectLayout(mYear);
            }
        });
        findViewById(R.id.tv_today).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCalendarView.scrollToCurrent();
            }
        });
        mCalendarLayout = (CalendarLayout) findViewById(R.id.calendarLayout);
        mCalendarView.setOnDateSelectedListener(this);
        mCalendarView.setOnYearChangeListener(this);
        mTextYear.setText(String.valueOf(mCalendarView.getCurYear()));
        mYear = mCalendarView.getCurYear();

        findViewById(R.id.activity_img_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
    @Override
    protected void initData() {
        List<Calendar> schemes = new ArrayList<>();
        int year = mCalendarView.getCurYear();
        int month = mCalendarView.getCurMonth();

//        schemes.add(getSchemeCalendar(year, month, 3, 0xFF40db25, "假"));
//        schemes.add(getSchemeCalendar(year, month, 6, 0xFFe69138, "事"));
//        schemes.add(getSchemeCalendar(year, month, 9, 0xFFdf1356, "议"));
//        schemes.add(getSchemeCalendar(year, month, 13, 0xFFedc56d, "记"));
//        schemes.add(getSchemeCalendar(year, month, 14, 0xFFedc56d, "记"));
//        schemes.add(getSchemeCalendar(year, month, 15, 0xFFaacc44, "假"));
//        schemes.add(getSchemeCalendar(year, month, 18, 0xFFbc13f0, "记"));
//        schemes.add(getSchemeCalendar(year, month, 25, 0xFF13acf0, "假"));
//        mCalendarView.setSchemeDate(schemes);

        mRecyclerView = (GroupRecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new GroupItemDecoration<String, Article>());
        mRecyclerView.setAdapter(new ArticleAdapter(this, "20"));
        mRecyclerView.notifyDataSetChanged();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.ll_flyme:
//                MeiZuActivity.show(this);
//                break;
//            case R.id.ll_simple:
//                SimpleActivity.show(this);
//                break;
//            case R.id.ll_colorful:
//                ColorfulActivity.show(this);
//                break;
//            case R.id.ll_index:
//                IndexActivity.show(this);
//                break;
        }
    }

    private Calendar getSchemeCalendar(int year, int month, int day, int color, String text) {
        Calendar calendar = new Calendar();
        calendar.setYear(year);
        calendar.setMonth(month);
        calendar.setDay(day);
        calendar.setSchemeColor(color);//如果单独标记颜色、则会使用这个颜色
        calendar.setScheme(text);
        return calendar;
    }



    @SuppressLint("SetTextI18n")
    @Override
    public void onDateSelected(Calendar calendar, boolean isClick) {
        //mTextLunar.setVisibility(View.VISIBLE);
        mTextYear.setVisibility(View.VISIBLE);
        //mTextMonthDay.setText(calendar.getMonth() + "月" + calendar.getDay() + "日");
        mTextYear.setText(String.valueOf(calendar.getYear()) + "年" + calendar.getMonth() + "月");
        //mTextLunar.setText(calendar.getLunar());
        mYear = calendar.getYear();
    }


    @Override
    public void onYearChange(int year) {
        //mTextMonthDay.setText(String.valueOf(year));
    }



}
