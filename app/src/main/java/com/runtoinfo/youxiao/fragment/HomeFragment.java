package com.runtoinfo.youxiao.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.google.gson.Gson;
import com.runtoinfo.httpUtils.HttpEntity;
import com.runtoinfo.httpUtils.bean.GetSchoolSettingEntity;
import com.runtoinfo.httpUtils.bean.HomeCourseEntity;
import com.runtoinfo.httpUtils.bean.RequestDataEntity;
import com.runtoinfo.httpUtils.bean.SchoolDynamicsNewEntity;
import com.runtoinfo.httpUtils.utils.HttpUtils;
import com.runtoinfo.youxiao.R;
import com.runtoinfo.youxiao.adapter.CoursePunchAdapter;
import com.runtoinfo.youxiao.globalTools.adapter.UniversalRecyclerAdapter;
import com.runtoinfo.youxiao.globalTools.utils.DensityUtil;
import com.runtoinfo.youxiao.globalTools.utils.DialogMessage;
import com.runtoinfo.youxiao.globalTools.utils.Entity;
import com.runtoinfo.youxiao.databinding.FragmentHomeBinding;
import com.runtoinfo.youxiao.entity.SelectSchoolEntity;
import com.runtoinfo.youxiao.globalTools.utils.RecyclerViewDecoration;
import com.runtoinfo.youxiao.globalTools.utils.TimeUtil;
import com.runtoinfo.youxiao.ui.FloatDragView;
import com.runtoinfo.youxiao.ui.MyScrollView;
import com.runtoinfo.youxiao.ui.PopupWindowFragment;
import com.runtoinfo.youxiao.globalTools.utils.IntentDataType;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by qiaojunchao on 2018/5/24 0024.
 */

@SuppressLint("ValidFragment")
@SuppressWarnings("all")
@Route(path = "/main/homeFragment")
public class HomeFragment extends BaseFragment implements MyScrollView.ScrollViewListener {

    public FragmentHomeBinding binding;
    public CoursePunchAdapter coursePunchAdapter;
    public List<HomeCourseEntity> getCourseList = new ArrayList<>();
    public PopupWindowFragment popupWindow;
    public int mHeight = 0;
    public List<SelectSchoolEntity> schoolSelectList;
    public TextView tSignUp;
    public String phoneNumber = "15606344426";
    public final int[] startLocation = new int[2];
    public boolean isGetData = false;
    public boolean mHasLoadedOnce = false;
    public boolean isPrepared = false;
    public HttpUtils httpUtils;
    public List<SchoolDynamicsNewEntity> dataList;
    public Context context;

    public HomeFragment(List<SelectSchoolEntity> schoolSelectList) {
        this.schoolSelectList = schoolSelectList;
    }
    public HomeFragment(){super();}

    public static HomeFragment getInstance(List<SelectSchoolEntity> schoolSelectList){
        Bundle bundle = new Bundle();
        String gson = new Gson().toJson(schoolSelectList);
        bundle.putString(IntentDataType.DATA, gson);
        HomeFragment homeFragment = new HomeFragment(schoolSelectList);
        homeFragment.setArguments(bundle);
        return homeFragment;
    }

    public final static IntentFilter intentFilter = new IntentFilter();
    static {
        intentFilter.addAction(IntentDataType.DATA);
    }

