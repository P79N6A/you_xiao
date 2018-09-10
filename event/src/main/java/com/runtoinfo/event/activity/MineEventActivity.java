package com.runtoinfo.event.activity;

import android.databinding.DataBindingUtil;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.google.gson.Gson;
import com.runtoinfo.event.R;
import com.runtoinfo.event.adapter.EventRecyclerAdapter;
import com.runtoinfo.event.databinding.ActivityMineEventBinding;
import com.runtoinfo.httpUtils.HttpEntity;
import com.runtoinfo.httpUtils.bean.MyEventEntity;
import com.runtoinfo.httpUtils.bean.RequestDataEntity;
import com.runtoinfo.httpUtils.utils.HttpUtils;
import com.runtoinfo.youxiao.globalTools.adapter.UniversalRecyclerAdapter;
import com.runtoinfo.youxiao.globalTools.utils.Entity;
import com.runtoinfo.youxiao.globalTools.utils.IntentDataType;

import java.util.ArrayList;
import java.util.List;

@Route(path = "/event/mineEventActivity")
public class MineEventActivity extends EventBaseActivity {

    public ActivityMineEventBinding binding;
    public List<MyEventEntity> dataList = new ArrayList<>();
    public EventRecyclerAdapter mAdapter;

    @Override
    protected void initView() {
        binding = DataBindingUtil.setContentView(MineEventActivity.this, R.layout.activity_mine_event);
        requestData();
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initEvent() {
        binding.mineEventBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
    public void initRecyclerData(){
        mAdapter = new EventRecyclerAdapter(MineEventActivity.this, dataList, R.layout.mian_activity_recycler_item, 0);
        binding.minEventRecycler.setLayoutManager(new LinearLayoutManager(this));
        binding.minEventRecycler.setHasFixedSize(true);
        binding.minEventRecycler.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new UniversalRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                MyEventEntity eventEntity = mAdapter.getList().get(position);
                String json = new Gson().toJson(eventEntity);
                ARouter.getInstance().build("/event/eventDetails").withString(IntentDataType.DATA, json).withInt(IntentDataType.TYPE, 1).navigation();
            }
        });
    }

    public void requestData(){
        RequestDataEntity entity = new RequestDataEntity();
        entity.setToken(spUtils.getString(Entity.TOKEN));
        entity.setUrl(HttpEntity.MAIN_URL + HttpEntity.GET_CAMPAIGN_BY_USER);
        entity.setUserId(spUtils.getInt(Entity.USER_ID));
        HttpUtils.getMyEvent(handler, entity, dataList);
    }

    public Handler handler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    initRecyclerData();
                    break;
            }
        }
    };
}
