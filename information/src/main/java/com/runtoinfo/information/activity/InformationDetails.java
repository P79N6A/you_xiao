package com.runtoinfo.information.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.runtoinfo.httpUtils.CPRCBean.PraiseEntity;
import com.runtoinfo.httpUtils.bean.SystemMessageEntity;
import com.runtoinfo.information.R;
import com.runtoinfo.information.adapter.MyCommentAdapter;
import com.runtoinfo.information.adapter.PraiseAdapter;
import com.runtoinfo.information.adapter.SystemMessageAdapter;
import com.runtoinfo.information.databinding.ClassReminderBinding;
import com.runtoinfo.information.databinding.CommentInformationBinding;
import com.runtoinfo.information.databinding.PraiseInformationBinding;
import com.runtoinfo.information.databinding.SchoolNoticeBinding;
import com.runtoinfo.information.databinding.SystemInformationBinding;
import com.runtoinfo.httpUtils.CPRCBean.MyCommentEntity;
import com.runtoinfo.httpUtils.HttpEntity;
import com.runtoinfo.httpUtils.bean.RequestDataEntity;
import com.runtoinfo.httpUtils.utils.HttpUtils;
import com.runtoinfo.youxiao.globalTools.utils.Entity;
import com.runtoinfo.youxiao.globalTools.utils.IntentDataType;
import com.runtoinfo.youxiao.globalTools.utils.RecyclerViewDecoration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/7/31 0031.
 */
@SuppressWarnings("all")
@Route(path = "/information/informationDetails")
public class InformationDetails extends BaseActivity {

    public SystemInformationBinding systemBinding;
    public ClassReminderBinding reminderBinding;
    public SchoolNoticeBinding schoolNoticeBinding;
    public CommentInformationBinding commentBinding;
    public PraiseInformationBinding praiseBinding;

    public List<MyCommentEntity> commentEntityList = new ArrayList<>();
    public List<PraiseEntity> praiseEntityList = new ArrayList<>();
    public List<SystemMessageEntity> noticeList = new ArrayList<>();
    public SystemMessageAdapter messageAdapter;
    public HttpUtils httpUtils;
    public boolean isReadInformation = false;//标记是否进入过页面，是否更改过消息状态

