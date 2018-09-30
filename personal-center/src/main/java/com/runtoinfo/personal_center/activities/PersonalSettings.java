package com.runtoinfo.personal_center.activities;

import android.app.Activity;
import android.app.Dialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.runtoinfo.httpUtils.HttpEntity;
import com.runtoinfo.httpUtils.bean.PersonalCenterEntity;
import com.runtoinfo.httpUtils.bean.RequestDataEntity;
import com.runtoinfo.httpUtils.bean.VersionEntity;
import com.runtoinfo.httpUtils.utils.HttpUtils;
import com.runtoinfo.personal_center.BuildConfig;
import com.runtoinfo.personal_center.R;
import com.runtoinfo.personal_center.databinding.ActivityPersonalSettingsBinding;
import com.runtoinfo.youxiao.globalTools.utils.DialogMessage;
import com.runtoinfo.youxiao.globalTools.utils.Entity;
import com.runtoinfo.youxiao.globalTools.utils.IntentDataType;
import com.runtoinfo.youxiao.globalTools.utils.SPUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Route(path = "/personal/personalSettings")
public class PersonalSettings extends BaseActivity {

    public ActivityPersonalSettingsBinding binding;
    public HttpUtils httpUtils;
    public Dialog dialog;
    public String personalInfoData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    public void initView(){
        binding = DataBindingUtil.setContentView(this, R.layout.activity_personal_settings);
        httpUtils = new HttpUtils(this);
        dialog = new Dialog(this, R.style.dialog);
        binding.personalSettingPersonLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build("/personal/personalMain").withString(IntentDataType.DATA, personalInfoData).navigation();
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
                DialogMessage.showLoading(PersonalSettings.this, dialog, true);
                checkVersionFromServer();
            }
        });

        initData();
    }

    public void initData() {
        binding.perSettingPhoneNumber.setText(spUtils.getString(Entity.PHONE_NUMBER));
        binding.psSettingName.setText(spUtils.getString(Entity.NAME));
        httpUtils.postSrcPhoto(PersonalSettings.this, HttpEntity.IMAGE_HEAD + spUtils.getString(Entity.AVATAR), binding.perSettingAvatar);
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
            switch (msg.what){
                case 0:
                    VersionEntity version = (VersionEntity) msg.obj;
                    String [] versions = version.getVersion().split(".");
                    StringBuilder strVersion = new StringBuilder("");
                    for (String s : versions){
                        strVersion.append(s);
                    }
                    String[] locationVersion = BuildConfig.VERSION_NAME.split(".");
                    StringBuilder locVersion = new StringBuilder("");
                    for (String l : locationVersion){
                        locVersion.append(l);
                    }
                    if (Integer.parseInt(strVersion.toString()) > Integer.parseInt(locVersion.toString())){
                        //showUpdate(version.getDescription(), version.getiOSUpgradePath());
                        DialogMessage.showToast(PersonalSettings.this, "有新的更新！");
                    }
                    break;
                case 1:
                    break;
            }
        }
    };
}
