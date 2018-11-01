package com.runtoinfo.personal_center.activities;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.runtoinfo.personal_center.R;
import com.runtoinfo.personal_center.databinding.ActivityPersonalAboutUsBinding;

@Route(path = "/personal/aboutUs")
public class PersonalAboutUs extends BaseActivity {

    ActivityPersonalAboutUsBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_personal_about_us);
        setStatusBar(binding.aboutUsTitleLayout);
        binding.activityImgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
