package com.runtoinfo.youxiao.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.runtoinfo.teacher.HttpEntity;
import com.runtoinfo.teacher.bean.HomeCourseEntity;
import com.runtoinfo.teacher.utils.HttpUtils;
import com.runtoinfo.youxiao.R;
import com.runtoinfo.youxiao.adapter.CoursePunchAdapter;
import com.runtoinfo.youxiao.globalTools.adapter.UniversalRecyclerAdapter;
import com.runtoinfo.youxiao.globalTools.utils.DialogMessage;
import com.runtoinfo.youxiao.globalTools.utils.Entity;
import com.runtoinfo.youxiao.databinding.FragmentHomeBinding;
import com.runtoinfo.youxiao.entity.SelectSchoolEntity;
import com.runtoinfo.youxiao.ui.FloatDragView;
import com.runtoinfo.youxiao.ui.MyScrollView;
import com.runtoinfo.youxiao.ui.PopupWindowFragment;
import com.runtoinfo.youxiao.utils.IntentDataType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by qiaojunchao on 2018/5/24 0024.
 */

@SuppressLint("ValidFragment")
public class HomeFragment extends BaseFragment implements MyScrollView.ScrollViewListener{

    public FragmentHomeBinding binding;
    public CoursePunchAdapter coursePunchAdapter;
    public List<HomeCourseEntity> getCourseList = new ArrayList<>();
    public PopupWindowFragment popupWindow;
    public int mHeight = 0;
    public List<SelectSchoolEntity> schoolSelectList;
    public TextView tSignUp;

    public HomeFragment(List<SelectSchoolEntity> schoolSelectList){
        this.schoolSelectList = schoolSelectList;
    }

    public View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            tSignUp = (TextView) v;
            tSignUp.setText("已签");
            tSignUp.setBackgroundResource(R.drawable.home_sign_finish);
            tSignUp.setEnabled(false);
        }
    };

    public Handler handler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    initCourseData();
                    /**
                     * 暂时使用
                     */
                    for(int i = 0; i < getCourseList.size(); i++){
                        HomeCourseEntity entity = getCourseList.get(i);
                        spUtils.setInt(com.runtoinfo.youxiao.globalTools.utils.Entity.COURSE_ID, entity.getCourseId());
                        spUtils.setInt(com.runtoinfo.youxiao.globalTools.utils.Entity.COURSE_INST_ID,entity.getCourseInstId());
                    }
                    break;
                case 404:
                    DialogMessage.showToast(getContext(), "请求数据失败");
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        requestPermission();
        initCourseDataList();
        initFloatWindow();
        initListener();
        return binding.getRoot();
    }


    public void requestPermission(){
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            //如果应用之前请求过此权限但用户拒绝了请求，此方法将返回 true。
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.CALL_PHONE)) {
                //这里可以写个对话框之类的项向用户解释为什么要申请权限，并在对话框的确认键后续再次申请权限
            } else {
                //申请权限，字符串数组内是一个或多个要申请的权限，1是申请权限结果的返回参数，在onRequestPermissionsResult可以得知申请结果
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE,}, 1);
            }
        }
    }


    public void initCourseData(){

        binding.homeRecyclerView.setHasFixedSize(true);
        binding.homeRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        coursePunchAdapter = new CoursePunchAdapter(getActivity(), getCourseList, R.layout.fragment_home_recyclerview_item);
        binding.homeRecyclerView.setAdapter(coursePunchAdapter);

        binding.homeRecyclerView.setNestedScrollingEnabled(false);

        coursePunchAdapter.setOnItemClickListener(new UniversalRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                ARouter.getInstance().build("/cources/colorfulActivity").navigation();
            }
        });
    }

    /**
     * 请求接口获取今日课程
     */
    public void initCourseDataList(){
        Map<String, Object> map = new HashMap<>();
        map.put("url", HttpEntity.MAIN_URL + HttpEntity.GET_HOME_COURSE_DATA);
        map.put("token", spUtils.getString(com.runtoinfo.youxiao.globalTools.utils.Entity.TOKEN));
        HttpUtils.getCourseDataList(handler, map, getCourseList);
    }

    /**
     * 悬浮按钮
     */
    public void initFloatWindow(){
        final int[] startLocation = new int[2];
        ViewTreeObserver vto = binding.homeHeadImage.getViewTreeObserver();
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                startLocation[1] = binding.homeHeadImage.getMeasuredHeight();
                startLocation[0] = binding.homeHeadImage.getMeasuredWidth();
                return true;
            }
        });

        //悬浮按钮
        FloatDragView.addFloatDragView(getActivity(), binding.homeFrameLayout, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //调起电话
                Intent intent = new Intent(Intent.ACTION_CALL);
                Uri data = Uri.parse("tel:" + 10010);
                intent.setData(data);
                startActivity(intent);
            }
        }, startLocation);
    }
    public void initListener(){

        /**
         * 选择学校
         * 切换学校
         */
        binding.fragmentHomeImagview.setOnLongClickListener(new View.OnLongClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public boolean onLongClick(View v) {
                //实现手机振动
                Vibrator vibrator = (Vibrator) getActivity().getSystemService(getActivity().VIBRATOR_SERVICE);
                vibrator.vibrate(100);

                List<SelectSchoolEntity> list = new ArrayList<>();
                popupWindow = new PopupWindowFragment(getContext(), getActivity());
                popupWindow.showPopupWindows(schoolSelectList, binding.fragmentHomeImagview);
                WindowManager.LayoutParams params=getActivity().getWindow().getAttributes();
                params.alpha=0.7f;
                getActivity().getWindow().setAttributes(params);
                return true;
            }
        });
        binding.homeEmailImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build("/information/informationActivity").navigation();
            }
        });

        binding.homeCourseListLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build("/cources/colorfulActivity").navigation();
            }
        });

        binding.homeActivityListLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build("/event/eventActivity").withString(Entity.USER_ID, spUtils.getString(Entity.USER_ID)).navigation();
            }
        });

        binding.homeSchoolMovingStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build("/main/schoolDynamics")
                        .withString(IntentDataType.INTENT_KEY, IntentDataType.SCHOOL_DYNAMICS).navigation();
            }
        });

        binding.homeHeadNewsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build("/main/schoolDynamics")
                        .withString(IntentDataType.INTENT_KEY, IntentDataType.HEAD_NEWS).navigation();
            }
        });

        //binding.homeMyScroll.setOnScrollChangeListener(this);

        ViewTreeObserver vto = binding.homeHeadImage.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mHeight = binding.homeHeadImage.getHeight();
                binding.homeMyScroll.setScrollViewListener(HomeFragment.this);
            }
        });
    }



    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onScrollChanged(MyScrollView scrollView, int x, int y, int oldx, int oldy) {
        if (y <= 0) {  //设置标题的背景颜色
            binding.homeTitleRelative.setBackgroundColor(Color.argb((int) 0, 144,151,166));
        } else if (y > 0 && y <= mHeight) { //滑动距离小于banner图的高度时，设置背景和字体颜色颜色透明度渐变
            float scale = (float) y / mHeight;
            float alpha = (255 * scale);
            //binding.homeTitleRelative.setTextColor(Color.argb((int) alpha, 255,255,255));
            binding.homeTitleRelative.setBackgroundColor(Color.argb((int) alpha, 139,185,247));
        } else {  //滑动到banner下面设置普通颜色
            binding.homeTitleRelative.setBackgroundColor(Color.argb((int) 255, 82,151,248));
        }

    }
}
