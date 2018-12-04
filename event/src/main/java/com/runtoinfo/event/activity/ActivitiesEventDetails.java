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
import com.google.gson.JsonSyntaxException;
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

import java.util.HashMap;
import java.util.Map;

@Route(path = "/event/eventDetails")
public class ActivitiesEventDetails extends EventBaseActivity {

    public ActivityEventDetailsBinding binding;
    public int type;
    public Dialog cancelDialog;
    public MyEventEntity eventEntity;
    public HttpUtils httpUtils;
    public int position;
    public boolean isSignIn;
    public int signIn;
    public int eventId;//活动ID

    @Override
    protected void initView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_event_details);
        httpUtils = new HttpUtils(this);
        setStatusBar(binding.eventTitleLayout);
    }

    @SuppressLint("SetTextI18n")
    public void initData() {
        String json = getIntent().getExtras().getString(IntentDataType.DATA);
        type = getIntent().getIntExtra(IntentDataType.TYPE, 0);
        eventId = getIntent().getExtras().getInt(IntentDataType.ID);
        position = getIntent().getExtras().getInt(IntentDataType.POSITION);
        requestEventDetails();
    }

    @SuppressLint("SetTextI18n")
    public void initEventDetails(){
        binding.activityEventAddress.setText("地点: " + eventEntity.getLocation());
        binding.activityEventDescription.setText(eventEntity.getIntroduction());
        httpUtils.postPhoto(this, eventEntity.getCover(), binding.activityEventImg);
        binding.activityEventTime.setText("时间: " + eventEntity.getStartDate().concat("至").concat(eventEntity.getEndTime()));
        binding.eventParticipant.setText("已报人数：" + eventEntity.getParticipantNumber() + "人");
        binding.eventDetailsTeacher.setText("负责人：" + eventEntity.getPrincipal());
        binding.activityEventName.setText(eventEntity.getName());
        isSignIn = eventEntity.isSignIn();
        signIn = String.valueOf(eventEntity.getSignInId()).equals("null") ? 0 : eventEntity.getSignInId();

        if (isSignIn) {
            //binding.eventParticipant.setVisibility(View.VISIBLE);
            //binding.eventParticipant.setText("随行人员(" + eventEntity.getParticipantNumber() + "):" + eventEntity.getPrincipal());
            binding.activitySignUpNow.setText("取消报名");
            binding.activitySignUpNow.setTextColor(Color.parseColor("#999999"));
            binding.activitySignUpNow.setBackgroundResource(R.drawable.background_button_cancel);
        } else {
            binding.activitySignUpNow.setText("立即报名");
            binding.activitySignUpNow.setTextColor(Color.parseColor("#ffffff"));
            binding.activitySignUpNow.setBackgroundResource(R.drawable.background_button_blue);
            //binding.eventParticipant.setVisibility(View.GONE);
        }
    }

    public void requestEventDetails(){
        eventId = getIntent().getIntExtra(IntentDataType.ID, -1);
        if (eventId != -1){
            RequestDataEntity requestDataEntity = new RequestDataEntity();
            requestDataEntity.setUrl(HttpEntity.MAIN_URL + HttpEntity.GET_CAMPAIGN_DETAILS);
            requestDataEntity.setToken(spUtils.getString(Entity.TOKEN));

            Map<String, Object> map = new HashMap<>();
            map.put("Id", eventId);

            httpUtils.getEventDetails(handler, requestDataEntity, map);
        }

    }

    public void initEvent() {
        binding.activitySignUpNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSignIn) {
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
                } else {
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

    public Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    try {
                        eventEntity = new Gson().fromJson(msg.obj.toString(),
                                new TypeToken<MyEventEntity>() {}.getType());
                        initEventDetails();
                    }catch (JsonSyntaxException e){
                        e.printStackTrace();
                    } catch (IllegalStateException e){
                        e.printStackTrace();
                    }

                    break;
                case 200:
                    if (cancelDialog != null && cancelDialog.isShowing()) {
                        cancelDialog.dismiss();
                        Intent intent = new Intent(ActivitiesEventDetails.this, MineEventActivity.class);
                        intent.putExtra(IntentDataType.POSITION, position);
                        setResult(2, intent);
                        ActivitiesEventDetails.this.finish();
                    }
                    break;
            }

        }
    };
}
