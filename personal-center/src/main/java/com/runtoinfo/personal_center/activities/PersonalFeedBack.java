package com.runtoinfo.personal_center.activities;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.runtoinfo.personal_center.R;
import com.runtoinfo.personal_center.databinding.ActivityPersonalFeedBackBinding;

@Route(path = "/personal/feedback")
public class PersonalFeedBack extends BaseActivity {

    public ActivityPersonalFeedBackBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void initView(){
        binding = DataBindingUtil.setContentView(this, R.layout.activity_personal_feed_back);
        binding.personalFeedbackBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
