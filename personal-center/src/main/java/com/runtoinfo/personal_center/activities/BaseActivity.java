package com.runtoinfo.personal_center.activities;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import com.runtoinfo.youxiao.globalTools.utils.SPUtils;

/**
 * Created by QiaoJunChao on 2018/8/30.
 */

public abstract class BaseActivity extends FragmentActivity {

    public SPUtils spUtils;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        spUtils = new SPUtils(this);
        initView();
    }

    protected abstract void initView();
}
