package com.runtoinfo.information;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.runtoinfo.information.databinding.ClassReminderBinding;
import com.runtoinfo.information.databinding.CommentInformationBinding;
import com.runtoinfo.information.databinding.PraiseInformationBinding;
import com.runtoinfo.information.databinding.SchoolNoticeBinding;
import com.runtoinfo.information.databinding.SystemInformationBinding;

/**
 * Created by Administrator on 2018/7/31 0031.
 */

@Route(path = "/information/informationDetails")
public class InformationDetails extends Activity{

    public SystemInformationBinding systemBinding;
    public ClassReminderBinding reminderBinding;
    public SchoolNoticeBinding schoolNoticeBinding;
    public CommentInformationBinding commentBinding;
    public PraiseInformationBinding praiseBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String type = getIntent().getExtras().getString("type");
        switch (type){
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
                initCommentData();
                break;
            case "praise":
                praiseBinding = DataBindingUtil.setContentView(this, R.layout.praise_information);
                initPraiseData();
                break;
                default:
                    systemBinding = DataBindingUtil.setContentView(this, R.layout.system_information);
                    initSystemData();
                    break;
        }


    }

    public void initSystemData(){
        systemBinding.systemInformationBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        systemBinding.systemCheckCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build("/cources/colorfulActivity").navigation();
            }
        });
    }

    public void initClassReminderData(){
        reminderBinding.systemInformationBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        reminderBinding.reminderGoCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build("/cources/colorfulActivity").navigation();
            }
        });
    }

    public void initSchoolData(){
        schoolNoticeBinding.systemInformationBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void initPraiseData(){
        praiseBinding.systemInformationBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void initCommentData(){
        commentBinding.systemInformationBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
