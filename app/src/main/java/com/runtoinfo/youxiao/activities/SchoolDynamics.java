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
import com.alibaba.android.arouter.launcher.ARouter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.runtoinfo.httpUtils.CPRCBean.CPRCDataEntity;
import com.runtoinfo.httpUtils.CPRCBean.CPRCTypeEntity;
import com.runtoinfo.httpUtils.HttpEntity;
import com.runtoinfo.httpUtils.bean.RequestDataEntity;
import com.runtoinfo.httpUtils.utils.HttpUtils;
import com.runtoinfo.youxiao.R;
import com.runtoinfo.youxiao.adapter.SchoolDynamicsRecyclerAdapter;
import com.runtoinfo.youxiao.globalTools.utils.DialogMessage;
import com.runtoinfo.youxiao.globalTools.utils.Entity;
import com.runtoinfo.youxiao.databinding.SchoolMovmentBinding;
import com.runtoinfo.youxiao.entity.SchoolDynamicsEntity;
import com.runtoinfo.youxiao.globalTools.utils.IntentDataType;
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
    public int type;
    public int targetType;
    public int returnType;
    public HttpUtils httpUtils;
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

        binding.detailsComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build("/comment/publishComment")
                        .withInt(IntentDataType.ARTICLE, schoolDynamicsEntity.getId())
                        .withInt(IntentDataType.TARGET_TYPE, targetType).navigation();
            }
        });
        binding.detailsCollection.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                returnType = 1;
                CPRCDataEntity dataEntity = new CPRCDataEntity();
                dataEntity.setToken(spUtils.getString(Entity.TOKEN));
                dataEntity.setType(CPRCTypeEntity.COLLECTION);
                dataEntity.setTarget(schoolDynamicsEntity.getId());
                dataEntity.setTargetType(targetType);
                dataEntity.setUserId(spUtils.getInt(Entity.USER_ID));
                httpUtils.postComment(handler, dataEntity);
            }
        });

        binding.detailsPraise.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                CPRCDataEntity dataEntity = new CPRCDataEntity();
                dataEntity.setToken(spUtils.getString(Entity.TOKEN));
                dataEntity.setType(CPRCTypeEntity.PRAISE);
                dataEntity.setTarget(schoolDynamicsEntity.getId());
                dataEntity.setTargetType(targetType);
                dataEntity.setUserId(spUtils.getInt(Entity.USER_ID));
                httpUtils.postComment(handler, dataEntity);
            }
        });
    }

    public void changeView(){
        dataType = getIntent().getExtras().getString(IntentDataType.INTENT_KEY);
        if (!TextUtils.isEmpty(dataType)){
            switch (dataType){
                case IntentDataType.SCHOOL_DYNAMICS:
                    binding.schoolDynamicsActivityTitle.setText("学校动态");
                    targetType = 0;
                    requestData(0);
                    break;
                case IntentDataType.HEAD_NEWS:
                    binding.schoolDynamicsActivityTitle.setText("新闻头条");
                    targetType = 0;
                    requestData(1);
                    break;
                case IntentDataType.TOPICS:
                    binding.schoolDynamicsActivityTitle.setText("专题详情");
                    targetType = 1;
                    String result = getIntent().getStringExtra(IntentDataType.DATA);
                    schoolDynamicsEntity = new Gson().fromJson(result, new TypeToken<SchoolDynamicsEntity>(){}.getType());
                    hideView(true);
                    break;
            }
        }
    }

    public void initData(){

    }
    public void initRecyclerData(){
        adapter = new SchoolDynamicsRecyclerAdapter(SchoolDynamics.this, schoolDynamicsList, handler);
        binding.schoolRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.schoolRecyclerView.setAdapter(adapter);
    }

    public void requestData(int type){
        RequestDataEntity dataEntity = new RequestDataEntity();
        dataEntity.setUrl(HttpEntity.MAIN_URL + HttpEntity.SCHOOL_NEWS_ALL);
        dataEntity.setType(type);
        dataEntity.setToken(spUtils.getString(Entity.TOKEN));
        httpUtils.getSchoolNewsAll(handler, dataEntity);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (dataType != null && dataType.equals(IntentDataType.TOPICS)) {
            SchoolDynamicsEntity schoolEntity =
                    new Gson().fromJson(getIntent().getExtras().getString(IntentDataType.DATA),
                            new TypeToken<SchoolDynamicsEntity>(){}.getType());
            binding.dynamicsTitle.setText(schoolEntity.getTile());
            binding.dynamicsTime.setText(schoolEntity.getPublishTime());
            binding.dynamicsReadNumber.setText(schoolEntity.getReadNumber());

            binding.dynamicsWebview.loadData(schoolEntity.getContent(), "text/html; charset=UTF-8",null);
        }
    }

    @SuppressLint("HandlerLeak")
    public Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                case 1:
                case 2:
                    if (returnType == 1){
                        binding.detailsPraiseImagView.setBackgroundResource(R.drawable.comment_praised);
                    }else {
                        schoolDynamicsEntity = (SchoolDynamicsEntity) msg.obj;
                        times++;
                        hideView(true);
                        binding.dynamicsTitle.setText(schoolDynamicsEntity.getTile());
                        binding.dynamicsTime.setText(schoolDynamicsEntity.getPublishTime());
                        httpUtils.getNewsReadNumber(handler,
                                HttpEntity.MAIN_URL + HttpEntity.NEWS_READ_NUMBER,
                                spUtils.getString(Entity.TOKEN),
                                schoolDynamicsEntity.getId());
                        binding.dynamicsWebview.loadData(schoolDynamicsEntity.getContent(), "text/html; charset=UTF-8", null);
                    }
                    break;
                case 3:
                    binding.detailsCollectionImagView.setBackgroundResource(R.drawable.boutique_course_collectioned);
                    DialogMessage.showToast(SchoolDynamics.this, "收藏成功");
                    break;

                case 5:
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
                setDataList(childItem);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setDataList(JSONObject childItem) throws JSONException{
        schoolDynamicsList = new ArrayList<>();
        JSONArray images = childItem.getJSONArray("coverImgs");
        SchoolDynamicsEntity entity = new SchoolDynamicsEntity();
        int coverType = childItem.getInt("coverType");
        List<String> imageList = new ArrayList<>();
        for (int i = 0; i < images.length(); i++){
            imageList.add(images.getString(i));
        }
        switch (coverType){
            case 0://图片
                switch (images.length()){
                    case 1:
                    case 2:
                        entity.setType(1);
                        break;
                    case 3:
                    default:
                        entity.setType(2);
                        break;
                }
                break;
            case 1://视频
                entity.setType(0);
                break;
        }

        entity.setTile(childItem.getString("title"));
        entity.setId(childItem.getInt("id"));
        entity.setImagList(imageList);
        entity.setCoverType(childItem.getInt("coverType"));
        //entity.setReadNumber(childItem.getInt("pageView"));
        entity.setStatus(childItem.getString("status"));
        entity.setVideoPath(childItem.getString("videoPath"));
        entity.setContent(childItem.getString("content"));
        entity.setMessage(childItem.getString("subtitle"));
        entity.setPublishTime(childItem.getString("publishTime"));
        schoolDynamicsList.add(entity);
    }

    public void hideView(boolean flag){
        if (flag) {
            binding.schoolRecyclerView.setVisibility(View.GONE);
            binding.dynamicsWebViewLayout.setVisibility(View.VISIBLE);
        }else{
            binding.schoolRecyclerView.setVisibility(View.VISIBLE);
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
