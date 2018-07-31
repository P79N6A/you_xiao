package com.runtoinfo.youxiao.activities;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.runtoinfo.youxiao.R;
import com.runtoinfo.youxiao.databinding.SchoolMovmentBinding;

@Route(path = "/main/schoolDynamics")
public class SchoolDynamics extends Activity {

    public SchoolMovmentBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.school_movment);
        binding = DataBindingUtil.setContentView(this, R.layout.school_movment);
        binding.activityImgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
