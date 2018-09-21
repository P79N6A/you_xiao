package com.runto.cources.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
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
import com.runtoinfo.httpUtils.HttpEntity;
import com.runtoinfo.httpUtils.bean.CourseEntity;
import com.runtoinfo.httpUtils.bean.RequestDataEntity;
import com.runtoinfo.httpUtils.utils.HttpUtils;
import com.runtoinfo.youxiao.globalTools.timepicker.CustomDatePicker;
import com.runtoinfo.youxiao.globalTools.utils.Entity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Route(path = "/cources/colorfulActivity")
public class ColorfulActivity extends BaseActivity implements
        CalendarView.OnDateSelectedListener,
        CalendarView.OnYearChangeListener,
        View.OnClickListener {

    TextView mTextYear;
    CalendarView mCalendarView;
    RelativeLayout mRelativeTool;
    private int mYear;
    CalendarLayout mCalendarLayout;
    GroupRecyclerView mRecyclerView;
    public ImageView imgMenu;
    public ActivityCourceBinding binding;
    public List<CourseEntity> dataList = new ArrayList<>();
    public CustomDatePicker customDatePicker;

    public static void show(Context context) {
        context.startActivity(new Intent(context, ColorfulActivity.class));
    }


    @Override
    protected void initView() {
        binding = DataBindingUtil.setContentView(ColorfulActivity.this, R.layout.activity_cource);
        setStatusBarDarkMode();
        mTextYear = (TextView) findViewById(R.id.tv_year);
        mRelativeTool = (RelativeLayout) findViewById(R.id.rl_tool);
        mCalendarView = (CalendarView) findViewById(R.id.calendarView);
        imgMenu = findViewById(R.id.course_message_menu);
        imgMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ARouter.getInstance().build("/information/informationActivity").navigation();
                initDatePicker();
                customDatePicker.show(mCalendarView.getCurYear() + "-" + mCalendarView.getCurMonth() + "-" + mCalendarView.getCurDay());
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

        binding.monthUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCalendarView.scrollToPre();
            }
        });

        binding.monthNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCalendarView.scrollToNext();
            }
        });
    }

    @Override
    protected void initData() {
        dataList.clear();
        Map<String, Object> map = new HashMap<>();
        map.put("startDate", mCalendarView.getCurYear() + "-" + mCalendarView.getCurMonth() + "-" + mCalendarView.getCurDay());
        map.put("endDate", mCalendarView.getCurYear() + "-" + mCalendarView.getCurMonth() + "-" + (mCalendarView.getCurDay() + 1));
        RequestDataEntity requestDataEntity = new RequestDataEntity();
        requestDataEntity.setUrl(HttpEntity.MAIN_URL + HttpEntity.GET_USER_COURSE_LIST);
        requestDataEntity.setToken(spUtils.getString(Entity.TOKEN));
        HttpUtils<CourseEntity> utils = new HttpUtils<>(getBaseContext());
        utils.getCouseAll(handler, requestDataEntity, map, dataList);
    }

    public Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    mRecyclerView = (GroupRecyclerView) findViewById(R.id.recyclerView);
                    List<Calendar> schemes = new ArrayList<>();
                    int year = mCalendarView.getCurYear();
                    int month = mCalendarView.getCurMonth();
                    mRecyclerView.setLayoutManager(new LinearLayoutManager(ColorfulActivity.this));
                    mRecyclerView.addItemDecoration(new GroupItemDecoration<String, Article>());
                    //etListData();
                    if (dataList.size() == 0){
                        CourseEntity entity = new CourseEntity();
                        entity.setType(1);
                        entity.setCourseMessage("今日没有课程");
                        dataList.add(entity);
                    }

                    schemes.add(getSchemeCalendar(year, month, 3, Color.parseColor("#3aa6fe"), "假"));
                    schemes.add(getSchemeCalendar(year, month, 8, Color.parseColor("#999999"), ""));
                    mCalendarView.setSchemeDate(schemes);

                    mRecyclerView.setAdapter(new ArticleAdapter(ColorfulActivity.this, dataList, handler));
                    mRecyclerView.notifyDataSetChanged();
                    break;
                case 1:
                    break;

            }
        }
    };


    public void setListData(){
        CourseEntity entity = new CourseEntity();
        entity.setType(0);
        entity.setCourseName("钢琴");
        entity.setClassroomName("钢琴shi-");
        entity.setDate("2018-09-12 10:00");
        entity.setTeacherName("Miss Li");
        entity.setProgress(20);
        entity.setStartTime("18:00");
        entity.setEndTime("20:00");
        entity.setHomeworkRequirement("练习曲谱");
        dataList.add(entity);
    }


    @Override
    public void onClick(View v) {
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
        mTextYear.setVisibility(View.VISIBLE);
        mTextYear.setText(String.valueOf(calendar.getYear()) + "年" + calendar.getMonth() + "月");
        mYear = calendar.getYear();
    }


    @Override
    public void onYearChange(int year) {
        //mTextMonthDay.setText(String.valueOf(year));
    }

    //时间选择器
    private void initDatePicker() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        String now = sdf.format(new Date());
        //binding.personalEditBirth.setText(now.split(" ")[0]);
        //currentTime.setText(now);

        customDatePicker = new CustomDatePicker(this, new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) { // 回调接口，获得选中的时间
                String result= time.split(" ")[0];
                String [] part = result.split("-");
                mCalendarView.scrollToCalendar(Integer.parseInt(part[0]), Integer.parseInt(part[1]), Integer.parseInt(part[2]));
            }
        }, "1970-01-01 00:00", now); // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
        customDatePicker.showSpecificTime(false); // 不显示时和分
        customDatePicker.setIsLoop(true); // 不允许循环滚动

    }

}
