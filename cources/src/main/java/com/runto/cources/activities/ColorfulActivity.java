package com.runto.cources.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;


import com.alibaba.android.arouter.facade.annotation.Route;
import com.qjc.library.StatusBarUtil;
import com.runto.cources.R;
import com.runto.cources.adapter.ArticleAdapter;
import com.runto.cources.bean.Article;
import com.runto.cources.databinding.ActivityCourceBinding;
import com.runto.cources.group.GroupItemDecoration;
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
@SuppressWarnings("all")
public class ColorfulActivity extends BaseActivity implements
        CalendarView.OnDateSelectedListener,
        CalendarView.OnYearChangeListener,
        CalendarView.OnMonthChangeListener,
        View.OnClickListener {

    private int mYear;
    CalendarLayout mCalendarLayout;
    public ActivityCourceBinding binding;
    public List<CourseEntity> dataList = new ArrayList<>();
    public List<CourseEntity> requestDataList;
    public List<CourseEntity> monthDataList;
    public CustomDatePicker customDatePicker;
    public HttpUtils httpUtils;
    public ArticleAdapter articleAdapter;
    public final String N = "-";

    public final long MIN_CLICK_TIME = 1000;
    public long lastClickTime = 0;

    public static void show(Context context) {
        context.startActivity(new Intent(context, ColorfulActivity.class));
    }

    @Override
    protected void initView() {
        binding = DataBindingUtil.setContentView(ColorfulActivity.this, R.layout.activity_cource);
        httpUtils = new HttpUtils(this);
        //setStatusBarDarkMode();
        binding.courseMessageMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initDatePicker();
                customDatePicker.show(binding.calendarView.getCurYear() + "-" + binding.calendarView.getCurMonth() + "-" + binding.calendarView.getCurDay());
            }
        });
        binding.tvYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mCalendarLayout.isExpand()) {
                    binding.calendarView.showYearSelectLayout(mYear);
                    return;
                }
                binding.calendarView.showYearSelectLayout(mYear);
            }
        });
        findViewById(R.id.tv_today).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.calendarView.scrollToCurrent();
            }
        });
        mCalendarLayout = (CalendarLayout) findViewById(R.id.calendarLayout);
        binding.calendarView.setOnDateSelectedListener(this);
        binding.calendarView.setOnYearChangeListener(this);
        binding.calendarView.setOnMonthChangeListener(this);
        binding.tvYear.setText(String.valueOf(binding.calendarView.getCurYear()));
        mYear = binding.calendarView.getCurYear();


        findViewById(R.id.activity_img_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        binding.monthUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.calendarView.scrollToPre();
            }
        });

        binding.monthNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.calendarView.scrollToNext();
            }
        });
    }

    @Override
    protected void initData() {
        setStatusBar();
    }

    public void setStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.status_bar));
    }

    public void initAdapter() {
        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(ColorfulActivity.this));
        binding.recyclerView.addItemDecoration(new GroupItemDecoration<String, Article>());
        articleAdapter = new ArticleAdapter(ColorfulActivity.this, dataList, handler);
        binding.recyclerView.setAdapter(articleAdapter);
    }

    public void requestCourseData(Calendar calendar, int type) {
        Map<String, Object> map = new HashMap<>();
        RequestDataEntity requestDataEntity = new RequestDataEntity();
        requestDataEntity.setUrl(HttpEntity.MAIN_URL + HttpEntity.GET_USER_COURSE_LIST);
        requestDataEntity.setToken(spUtils.getString(Entity.TOKEN));
        int dayCount = binding.calendarView.getMonthCount(binding.calendarView.getCurYear(), binding.calendarView.getCurMonth());
        switch (type){
            case 0:
                break;
            case 1:
                int month = calendar.getMonth();
                int year = calendar.getYear();
                int day = calendar.getDay();
                map.put("startDate", year + N + month + N + day);
                month += 1;
                if (month > 12){
                    year+=1;
                    month = 1;
                }
                map.put("endDate", year + N + month + N + day);
                monthDataList = new ArrayList<>();
                httpUtils.getCouseAll(handler, requestDataEntity, map, monthDataList, type);
                break;
            case 2:
                int days = calendar.getDay();
                int months = calendar.getMonth();
                int years = calendar.getYear();
                map.put("startDate", years + N + months + N + days);
                days += 1;
                if (days > dayCount){
                    months += 1;
                    days = 1;
                    if (months > 12){
                        years += 1;
                        months = 1;
                    }
                }
                map.put("endDate", years + N + months + N + days);
                requestDataList = new ArrayList<>();
                httpUtils.getCouseAll(handler, requestDataEntity, map, requestDataList, type);
                break;
        }
    }



    public Handler handler = new Handler(Looper.getMainLooper()) {
        @SuppressLint("SetTextI18n")
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    break;
                case 1:
                    int monthSize = monthDataList.size();
                    List<Calendar> schemes = new ArrayList<>();
                    if (monthSize > 0){
                        for (int i = 0; i < monthSize; i++){
                            CourseEntity entity = monthDataList.get(i);
                            String[] splitDate = entity.getDate().split(N);
                            String now = binding.calendarView.getCurYear() + N + binding.calendarView.getCurMonth() + N + binding.calendarView.getCurDay();
                            int result = now.compareTo(entity.getDate());
                            if (result <= 0){
                                schemes.add(getSchemeCalendar(Integer.parseInt(splitDate[0]),
                                        Integer.parseInt(splitDate[1]), Integer.parseInt(splitDate[2]), Color.parseColor("#3aa6fe"), "假"));
                            }else{
                                schemes.add(getSchemeCalendar(Integer.parseInt(splitDate[0]),
                                        Integer.parseInt(splitDate[1]), Integer.parseInt(splitDate[2]), Color.parseColor("#999999"), "假"));
                            }
                        }
                        binding.calendarView.setSchemeDate(schemes);
                    }
                    break;
                case 2:
                    if (requestDataList.size() == 0) {
                        CourseEntity entity = new CourseEntity();
                        entity.setType(1);
                        entity.setCourseMessage("今日没有课程");
                        requestDataList.add(entity);
                    }
                    dataList.clear();
                    dataList.addAll(requestDataList);
                    if (articleAdapter != null) {
                        articleAdapter = new ArticleAdapter(ColorfulActivity.this, dataList, handler);
                        binding.recyclerView.setAdapter(articleAdapter);
                    }else{
                        initAdapter();
                    }
                    binding.recyclerView.notifyDataSetChanged();
                    break;

            }
        }
    };

    /**
     * 需要标记的日期 添加下标点
     */
    public void setSchemes(){
        List<Calendar> schemes = new ArrayList<>();
        int year = binding.calendarView.getCurYear();
        int month = binding.calendarView.getCurMonth();
        schemes.add(getSchemeCalendar(year, month, 3, Color.parseColor("#3aa6fe"), "假"));
        schemes.add(getSchemeCalendar(year, month, 8, Color.parseColor("#999999"), ""));
        binding.calendarView.setSchemeDate(schemes);
    }


    public void setListData() {
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
        requestDataList.add(entity);
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
        binding.tvYear.setVisibility(View.VISIBLE);
        binding.tvYear.setText(String.valueOf(calendar.getYear()) + "年" + calendar.getMonth() + "月");
        mYear = calendar.getYear();
        long curTime = System.currentTimeMillis();
        if ((curTime - lastClickTime) > MIN_CLICK_TIME){
            lastClickTime = curTime;
            requestCourseData(calendar, 2);
        }
    }


    @Override
    public void onYearChange(int year) {
        //mTextMonthDay.setText(String.valueOf(year));
    }

    @Override
    public void onMonthChange(int year, int month) {
        Calendar calendar = new Calendar();
        calendar.setYear(year);
        calendar.setMonth(month);
        calendar.setDay(1);
        requestCourseData(calendar, 1);
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
                String result = time.split(" ")[0];
                String[] part = result.split("-");
                binding.calendarView.scrollToCalendar(Integer.parseInt(part[0]), Integer.parseInt(part[1]), Integer.parseInt(part[2]));
            }
        }, "1970-01-01 00:00", (binding.calendarView.getCurYear() + 10) + "-12-31 00:00"); // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
        customDatePicker.showSpecificTime(false); // 不显示时和分
        customDatePicker.setIsLoop(true); // 不允许循环滚动

    }


}
