package com.runtoinfo.event.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;

import com.runtoinfo.youxiao.common_ui.utils.SPUtils;

/**
 * Created by QiaoJunChao on 2018/8/21.
 */

public abstract class EventBaseActivity extends Activity {

    public SPUtils spUtils;
    @SuppressLint("WrongConstant")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initShared();
        initData();
        initEvent();

    }

    protected abstract void initView();
    protected abstract void initData();
    protected abstract void initEvent();

    public void initShared(){
        spUtils = new SPUtils(this);
    }
}
