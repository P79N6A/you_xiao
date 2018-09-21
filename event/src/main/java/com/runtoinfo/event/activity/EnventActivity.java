package com.runtoinfo.event.activity;

import android.annotation.SuppressLint;
import android.databinding.DataBindingUtil;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.runtoinfo.event.R;
import com.runtoinfo.event.adapter.EventRecyclerAdapter;
import com.runtoinfo.event.databinding.ActivityActivitiesMainBinding;
import com.runtoinfo.httpUtils.HttpEntity;
import com.runtoinfo.httpUtils.bean.MyEventEntity;
import com.runtoinfo.httpUtils.bean.RequestDataEntity;
import com.runtoinfo.httpUtils.utils.HttpUtils;
import com.runtoinfo.youxiao.globalTools.adapter.UniversalRecyclerAdapter;
import com.runtoinfo.youxiao.globalTools.utils.Entity;
import com.runtoinfo.youxiao.globalTools.utils.IntentDataType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

@Route(path = "/event/eventActivity")
public class EnventActivity extends EventBaseActivity{

    public ActivityActivitiesMainBinding binding;
    public List<MyEventEntity> dataList = new ArrayList<>();
    public EventRecyclerAdapter adapter;
    public HttpUtils httpUtils;
    public UniversalRecyclerAdapter.OnItemClickListener onItemClickListener = new UniversalRecyclerAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            MyEventEntity eventEntity = dataList.get(position);
            String json = new Gson().toJson(eventEntity);
            ARouter.getInstance().build("/event/eventDetails").withString(IntentDataType.DATA, json).withInt(IntentDataType.TYPE, 0).navigation();
        }
    };

    @SuppressLint("CommitPrefEdits")
    public void initView(){
        binding = DataBindingUtil.setContentView(this, R.layout.activity_activities_main);
        httpUtils = new HttpUtils(getBaseContext());
        binding.activityImgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        RequestDataEntity entity = new RequestDataEntity();
        entity.setUrl(HttpEntity.MAIN_URL + HttpEntity.SCHOOL_CAMPAIGN);
        entity.setToken(spUtils.getString(Entity.TOKEN));
        httpUtils.getEventAll(mHandler, entity);
    }

    @Override
    protected void initData() {
        int userId = getIntent().getExtras().getInt(Entity.USER_ID, -1);
        spUtils.setInt(Entity.USER_ID, userId);
    }

    @Override
    protected void initEvent() {

    }

    public void initRecyclerView(){
        if (dataList.size() > 0) {
            adapter = new EventRecyclerAdapter(EnventActivity.this, dataList, R.layout.mian_activity_recycler_item, 1);
            binding.activityEventRecycler.setLayoutManager(new LinearLayoutManager(this));
            binding.activityEventRecycler.setAdapter(adapter);

            adapter.setOnItemClickListener(onItemClickListener);
        }
    }

    @SuppressLint("HandlerLeak")
    public Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    try{
                        dataList.clear();
                        JSONObject json = new JSONObject(msg.obj.toString());
                        JSONObject result = json.getJSONObject("result");
                        JSONArray items = result.getJSONArray("items");
                        for (int i = 0; i < items.length(); i++){
                            JSONObject item = items.getJSONObject(i);
                            MyEventEntity eventEntity =
                                    new Gson().fromJson(item.toString(), new TypeToken<MyEventEntity>(){}.getType());
                            dataList.add(eventEntity);
                        }
                        initRecyclerView();
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };

}
