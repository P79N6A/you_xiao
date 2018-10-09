package com.runtoinfo.event.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.runtoinfo.event.R;
import com.runtoinfo.event.databinding.ActivityEventDetailsBinding;
import com.runtoinfo.httpUtils.HttpEntity;
import com.runtoinfo.httpUtils.bean.MyEventEntity;
import com.runtoinfo.httpUtils.bean.RequestDataEntity;
import com.runtoinfo.httpUtils.utils.HttpUtils;
import com.runtoinfo.youxiao.globalTools.utils.DialogMessage;
import com.runtoinfo.youxiao.globalTools.utils.Entity;
import com.runtoinfo.youxiao.globalTools.utils.IntentDataType;
import com.runtoinfo.youxiao.globalTools.utils.TimeUtil;

@Route(path = "/event/eventDetails")
public class ActivitiesEventDetails extends EventBaseActivity {

    public ActivityEventDetailsBinding binding;
    public int type;
    public Dialog cancelDialog;
    public MyEventEntity eventEntity;
    public HttpUtils httpUtils;
    public int position;

    @Override
    protected void initView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_event_details);
        httpUtils = new HttpUtils(this);
    }

    @SuppressLint("SetTextI18n")
    public void initData(){
        String json = getIntent().getExtras().getString(IntentDataType.DATA);
        type = getIntent().getIntExtra(IntentDataType.TYPE, 0);
        eventEntity = new Gson().fromJson(json, new TypeToken<MyEventEntity>(){}.getType());
        position = getIntent().getExtras().getInt(IntentDataType.POSITION);
        binding.activityEventAddress.setText("地点: " + eventEntity.getLocation());
        binding.activityEventDescription.setText(eventEntity.getIntroduction());
        httpUtils.postPhoto(this, eventEntity.getCover(), binding.activityEventImg);
        binding.activityEventTime.setText("时间: " + eventEntity.getStartDate());
        binding.activityEventName.setText(eventEntity.getName());
        if (type == 1){
            binding.eventParticipant.setVisibility(View.VISIBLE);
            binding.eventParticipant.setText("随行人员(" + eventEntity.getParticipantNumber() + "):" + eventEntity.getPrincipal());
            binding.activitySignUpNow.setText("取消报名");
            binding.activitySignUpNow.setTextColor(Color.parseColor("#999999"));
            binding.activitySignUpNow.setBackgroundResource(R.drawable.background_button_cancel);
        }else{
            binding.eventParticipant.setVisibility(View.GONE);
        }
    }

    public void initEvent(){
        binding.activitySignUpNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type == 1){
                    cancelDialog = DialogMessage.showDialogWithLayout(ActivitiesEventDetails.this, R.layout.activity_cancel_layout);
                    cancelDialog.show();
                    cancelDialog.findViewById(R.id.cancel_sign).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //等待, 取消报名监听
                            RequestDataEntity requestDataEntity = new RequestDataEntity();
                            requestDataEntity.setUrl(HttpEntity.MAIN_URL + HttpEntity.DELETE_MODIFY_MEMBER);
                            requestDataEntity.setToken(spUtils.getString(Entity.TOKEN));
                            requestDataEntity.setSignId(eventEntity.getSignInId());
                            httpUtils.deleteMember(handler, requestDataEntity);
                        }
                    });

                    cancelDialog.findViewById(R.id.think_again).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            cancelDialog.cancel();
                        }
                    });
                }else {
                    ARouter.getInstance().build("/event/activitySignUp").withInt(IntentDataType.DATA, eventEntity.getId()).navigation();
                }
            }
        });
        binding.activityImgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public Handler handler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 200){
                if (cancelDialog != null && cancelDialog.isShowing()){
                    cancelDialog.dismiss();
                    Intent intent =new Intent(ActivitiesEventDetails.this, MineEventActivity.class);
                    intent.putExtra(IntentDataType.POSITION, position);
                    setResult(2, intent);
                    ActivitiesEventDetails.this.finish();
                }
            }
        }
    };
}
