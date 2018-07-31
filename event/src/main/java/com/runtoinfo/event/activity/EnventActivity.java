package com.runtoinfo.event.activity;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.runtoinfo.event.R;
import com.runtoinfo.event.databinding.ActivityActivitiesMainBinding;

@Route(path = "/event/eventActivity")
public class EnventActivity extends Activity {

    public ActivityActivitiesMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_activities_main);
        initView();
    }

    public void initView(){

        binding.activityCommentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EnventActivity.this, ActivitiesEventDetails.class));
            }
        });
        binding.activityImgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