    public String layoutType;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        httpUtils = new HttpUtils(getBaseContext());
        layoutType = getIntent().getExtras().getString(IntentDataType.TYPE);
        switch (layoutType) {
            case Entity.SYSTEM:
                systemBinding = DataBindingUtil.setContentView(this, R.layout.system_information);
                initSystemData();
                break;
            case Entity.LESSON_REMINDER:
                reminderBinding = DataBindingUtil.setContentView(this, R.layout.class_reminder);
                initClassReminderData();
                break;
            case Entity.CAMPUS_NOTICE:
                schoolNoticeBinding = DataBindingUtil.setContentView(this, R.layout.school_notice);
                initSchoolData();
                break;
            case "comment":
                commentBinding = DataBindingUtil.setContentView(this, R.layout.comment_information);
                setCommentRequestData();
                initCommentData();
                break;
            case "praise":
                praiseBinding = DataBindingUtil.setContentView(this, R.layout.praise_information);
                requestPraiseData();
                initPraiseData();
                break;
            default:
                systemBinding = DataBindingUtil.setContentView(this, R.layout.system_information);
                initSystemData();
                break;
        }
    }

    //初始系统消息
    public void initSystemData() {
        systemBinding.systemInformationBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        request(layoutType);
    }

    //初始上课提醒
    public void initClassReminderData() {
        reminderBinding.systemInformationBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        request(layoutType);
    }

    //初始学校通知数据
    public void initSchoolData() {
        schoolNoticeBinding.systemInformationBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        request(layoutType);
    }

    //获取用户未读消息
    public void request(String infoType) {
        RequestDataEntity entity = new RequestDataEntity();
        entity.setToken(spUtils.getString(Entity.TOKEN));
        entity.setUrl(HttpEntity.MAIN_URL + HttpEntity.GET_USER_NOTIFICATION_UNREAD);
        entity.setTenantId(spUtils.getInt(Entity.TENANT_ID));
        entity.setUserId(spUtils.getInt(Entity.USER_ID));
        entity.setMsg(infoType);
        noticeList.clear();
        httpUtils.getUserUnreadNotification(handler, entity, noticeList);
    }

    //初始赞内容
    public void initPraiseData() {
        praiseBinding.systemInformationBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        if (praiseEntityList.size() > 0) {
            praiseBinding.praiseNothing.setVisibility(View.VISIBLE);
        } else {
            praiseBinding.praiseNothing.setVisibility(View.GONE);
        }
    }

    //请求赞的数据
    public void requestPraiseData() {
        RequestDataEntity entity = new RequestDataEntity();
        entity.setUrl(HttpEntity.MAIN_URL + HttpEntity.GET_PRAISE_ME);
        entity.setUserId(spUtils.getInt(Entity.USER_ID));
        entity.setToken(spUtils.getString(Entity.TOKEN));
        httpUtils.getMyCommentOrPraise(handler, entity);
    }

    //为赞准备展现数据
    public void initPraiseRecycler() {
        PraiseAdapter praiseAdapter = new PraiseAdapter(InformationDetails.this, praiseEntityList, R.layout.info_praise_item_layout);
        praiseBinding.praiseRecycler.setLayoutManager(new LinearLayoutManager(this));
        praiseBinding.praiseRecycler.setHasFixedSize(true);
        praiseBinding.praiseRecycler.setAdapter(praiseAdapter);
        praiseBinding.praiseRecycler.addItemDecoration(new RecyclerViewDecoration(InformationDetails.this, RecyclerViewDecoration.HORIZONTAL_LIST));
    }

    public void initCommentData() {
        initCommentEvent();
        //initCommentRecycleData();
    }

    //获取评论
    public void setCommentRequestData() {
        RequestDataEntity entity = new RequestDataEntity();
        entity.setUserId(spUtils.getInt(Entity.USER_ID));
        entity.setUrl(HttpEntity.MAIN_URL + HttpEntity.GET_REPLY_ME);
        entity.setToken(spUtils.getString(Entity.TOKEN));
        httpUtils.getMyCommentOrPraise(handler, entity);
    }

    //评论、赞数据解析
    public List setListPraiseOrComment(JSONArray items) {
        try {
            List list = new ArrayList();
            for (int i = 0; i < items.length(); i++) {
                JSONObject item = items.getJSONObject(i);
                PraiseEntity praiseEntity =
                        new Gson().fromJson(item.toString(), new TypeToken<PraiseEntity>() {
                        }.getType());
                list.add(praiseEntity);
            }
            return list;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    //加载我的评论
    public void initCommentRecycleData() {
        MyCommentAdapter commentAdapter = new MyCommentAdapter(handler, InformationDetails.this, commentEntityList, R.layout.info_comment_item_layout);
        commentBinding.infoCommentRecycler.setLayoutManager(new LinearLayoutManager(this));
        commentBinding.infoCommentRecycler.setHasFixedSize(true);
        commentBinding.infoCommentRecycler.setAdapter(commentAdapter);
        commentBinding.infoCommentRecycler.addItemDecoration(new RecyclerViewDecoration(this, RecyclerViewDecoration.HORIZONTAL_LIST));
    }

    public void initCommentEvent() {
        commentBinding.systemInformationBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    //加载学校通知数据
    public void initCampusNoticeData() {
        messageAdapter = new SystemMessageAdapter(InformationDetails.this, noticeList);
        schoolNoticeBinding.schoolNoticeRecycler.setLayoutManager(new LinearLayoutManager(this));
        schoolNoticeBinding.schoolNoticeRecycler.setAdapter(messageAdapter);
    }

    //加载上课上课提醒数据
    public void initLessonReminder() {
        messageAdapter = new SystemMessageAdapter(InformationDetails.this, noticeList);
        reminderBinding.courseReminder.setLayoutManager(new LinearLayoutManager(this));
        reminderBinding.courseReminder.setAdapter(messageAdapter);
    }

    //加载系统信息
    public void initSystemInformationData() {
        messageAdapter = new SystemMessageAdapter(InformationDetails.this, noticeList);
        systemBinding.systemMessageRecycler.setLayoutManager(new LinearLayoutManager(this));
        systemBinding.systemMessageRecycler.setAdapter(messageAdapter);
    }

    //将消息状态更改为已读
    public void setNoticeReaded(){
        RequestDataEntity dataEntity = new RequestDataEntity();
        dataEntity.setUrl(HttpEntity.MAIN_URL + HttpEntity.UPDATE_USER_NOTIFICATION_STATUE_ALL);
        dataEntity.setToken(spUtils.getString(Entity.TOKEN));
        dataEntity.setTenantId(spUtils.getInt(Entity.TENANT_ID));

        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("tenantId", spUtils.getInt(Entity.TENANT_ID));
        List<String> idList = new ArrayList<>();

        for (int i = 0; i < noticeList.size(); i++){
            SystemMessageEntity messageEntity = noticeList.get(i);
            idList.add(messageEntity.getId());
        }

        dataMap.put("userNotificationIds", idList);
        httpUtils.setNoticeReader(handler, dataEntity, dataMap);
    }

    public Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    switch (layoutType) {
                        case Entity.SYSTEM:
                            initSystemInformationData();
                            setNoticeReaded();
                            break;
                        case Entity.LESSON_REMINDER:
                            initLessonReminder();
                            setNoticeReaded();
                            break;
                        case Entity.CAMPUS_NOTICE:
                            initCampusNoticeData();
                            setNoticeReaded();
                            break;
                    }
                    break;
                case 30:
                    switch (layoutType) {
                        case "comment":
                            JSONArray items = (JSONArray) msg.obj;
                            commentEntityList = setListPraiseOrComment(items);
                            initCommentRecycleData();
                            break;
                        case "praise":
                            JSONArray result = (JSONArray) msg.obj;
                            praiseEntityList = setListPraiseOrComment(result);
                            initPraiseRecycler();
                            break;
                    }
                    break;
                case 200:
                    isReadInformation = true;
                    Log.e("result", "状态更改成功");
                    break;
                case 400:
                    break;
                case 404:
                    break;
                case 500:
                    break;

            }
        }
    };

    @Override
    public void onBackPressed() {
        if (isReadInformation){
            setResult(RESULT_OK, new Intent(InformationDetails.this, InformationMainActivity.class));
            isReadInformation = false;
        }
        super.onBackPressed();
    }
}
