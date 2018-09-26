package com.runtoinfo.personal_center.activities;

import android.app.Activity;
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
import com.runtoinfo.personal_center.R;
import com.runtoinfo.personal_center.databinding.ActivityPersonalSettingsBinding;
import com.runtoinfo.youxiao.globalTools.utils.Entity;

import java.util.HashMap;
import java.util.Map;

@Route(path = "/personal/personalSettings")
public class PersonalSettings extends BaseActivity {

    public ActivityPersonalSettingsBinding binding;
    public HttpUtils httpUtils;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        httpUtils = new HttpUtils(getApplicationContext());
        initView();
    }

    public void initView(){
        binding = DataBindingUtil.setContentView(this, R.layout.activity_personal_settings);
        binding.personalSettingPersonLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build("/personal/personalMain").navigation();
            }
        });
        binding.personalSettingBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        binding.exitNowSystem.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                spUtils.setString(Entity.TOKEN, "");
                ARouter.getInstance().build("/main/LoginActivity").navigation();
                PersonalSettings.this.finish();
            }
        });
        binding.personalSettingVersionLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    //获取版本号提示是否更新
    public void checkVersionFromServer(){
        RequestDataEntity requestDataEntity = new RequestDataEntity();
        requestDataEntity.setToken(spUtils.getString(Entity.TOKEN));
        requestDataEntity.setUrl(HttpEntity.MAIN_URL + HttpEntity.CHECK_VERSION);

        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("Platform", 1);
        dataMap.put("Sorting", "Version desc");

        httpUtils.checkVersion(handler, requestDataEntity, dataMap);
    }

    public Handler handler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {

        }
    };
}
