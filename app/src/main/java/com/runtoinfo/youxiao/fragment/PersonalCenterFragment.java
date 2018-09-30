package com.runtoinfo.youxiao.fragment;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.SimpleAdapter;

import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.sdk.android.feedback.impl.FeedbackAPI;
import com.google.gson.Gson;
import com.runtoinfo.httpUtils.HttpEntity;
import com.runtoinfo.httpUtils.bean.PersonalCenterEntity;
import com.runtoinfo.httpUtils.bean.RequestDataEntity;
import com.runtoinfo.httpUtils.utils.HttpUtils;
import com.runtoinfo.youxiao.R;
import com.runtoinfo.youxiao.activities.LoginActivity;
import com.runtoinfo.youxiao.activities.MainActivity;
import com.runtoinfo.youxiao.databinding.FragmentPersonalCenterBinding;
import com.runtoinfo.youxiao.globalTools.utils.Entity;
import com.runtoinfo.youxiao.globalTools.utils.IntentDataType;
import com.runtoinfo.youxiao.ui.MyGridView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/5/24 0024.
 */

public class PersonalCenterFragment extends BaseFragment {

    public FragmentPersonalCenterBinding binding;
    public SimpleAdapter adapter;
    public Map<String, Object> map = new HashMap<>();
    public List<Map<String, Object>> dataList = new ArrayList<>();
    public boolean isGetData;
    private boolean mHasLoadedOnce = false;
    private boolean isPrepared = false;
    public List<PersonalCenterEntity> personal = new ArrayList<>();
    public HttpUtils httpUtils;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_personal_center, container, false);
        httpUtils = new HttpUtils(getContext());
        initData();
        getPersonalInfo();
        lazyLoad();
        return binding.getRoot();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void lazyLoad() {
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

    public void initData(){
        dataList.clear();
        String[] from = {"img", "text"};
        int[] to = {R.id.grid_img_view, R.id.grid_text};
        int[] img = {/*R.drawable.personal_img_order, R.drawable.personal_shopping_car, R.drawable.personal_img_activity,*/
                R.drawable.personal_img_course_nots, R.drawable.personal_img_leave_nots, R.drawable.personal_study_line, R.drawable.personal_img_collection,
        R.drawable.personal_img_about_us, R.drawable.personal_img_return};
        String[] text = {/*"我的订单","购物车","我的活动",*/"上课记录","请假记录","学习轨迹", "我的收藏", "关于我们","意见反馈"};
        for (int i = 0; i < img.length; i++)
        {
            Map<String, Object> map = new HashMap<>();
            map.put("img", img[i]);
            map.put("text", text[i]);
            dataList.add(map);
        }
        adapter = new SimpleAdapter(getContext(), dataList, R.layout.fragment_personal_gridview_item, from, to);
        //myGridView = new MyGridView(getActivity());
        binding.personalGridview.setAdapter(adapter);

        binding.personalGridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e("GridView", adapter.getItem(position).toString());
                String json = new Gson().toJson(adapter.getItem(position));
                try {
                    JSONObject object = new JSONObject(json);
                    String itemObj = object.getString("text").replaceAll("\\s*", "");
                    String dataType = null;
                    switch (position)
                    {
                        /*case 0://我的订单
                            break;
                        case 1://购物车
                            break;
                        case 2://我的活动
                            break;*/
                        case 0://上课记录
                            dataType = "courseRecord";
                            ARouter.getInstance().build("/center/mySomeRecord").withString(IntentDataType.DATA, dataType).navigation();
                            break;
                        case 1://"请假记录":
                            dataType = "leaveRecord";
                            ARouter.getInstance().build("/center/mySomeRecord").withString(IntentDataType.DATA, dataType).navigation();
                            break;
                        case 2://学习轨迹
                            dataType = "learnTrack";
                            ARouter.getInstance().build("/center/mySomeRecord").withString(IntentDataType.DATA, dataType).navigation();
                            break;
                        case 3://我的收藏
                            dataType = "collection";
                            ARouter.getInstance().build("/center/mySomeRecord").withString(IntentDataType.DATA, dataType).navigation();
                            break;
                        case 4://"关于我们":
                            ARouter.getInstance().build("/personal/aboutUs").navigation();
                            break;
                        case 5://"意见反馈":
                            //ARouter.getInstance().build("/personal/feedback").navigation();
                            FeedbackAPI.openFeedbackActivity();
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        binding.personalSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build("/personal/personalSettings").navigation();
            }
        });

        binding.personalMyCourses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build("/cources/colorfulActivity").navigation();
            }
        });
        binding.personalMyActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build("/event/mineEventActivity").navigation();
            }
        });
        binding.personalMyInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build("/information/informationActivity").navigation();
            }
        });
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        //进入当前Fragment
        if (enter && !isGetData) {
            isGetData = true;
            //这里可以做网络请求或者需要的数据刷新操作
            //refresh(PersonalCenterFragment.this);
        } else {
            isGetData = false;
        }
        return super.onCreateAnimation(transit, enter, nextAnim);
    }

    public void getPersonalInfo(){
        RequestDataEntity requestDataEntity = new RequestDataEntity();
        requestDataEntity.setToken(spUtils.getString(Entity.TOKEN));
        requestDataEntity.setUserId(spUtils.getInt(Entity.USER_ID));
        requestDataEntity.setUrl(HttpEntity.MAIN_URL + HttpEntity.GET_PERSONAL_INFO);
        httpUtils.postPersonlInfo(handler, requestDataEntity, personal);
    }

    public Handler handler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    if (personal.size() > 0){
                        PersonalCenterEntity entity = personal.get(0);
                        binding.personalCenterName.setText(entity.getName());
                        httpUtils.postSrcPhoto(getActivity(), HttpEntity.IMAGE_HEAD + entity.getAvatar(), binding.personalAvatar);
                        setSpData(entity);
                    }
                    break;
            }
        }
    };

    public void setSpData(PersonalCenterEntity entity){
        spUtils.setString(Entity.NAME, entity.getName());
        spUtils.setString(Entity.AGE, entity.getAge());
        spUtils.setInt(Entity.GENDER, entity.getGender());
        spUtils.setString(Entity.BIRTHDAY, entity.getBirthDay());
        spUtils.setString(Entity.AVATAR, entity.getAvatar());
        spUtils.setString(Entity.ADDRESS, entity.getProvince() + entity.getCity()+ entity.getDistrict() + entity.getStreet());
    }

    @Override
    public void onPause() {
        super.onPause();
        isGetData = false;
    }
}
