package com.runtoinfo.event.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;

import com.qjc.library.StatusBarUtil;
import com.runtoinfo.event.R;
import com.runtoinfo.youxiao.globalTools.utils.DensityUtil;
import com.runtoinfo.youxiao.globalTools.utils.SPUtils;

/**
 * Created by QiaoJunChao on 2018/8/21.
 */

public abstract class EventBaseActivity extends Activity {

    public SPUtils spUtils;
    @SuppressLint("WrongConstant")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        initShared();
        initView();
        initData();
        initEvent();

    }

    protected abstract void initView();
    protected abstract void initData();
    protected abstract void initEvent();

    public void initShared(){
        spUtils = new SPUtils(this);
    }

    public void setStatusBar(View view){
        StatusBarUtil.setColor(this, getResources().getColor(R.color.white));
        //DensityUtil.setMargin(this, view);
    }
}
