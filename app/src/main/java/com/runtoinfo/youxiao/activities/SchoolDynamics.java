package com.runtoinfo.youxiao.activities;

import android.annotation.SuppressLint;
import android.databinding.DataBindingUtil;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.runtoinfo.teacher.HttpEntity;
import com.runtoinfo.teacher.utils.HttpUtils;
import com.runtoinfo.youxiao.R;
import com.runtoinfo.youxiao.adapter.SchoolDynamicsRecyclerAdapter;
import com.runtoinfo.youxiao.globalTools.utils.Entity;
import com.runtoinfo.youxiao.databinding.SchoolMovmentBinding;
import com.runtoinfo.youxiao.entity.SchoolDynamicsEntity;
import com.runtoinfo.youxiao.utils.IntentDataType;
import com.runtoinfo.youxiao.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

@Route(path = "/main/schoolDynamics")
public class SchoolDynamics extends BaseActivity {

    public SchoolMovmentBinding binding;
    public List<SchoolDynamicsEntity> schoolDynamicsList = new ArrayList<>();
    public SchoolDynamicsRecyclerAdapter adapter;
    public String dataType = null;
    public int times = 0;
    public SchoolDynamicsEntity schoolDynamicsEntity;

    @Override
    protected void initView() {
        binding = DataBindingUtil.setContentView(this, R.layout.school_movment);
        changeView();
        initEvent();
    }

    public void initEvent(){
        binding.activityImgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (times == 0) {
                    onBackPressed();
                } else {
                    hideView(false);
                    times = 0;
                }
            }
        });
    }

    public void changeView(){
        dataType = getIntent().getExtras().getString(IntentDataType.INTENT_KEY);
        if (!TextUtils.isEmpty(dataType)){
            switch (dataType){
                case IntentDataType.SCHOOL_DYNAMICS:
                    binding.schoolDynamicsActivityTitle.setText("学校动态");
                    break;
                case IntentDataType.HEAD_NEWS:
                    binding.schoolDynamicsActivityTitle.setText("新闻头条");
                    break;
                case IntentDataType.TOPICS:
                    binding.schoolDynamicsActivityTitle.setText("专题详情");
                    hideView(true);
                    break;
            }
        }
    }

    public void initData(){

    }
    public void initRecyclerData(){
        adapter = new SchoolDynamicsRecyclerAdapter(SchoolDynamics.this, schoolDynamicsList, handler);
        binding.topicsRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        binding.topicsRecyclerview.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (dataType != null && !dataType.equals(IntentDataType.TOPICS))
        HttpUtils.getSchoolNewsAll(handler, HttpEntity.MAIN_URL + HttpEntity.SCHOOL_NEWS_ALL, spUtils.getString(Entity.TOKEN));
    }

    @SuppressLint("HandlerLeak")
    public Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                case 1:
                case 2:
                    schoolDynamicsEntity = (SchoolDynamicsEntity) msg.obj;
                    times ++;
                    hideView(true);
                    binding.dynamicsTitle.setText(schoolDynamicsEntity.getTile());
                    binding.dynamicsTime .setText(schoolDynamicsEntity.getPublishTime());
                    HttpUtils.getNewsReadNumber(handler,
                            HttpEntity.MAIN_URL + HttpEntity.NEWS_READ_NUMBER,
                            spUtils.getString(Entity.TOKEN),
                            schoolDynamicsEntity.getId());
                    binding.dynamicsWebview.loadData(schoolDynamicsEntity.getContent(), "text/html; charset=UTF-8",null);
                    break;
                case 3:
                    String json = msg.obj.toString();
                    Log.e("school", json);
                    fromJson(json);
                    initRecyclerData();
                    break;
                case 4:
                    binding.dynamicsReadNumber.setText(msg.obj.toString());
                    break;
                case 500:
                    Utils.showToast(SchoolDynamics.this, "请检查您的网络");
                    break;
            }
        }
    };

    public void fromJson(String json){
        try {
            JSONObject object = new JSONObject(json);
            JSONObject result = object.getJSONObject("result");
            JSONArray items = result.getJSONArray("items");
            for (int item = 0; item < items.length(); item++){
                JSONObject childItem = items.getJSONObject(item);
                //得到动态头条标识
                //默认为0：动态，1：头条。
                int type = childItem.getInt("type");
                switch (dataType){
                    case IntentDataType.SCHOOL_DYNAMICS:
                        if (type == 0){
                            setDataList(childItem);
                        }
                        break;
                    case IntentDataType.HEAD_NEWS:
                        if (type == 1){
                            setDataList(childItem);
                        }
                        break;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setDataList(JSONObject childItem) throws JSONException{
        schoolDynamicsList = new ArrayList<>();
        JSONArray images = childItem.getJSONArray("coverImgs");
        SchoolDynamicsEntity entity = new SchoolDynamicsEntity();
        List<String> imageList = new ArrayList<>();
        for (int i = 0; i < images.length(); i++){
            imageList.add(images.getString(i));
        }
        switch (images.length()){
            case 1:
                entity.setType(0);
                break;
            case 2:
                entity.setType(1);
                break;
            case 3:
                entity.setType(2);
                break;
        }

        entity.setTile(childItem.getString("title"));
        entity.setId(childItem.getInt("id"));
        entity.setImagList(imageList);
        entity.setCoverType(childItem.getInt("coverType"));
        entity.setReadNumber(childItem.getInt("pageView"));
        entity.setStatus(childItem.getString("status"));
        entity.setVideoPath(childItem.getString("videoPath"));
        entity.setContent(childItem.getString("content"));
        entity.setMessage(childItem.getString("subtitle"));
        entity.setPublishTime(childItem.getString("publishTime"));
        schoolDynamicsList.add(entity);
    }

    public void hideView(boolean flag){
        if (flag) {
            binding.topicsRecyclerview.setVisibility(View.GONE);
            binding.dynamicsWebViewLayout.setVisibility(View.VISIBLE);
        }else{
            binding.topicsRecyclerview.setVisibility(View.VISIBLE);
            binding.dynamicsWebViewLayout.setVisibility(View.GONE);
        }

        if (dataType.equals(IntentDataType.SCHOOL_DYNAMICS)){
            binding.schoolDynamicsActivityTitle.setText(flag?"动态详情":"学校动态");
        }else if(dataType.equals(IntentDataType.HEAD_NEWS)){
            binding.schoolDynamicsActivityTitle.setText(flag?"头条详情":"新闻头条");
        }
    }

    @Override
    public void onBackPressed() {
        if (times == 0)
            super.onBackPressed();
        else{
            hideView(false);
            times = 0;
        }

    }
}
