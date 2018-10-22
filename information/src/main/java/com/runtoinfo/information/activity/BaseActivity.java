package com.runtoinfo.information.activity;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.qjc.library.StatusBarUtil;
import com.runtoinfo.information.R;
import com.runtoinfo.youxiao.globalTools.utils.DensityUtil;
import com.runtoinfo.youxiao.globalTools.utils.SPUtils;

/**
 * Created by QiaoJunChao on 2018/9/5.
 */

public class BaseActivity extends Activity {

    public SPUtils spUtils;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        spUtils = new SPUtils(this);
    }

    public void setStatues(View view){
        StatusBarUtil.setColor(this, getResources().getColor(R.color.white));
        //DensityUtil.setViewHeight(this, view);
    }
}