    public BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(IntentDataType.DATA)) {
                binding.homeEmailImg.setImageResource(R.drawable.home_emals_off);
            }
        }
    };
    public View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            tSignUp = (TextView) v;
            tSignUp.setText("已签");
            tSignUp.setBackgroundResource(R.drawable.home_sign_finish);
            tSignUp.setEnabled(false);
        }
    };

    public Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    initCourseData();
                    /**
                     * 暂时使用
                     */
                    for (int i = 0; i < getCourseList.size(); i++) {
                        HomeCourseEntity entity = getCourseList.get(i);
                        spUtils.setInt(com.runtoinfo.youxiao.globalTools.utils.Entity.COURSE_ID, entity.getCourseId());
                        spUtils.setInt(com.runtoinfo.youxiao.globalTools.utils.Entity.COURSE_INST_ID, entity.getCourseInstId());
                    }
                    break;
                case 5:

                    if (dataList.size() > 1) {
                        SchoolDynamicsNewEntity item1 = dataList.get(0);
                        binding.homeSystemContent.setText(item1.getTitle());
                        if (TimeUtil.getTimeDif(item1.getPublishTime()) != null) {
                            binding.homeSystemContentTime.setText(TimeUtil.getTimeDif(item1.getPublishTime()));
                        }
                        SchoolDynamicsNewEntity item2 = dataList.get(1);
                        binding.homeActivityContent.setText(item2.getTitle());
                        if (TimeUtil.getTimeDif(item2.getPublishTime()) != null) {
                            binding.homeActivityContentTime.setText(TimeUtil.getTimeDif(item2.getPublishTime()));
                        }
                    }

                    break;
                case 10://获取校长电话
                    GetSchoolSettingEntity entity = (GetSchoolSettingEntity) msg.obj;
                    phoneNumber = entity.getContactPhoneNumber();
                    initFloatWindowListener();
                    break;
                case 20://切换学校后
                    try {
                        JSONObject result = (JSONObject) msg.obj;
                        spUtils.setString(Entity.TOKEN, result.getString("accessToken"));
                        spUtils.setInt(Entity.USER_ID, result.getInt("userId"));
                        refresh(HomeFragment.this);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 30:
                    int count = (int) msg.obj;
                    if (count > 0) {
                        binding.homeEmailImg.setImageResource(R.drawable.home_emals_off);
                    } else {
                        binding.homeEmailImg.setImageResource(R.drawable.home_emals_on);
                    }
                    break;
                case 400:
                    //DialogMessage.showToast(getContext(), "请求数据失败");
                    break;
                case 500:
                    //DialogMessage.showToast(getContext(), "");
                    break;
                case 404:
                    //DialogMessage.showToast(getContext(), "数据解析异常");
                    break;
            }
        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        context = getContext();
        DensityUtil.setViewHeight(getActivity(), binding.fragmentHomeView);
        httpUtils = new HttpUtils(context);
        requestPermission();
        initCourseDataList();
        initFloatWindow();
        initListener();
        requestNews();
        registBroadReciver();
        lazyLoad();
        return binding.getRoot();
    }

    public void registBroadReciver() {
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(receiver, intentFilter);
    }

    @Override
    public void lazyLoad() {
        if (mHasLoadedOnce || !isPrepared)
            return;
        mHasLoadedOnce = true;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mHasLoadedOnce = false;
        isPrepared = false;
    }


    public void requestPermission() {
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


    public void initCourseData() {


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

        binding.homeRecyclerView.addItemDecoration(new RecyclerViewDecoration(getContext(), RecyclerViewDecoration.HORIZONTAL_LIST));
    }

    /**
     * 请求接口获取今日课程
     */
    public void initCourseDataList() {

        RequestDataEntity requestDataEntity = new RequestDataEntity();
        requestDataEntity.setToken(spUtils.getString(Entity.TOKEN));
        requestDataEntity.setUrl(HttpEntity.MAIN_URL + HttpEntity.GET_HOME_COURSE_DATA);

        Map<String, Object> map = new HashMap<>();
        map.put("studentId", 18);
        map.put("date", TimeUtil.getNowDate());
        if (getCourseList.size() > 0) getCourseList.clear();

        httpUtils.getCourseDataList(handler, requestDataEntity, map, getCourseList);
    }

    /**
     * 获取动态
     */
    public void requestNews() {
        RequestDataEntity requestDataEntity = new RequestDataEntity();
        requestDataEntity.setToken(spUtils.getString(Entity.TOKEN));
        Log.e("Token", spUtils.getString(Entity.TOKEN));
        requestDataEntity.setUrl(HttpEntity.MAIN_URL + HttpEntity.SCHOOL_NEWS_ALL);
        requestDataEntity.setType(0);
        dataList = new ArrayList<>();
        httpUtils.getSchoolNewsAll(handler, requestDataEntity, dataList);
    }

    /**
     * 获取校长电话
     */
    public void requestSchoolSetting() {
        RequestDataEntity requestDataEntity = new RequestDataEntity();
        requestDataEntity.setToken(spUtils.getString(Entity.TOKEN));
        requestDataEntity.setUrl(HttpEntity.MAIN_URL + HttpEntity.GET_SCHOOL_SETTING);
        httpUtils.getSchoolSetting(handler, requestDataEntity);
    }

    /**
     * 获取用户未读消息
     */
    public void requestUserInformationCount() {
        RequestDataEntity requestDataEntity = new RequestDataEntity();
        requestDataEntity.setTenantId(spUtils.getInt(Entity.TENANT_ID));
        requestDataEntity.setUrl(HttpEntity.MAIN_URL + HttpEntity.GET_NOTIFICATION_COUNT);
        requestDataEntity.setUserId(spUtils.getInt(Entity.USER_ID));
        requestDataEntity.setToken(spUtils.getString(Entity.TOKEN));
        Map<String, Object> map = new HashMap<>();
        map.put("tenantId", spUtils.getInt(Entity.TENANT_ID));
        map.put("userId", spUtils.getInt(Entity.USER_ID));
        httpUtils.getNotificationCount(handler, requestDataEntity, map);
    }

    /**
     * 悬浮按钮
     */
    public void initFloatWindow() {
        ViewTreeObserver vto = binding.homeHeadImage.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                binding.homeHeadImage.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                startLocation[0] = binding.homeHeadImage.getWidth();
                startLocation[1] = binding.homeHeadImage.getHeight();
                Log.e("HomeFragment", "width:" + startLocation[0] + "; height:" + startLocation[1]);
            }
        });
        requestSchoolSetting();

    }

    public void initFloatWindowListener() {
        //悬浮按钮
        ImageView imageView = FloatDragView.addFloatDragView(getActivity(), binding.homeFrameLayout, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (phoneNumber != null) {
                    final Dialog dialog = DialogMessage.showDialogWithLayout(getContext(), R.layout.show_school_phone_number_layout);
                    dialog.show();
                    dialog.findViewById(R.id.call_number_cancel).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    ((TextView) dialog.findViewById(R.id.school_phoneNumber)).setText(phoneNumber);

                    dialog.findViewById(R.id.call_number).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //调起电话
                            Intent intent = new Intent(Intent.ACTION_CALL);
                            Uri data = Uri.parse("tel:" + phoneNumber);
                            intent.setData(data);
                            startActivity(intent);
                        }
                    });
                }
            }
        }, startLocation);
    }


    public void initListener() {

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

                popupWindow = new PopupWindowFragment(getContext(), getActivity(), new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        SelectSchoolEntity entity = schoolSelectList.get(position);
                        RequestDataEntity requestDataEntity = new RequestDataEntity();
                        requestDataEntity.setToken(spUtils.getString(Entity.TOKEN));
                        requestDataEntity.setUrl(HttpEntity.MAIN_URL + HttpEntity.SWITCH_CAMPUS);
                        requestDataEntity.setTenantId(Integer.parseInt(entity.getTenantId()));
                        requestDataEntity.setCampusId(entity.getId());
                        httpUtils.switchCampusId(handler, requestDataEntity);

                        popupWindow.popupWindow.dismiss();
                    }
                });
                popupWindow.showPopupWindows(schoolSelectList, binding.fragmentHomeImagview);
                WindowManager.LayoutParams params = getActivity().getWindow().getAttributes();
                params.alpha = 0.7f;
                getActivity().getWindow().setAttributes(params);
                return true;
            }
        });
        binding.homeEmailImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.homeEmailImg.setImageResource(R.drawable.home_emals_on);
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
                ARouter.getInstance().build("/event/eventActivity").withInt(Entity.USER_ID, spUtils.getInt(Entity.USER_ID)).navigation();
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
        binding.homeActivityContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build("/main/schoolDynamics")
                        .withString(IntentDataType.INTENT_KEY, IntentDataType.SCHOOL_DYNAMICS).navigation();
            }
        });
        binding.homeSystemContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build("/main/schoolDynamics")
                        .withString(IntentDataType.INTENT_KEY, IntentDataType.SCHOOL_DYNAMICS).navigation();
            }
        });
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        //进入当前Fragment
        if (enter && !isGetData) {
            isGetData = true;
            //这里可以做网络请求或者需要的数据刷新操作
            //refresh(HomeFragment.this);
        } else {
            isGetData = false;
        }
        return super.onCreateAnimation(transit, enter, nextAnim);
    }

    @Override
    public void onPause() {
        super.onPause();
        isGetData = false;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onScrollChanged(MyScrollView scrollView, int x, int y, int oldx, int oldy) {
        if (y <= 0) {  //设置标题的背景颜色
            binding.homeTitleRelative.setBackgroundColor(Color.argb((int) 0, 144, 151, 166));
            binding.fragmentHomeView.setBackgroundColor(Color.argb((int) 0, 144, 151, 166));
        } else if (y > 0 && y <= mHeight) { //滑动距离小于banner图的高度时，设置背景和字体颜色颜色透明度渐变
            float scale = (float) y / mHeight;
            float alpha = (255 * scale);
            //binding.homeTitleRelative.setTextColor(Color.argb((int) alpha, 255,255,255));
            binding.homeTitleRelative.setBackgroundColor(Color.argb((int) alpha, 139, 185, 247));
            binding.fragmentHomeView.setBackgroundColor(Color.argb((int) alpha, 139, 185, 247));
        } else {  //滑动到banner下面设置普通颜色
            binding.homeTitleRelative.setBackgroundColor(Color.argb((int) 255, 82, 151, 248));
            binding.fragmentHomeView.setBackgroundColor(Color.argb((int) 255, 82, 151, 248));
        }
    }
}
