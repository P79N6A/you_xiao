package com.runtoinfo.information.activity;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.runtoinfo.information.R;
import com.runtoinfo.information.databinding.InformationMainCenterBinding;

@Route(path = "/information/informationActivity")
public class InformationMainActivity extends BaseActivity {

    InformationMainCenterBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.information_main_center);
       initData();
    }

    public void initData(){
        binding.infoImgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        /**
         * 系统消息
         */
        binding.infoSystemMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buildTypeIntent("system");
            }
        });

        /**
         * 上课提醒
         */
        binding.infoClassNoticeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buildTypeIntent("classNotice");
            }
        });

        /**
         * 学校通知
         */
        binding.infoSchoolNoticeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buildTypeIntent("schoolNotice");
            }
        });

        /**
         * 我的评论
         */
        binding.infoCommentRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buildTypeIntent("comment");
            }
        });

        /**
         * 我的赞
         */
        binding.infoPraiseRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buildTypeIntent("praise");
            }
        });
    }

    public void buildTypeIntent(String type){
        ARouter.getInstance().build("/information/informationDetails")
                .withString("type", type).navigation();
    }
}
