package com.runtoinfo.information.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.runtoinfo.httpUtils.HttpEntity;
import com.runtoinfo.httpUtils.bean.RequestDataEntity;
import com.runtoinfo.httpUtils.utils.HttpUtils;
import com.runtoinfo.information.R;
import com.runtoinfo.information.databinding.InformationMainCenterBinding;
import com.runtoinfo.youxiao.globalTools.utils.Entity;

@Route(path = "/information/informationActivity")
public class InformationMainActivity extends BaseActivity {

    InformationMainCenterBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.information_main_center);
        initData();
        request();
    }

    public void request(){
        RequestDataEntity entity = new RequestDataEntity();
        entity.setToken(spUtils.getString(Entity.TOKEN));
        entity.setUrl(HttpEntity.MAIN_URL + HttpEntity.GET_USER_NOTIFICATION_UNREAD);
        entity.setTenantId(spUtils.getInt(Entity.TENANT_ID));
        entity.setUserId(spUtils.getInt(Entity.USER_ID));
        HttpUtils.getUserUnreadNotification(handler, entity);
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

    public Handler handler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };
}
