package com.runtoinfo.information.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.runtoinfo.httpUtils.CPRCBean.PraiseEntity;
import com.runtoinfo.information.R;
import com.runtoinfo.information.adapter.MyCommentAdapter;
import com.runtoinfo.information.adapter.PraiseAdapter;
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
import com.runtoinfo.youxiao.globalTools.utils.RecyclerViewDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/7/31 0031.
 */

@Route(path = "/information/informationDetails")
public class InformationDetails extends BaseActivity {

    public SystemInformationBinding systemBinding;
    public ClassReminderBinding reminderBinding;
    public SchoolNoticeBinding schoolNoticeBinding;
    public CommentInformationBinding commentBinding;
    public PraiseInformationBinding praiseBinding;

    public List<MyCommentEntity> commentEntityList = new ArrayList<>();
    public List<PraiseEntity> praiseEntityList = new ArrayList<>();

    public String layoutType;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        layoutType = getIntent().getExtras().getString("type");
        switch (layoutType) {
            case "system":
                systemBinding = DataBindingUtil.setContentView(this, R.layout.system_information);
                initSystemData();
                break;
            case "classNotice":
                reminderBinding = DataBindingUtil.setContentView(this, R.layout.class_reminder);
                initClassReminderData();
                break;
            case "schoolNotice":
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

    public void initSystemData() {
        systemBinding.systemInformationBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

//        systemBinding.systemCheckCourse.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ARouter.getInstance().build("/cources/colorfulActivity").navigation();
//            }
//        });
    }

    public void initClassReminderData() {
        reminderBinding.systemInformationBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

//        reminderBinding.reminderGoCourse.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ARouter.getInstance().build("/cources/colorfulActivity").navigation();
//            }
//        });
    }

    public void initSchoolData() {
        schoolNoticeBinding.systemInformationBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void initPraiseData() {
        praiseBinding.systemInformationBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        if (praiseEntityList.size() > 0){
            praiseBinding.praiseNothing.setVisibility(View.VISIBLE);
        }else{
            praiseBinding.praiseNothing.setVisibility(View.GONE);
        }
    }

    public void requestPraiseData(){
        RequestDataEntity entity = new RequestDataEntity();
        entity.setUrl(HttpEntity.MAIN_URL + HttpEntity.GET_PRAISE_ME);
        entity.setUserId(spUtils.getInt(Entity.USER_ID));
        entity.setToken(spUtils.getString(Entity.TOKEN));
        HttpUtils.getMyCommentOrPraise(handler, entity);
    }

    public void initPraiseRecycler(){
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
        HttpUtils.getMyCommentOrPraise(handler, entity);
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

    public Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 30:
                    switch (layoutType){
                        case "comment":
                            break;
                        case "praise":
                            break;
                    }
                    break;
            }
        }
    };
}
