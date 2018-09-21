package com.runtoinfo.information.activity;

import android.databinding.DataBindingUtil;
import android.opengl.Visibility;
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
import com.runtoinfo.youxiao.globalTools.utils.IntentDataType;

import java.util.HashMap;
import java.util.Map;

@Route(path = "/information/informationActivity")
public class InformationMainActivity extends BaseActivity {

    InformationMainCenterBinding binding;
    public HttpUtils httpUtils;
    public String[] notificationNames = {"system", "lessonReminder", "campusNotice"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.information_main_center);
        httpUtils = new HttpUtils(getBaseContext());
        initData();
        requestCount();
    }


    //获取用户未读消息数量
    public void requestCount(){
        RequestDataEntity requestDataEntity = new RequestDataEntity();
        requestDataEntity.setUrl(HttpEntity.MAIN_URL + HttpEntity.GET_NOTIFICATION_COUNT);
        requestDataEntity.setToken(spUtils.getString(Entity.TOKEN));

        Map<String, Object> map = new HashMap<>();
        map.put("tenantId", spUtils.getInt(Entity.TENANT_ID));
        map.put("userId", spUtils.getInt(Entity.USER_ID));

        for (int i = 0; i < notificationNames.length; i++){
            requestDataEntity.setType(i + 1);
            map.put("notificationName", notificationNames[i]);
            httpUtils.getNotificationCount(handler, requestDataEntity, map);
        }
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
                buildTypeIntent(Entity.SYSTEM);
            }
        });

        /**
         * 上课提醒
         */
        binding.infoClassNoticeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buildTypeIntent(Entity.LESSON_REMINDER);
            }
        });

        /**
         * 学校通知
         */
        binding.infoSchoolNoticeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buildTypeIntent(Entity.CAMPUS_NOTICE);
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
                .withString(IntentDataType.TYPE, type).navigation();
    }

    public Handler handler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 31:
                    if ((int) msg.obj > 0){
                        binding.infoSystemLayout.setVisibility(View.VISIBLE);
                        binding.infoSystemCount.setText(msg.obj.toString());
                    }else{
                        binding.infoSystemLayout.setVisibility(View.GONE);
                    }
                    break;
                case 32:
                    if ((int) msg.obj > 0){
                        binding.infoLessonLayout.setVisibility(View.VISIBLE);
                        binding.infoLessonCount.setText(msg.obj.toString());
                    }else{
                        binding.infoLessonLayout.setVisibility(View.GONE);
                    }
                    break;
                case 33:
                    if ((int) msg.obj > 0){
                        binding.infoCampusLayout.setVisibility(View.VISIBLE);
                        binding.infoCampusCount.setText(msg.obj.toString());
                    }else{
                        binding.infoCampusLayout.setVisibility(View.GONE);
                    }
                    break;
            }
        }
    };
}
