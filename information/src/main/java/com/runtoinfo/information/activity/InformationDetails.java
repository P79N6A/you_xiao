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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.runtoinfo.httpUtils.CPRCBean.CPRCDataEntity;
import com.runtoinfo.httpUtils.CPRCBean.CPRCTypeEntity;
import com.runtoinfo.httpUtils.CPRCBean.CommentRequestResultEntity;
import com.runtoinfo.httpUtils.CPRCBean.MyCommentEntity;
import com.runtoinfo.httpUtils.CPRCBean.PraiseEntity;
import com.runtoinfo.httpUtils.HttpEntity;
import com.runtoinfo.httpUtils.bean.RequestDataEntity;
import com.runtoinfo.httpUtils.bean.SystemMessageEntity;
import com.runtoinfo.httpUtils.bean.SystemMessageGroupEntity;
import com.runtoinfo.httpUtils.utils.HttpUtils;
import com.runtoinfo.information.R;
import com.runtoinfo.information.adapter.MyCommentAdapter;
import com.runtoinfo.information.adapter.PraiseAdapter;
import com.runtoinfo.information.adapter.SystemMessageAdapter;
import com.runtoinfo.information.databinding.ClassReminderBinding;
import com.runtoinfo.information.databinding.CommentInformationBinding;
import com.runtoinfo.information.databinding.PraiseInformationBinding;
import com.runtoinfo.information.databinding.SchoolNoticeBinding;
import com.runtoinfo.information.databinding.SystemInformationBinding;
import com.runtoinfo.youxiao.globalTools.utils.DensityUtil;
import com.runtoinfo.youxiao.globalTools.utils.DialogMessage;
import com.runtoinfo.youxiao.globalTools.utils.Entity;
import com.runtoinfo.youxiao.globalTools.utils.IntentDataType;
import com.runtoinfo.youxiao.globalTools.utils.RecyclerViewDecoration;
import com.runtoinfo.youxiao.globalTools.utils.TimeUtil;
import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

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
    public List<SystemMessageGroupEntity> noticeList = new ArrayList<>();
    public SystemMessageAdapter messageAdapter;
    public HttpUtils httpUtils;
    public boolean isReadInformation = false;//标记是否进入过页面，是否更改过消息状态
    public int page = 1;

    public String layoutType;

    public MyCommentEntity myCommentEntity;//我的评论（传递过来的参数）

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        httpUtils = new HttpUtils(this);
        layoutType = getIntent().getExtras().getString(IntentDataType.TYPE);
        switch (layoutType) {
            case Entity.SYSTEM:
                systemBinding = DataBindingUtil.setContentView(this, R.layout.system_information);
                setStatues(systemBinding.systemTitleLayout);
                initSystemData();
                break;
            case Entity.LESSON_REMINDER:
                reminderBinding = DataBindingUtil.setContentView(this, R.layout.class_reminder);
                setStatues(reminderBinding.classTitleLayout);
                initClassReminderData();
                break;
            case Entity.CAMPUS_NOTICE:
                schoolNoticeBinding = DataBindingUtil.setContentView(this, R.layout.school_notice);
                setStatues(schoolNoticeBinding.schoolTitleLayout);
                initSchoolData();
                break;
            case "comment":
                commentBinding = DataBindingUtil.setContentView(this, R.layout.comment_information);
                setStatues(commentBinding.commentTitleLayout);
                setCommentRequestData(page);
                initCommentEvent();
                break;
            case "praise":
                praiseBinding = DataBindingUtil.setContentView(this, R.layout.praise_information);
                setStatues(praiseBinding.praiseTitleLayout);
                requestPraiseData(page);
                initPraiseData();
                break;
            default:
                systemBinding = DataBindingUtil.setContentView(this, R.layout.system_information);
                setStatues(systemBinding.systemTitleLayout);
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
        entity.setUrl(HttpEntity.MAIN_URL + HttpEntity.GET_USER_NOTIFICATION_GROUP_UNREAD);
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
    public void requestPraiseData(int page) {
        RequestDataEntity entity = new RequestDataEntity();
        entity.setUrl(HttpEntity.MAIN_URL + HttpEntity.GET_PRAISE_ME/**/);
        entity.setToken(spUtils.getString(Entity.TOKEN));

        Map<String, Object> map = new HashMap<>();
        map.put("skipCount", DensityUtil.getOffSet(page));
        map.put("maxResultCount", 10);
        map.put("userId", spUtils.getInt(Entity.USER_ID));

        httpUtils.getMyCommentOrPraise(handler, entity, map);
    }

    //为赞准备展现数据
    public PraiseAdapter praiseAdapter;
    public void initPraiseRecycler() {
        if (praiseAdapter != null){
            praiseAdapter.notifyDataSetChanged();
            return;
        }
        praiseAdapter = new PraiseAdapter(InformationDetails.this, praiseEntityList, R.layout.info_praise_item_layout);
        praiseBinding.praiseRecycler.setLinearLayout();
        praiseBinding.praiseRecycler.setAdapter(praiseAdapter);
        praiseBinding.praiseRecycler.addItemDecoration(new RecyclerViewDecoration(InformationDetails.this, RecyclerViewDecoration.HORIZONTAL_LIST));
        praiseBinding.praiseRecycler.setOnPullLoadMoreListener(pullLoadMoreListener);
    }

    public PullLoadMoreRecyclerView.PullLoadMoreListener pullLoadMoreListener = new PullLoadMoreRecyclerView.PullLoadMoreListener() {
        @Override
        public void onRefresh() {
            page = 1;
            refreshData();
        }

        @Override
        public void onLoadMore() {
            page++;
            refreshData();
        }
    };

    public void refreshData() {
        switch (layoutType){
            case "comment":
                setCommentRequestData(page);
                break;
            case "praise":
                requestPraiseData(page);
                break;
        }
    }

    //获取评论
    public void setCommentRequestData(int page) {
        RequestDataEntity entity = new RequestDataEntity();
        entity.setUrl(HttpEntity.MAIN_URL + HttpEntity.GET_REPLY_ME);
        entity.setToken(spUtils.getString(Entity.TOKEN));
        entity.setUserId(spUtils.getInt(Entity.USER_ID));

        Map<String, Object> map = new HashMap<>();
        map.put("skipCount", DensityUtil.getOffSet(page));
        map.put("maxResultCount", 10);
        map.put("userId", spUtils.getInt(Entity.USER_ID));

        httpUtils.getMyCommentOrPraise(handler, entity, map);
    }

    //评论、赞数据解析
    public List setListPraiseOrComment(JSONArray items, String type) {
        try {
            List list = new ArrayList();
            switch (type){
                case "comment":
                    for (int i = 0; i < items.length(); i++) {
                        JSONObject item = items.getJSONObject(i);
                        MyCommentEntity myCommentEntity =
                                new Gson().fromJson(item.toString(), new TypeToken<MyCommentEntity>() {}.getType());
                        list.add(myCommentEntity);
                    }
                    break;
                case "praise":
                    for (int i = 0; i < items.length(); i++) {
                        JSONObject item = items.getJSONObject(i);
                        PraiseEntity praiseEntity =
                                new Gson().fromJson(item.toString(), new TypeToken<PraiseEntity>() {}.getType());
                        list.add(praiseEntity);
                    }
                    break;
            }
            return list;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    //加载我的评论
    public MyCommentAdapter commentAdapter;
    public void initCommentRecycleData() {
        if (commentAdapter == null){
            commentAdapter = new MyCommentAdapter(handler, InformationDetails.this, commentEntityList, R.layout.info_comment_item_layout);
            commentBinding.infoCommentRecycler.setLinearLayout();
            commentBinding.infoCommentRecycler.setAdapter(commentAdapter);
            commentBinding.infoCommentRecycler.addItemDecoration(new RecyclerViewDecoration(this, RecyclerViewDecoration.HORIZONTAL_LIST));
            commentBinding.infoCommentRecycler.setOnPullLoadMoreListener(pullLoadMoreListener);
            commentAdapter.setCommentListener(commentListener);
        }else{
            commentAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 我的评论：回复监听事件
     */
    public MyCommentAdapter.CommentListener commentListener = new MyCommentAdapter.CommentListener() {
        @Override
        public void commentListenner(View v, int positon, MyCommentEntity entity) {
            DialogMessage.showBottomDialog(handler, -1, InformationDetails.this, true);
            myCommentEntity = entity;
        }
    };

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
            SystemMessageGroupEntity messageEntity = noticeList.get(i);
            List<SystemMessageEntity> list = messageEntity.getItems();
            for (int j = 0; j < list.size(); j++) {
                SystemMessageEntity entity = list.get(j);
                idList.add(entity.getId());
            }

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
                case 20:
                    String content = msg.obj.toString();
                    if (myCommentEntity != null){
                        CPRCDataEntity entity = new CPRCDataEntity();
                        entity.setType(CPRCTypeEntity.REPLY);
                        entity.setParentId(String.valueOf(myCommentEntity.getReplyId()));
                        entity.setParentType(CPRCTypeEntity.PARENT_REPLY);
                        entity.setTarget(myCommentEntity.getTargetId());
                        entity.setTargetType(CPRCTypeEntity.TARGET_REPLY);
                        entity.setUserId(spUtils.getInt(Entity.USER_ID));
                        entity.setContent(content.concat("//@")
                                .concat(myCommentEntity.getReplyer() == null ? "" :myCommentEntity.getReplyer())
                                .concat(":").concat(myCommentEntity.getReplyContent() == null ? "" : myCommentEntity.getReplyContent()));
                        httpUtils.postComment(handler, entity);
                    }
                    break;
                case 2:
                    DialogMessage.showBottomDialog(handler, -1, InformationDetails.this, false);
                    CommentRequestResultEntity resultEntity = new Gson().fromJson(msg.obj.toString(), new TypeToken<CommentRequestResultEntity>(){}.getType());
                    MyCommentEntity commentEntity = new MyCommentEntity();
                    //String rContent = resultEntity.getContent() + "//@" + resultEntity.getNickName() + ":";
                    commentEntity.setReplyContent(myCommentEntity.getReplyContent());
                    commentEntity.setReplyTime(TimeUtil.iso8601ToDate(resultEntity.getApprovedTime(), 1));
                    commentEntity.setReplyer(resultEntity.getNickName());
                    commentEntity.setTargetCover(myCommentEntity.getTargetCover());
                    commentEntity.setTargetTitle(myCommentEntity.getTargetTitle());
                    commentEntity.setTargetPublisher(myCommentEntity.getTargetPublisher());
                    commentAdapter.addItem(commentEntity, 0);
                    break;
                case 30:
                    switch (layoutType) {
                        case "comment":
                            JSONArray items = (JSONArray) msg.obj;
                            commentEntityList.addAll(setListPraiseOrComment(items, "comment"));
                            commentBinding.infoCommentRecycler.setPullLoadMoreCompleted();
                            initCommentRecycleData();
                            break;
                        case "praise":
                            JSONArray result = (JSONArray) msg.obj;
                            praiseEntityList = setListPraiseOrComment(result, "praise");
                            praiseBinding.praiseRecycler.setPullLoadMoreCompleted();
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
