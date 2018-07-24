package com.runtoinfo.youxiao.activities;

import android.app.Activity;
import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.runtoinfo.youxiao.R;

@Route(path = "/main/schoolDynamics")
public class SchoolDynamics extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_topics);
    }
}
