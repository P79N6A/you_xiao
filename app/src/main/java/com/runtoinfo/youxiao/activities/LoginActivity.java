package com.runtoinfo.youxiao.activities;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.alibaba.android.arouter.launcher.ARouter;
import com.runtoinfo.personal_center.*;
import com.runtoinfo.youxiao.R;
import com.runtoinfo.youxiao.databinding.ActivityLoginBinding;
import com.runtoinfo.youxiao.databinding.ActivityMainBinding;
import com.runtoinfo.youxiao.fragment.BaseFragment;

public class LoginActivity extends BaseActivity {

    public ActivityLoginBinding binding;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {
        binding = DataBindingUtil.setContentView(LoginActivity.this, R.layout.activity_login);
        binding.loginBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build("/personal/personalMain").navigation();
            }
        });
    }

    @Override
    protected void initData() {

    }
}
