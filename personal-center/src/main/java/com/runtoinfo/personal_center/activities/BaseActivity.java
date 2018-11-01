package com.runtoinfo.personal_center.activities;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.qjc.library.StatusBarUtil;
import com.runtoinfo.personal_center.R;
import com.runtoinfo.youxiao.globalTools.utils.SPUtils;

/**
 * Created by QiaoJunChao on 2018/8/30.
 */
@SuppressWarnings("all")
public abstract class BaseActivity extends FragmentActivity {

    public SPUtils spUtils;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        spUtils = new SPUtils(this);
        initView();
    }

    protected abstract void initView();

    public void setStatusBar(int color){
        StatusBarUtil.setColor(this, getResources().getColor(color));
    }

    public void setStatusBar(View view){
        StatusBarUtil.setColor(this, getResources().getColor(R.color.relative_background_color));
        //DensityUtil.setViewHeight(this, view);
    }
}
