package com.runtoinfo.information.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.runtoinfo.youxiao.globalTools.utils.SPUtils;

/**
 * Created by QiaoJunChao on 2018/9/5.
 */

public class BaseActivity extends Activity {

    public SPUtils spUtils;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        spUtils = new SPUtils(this);
    }
}
