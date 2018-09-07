package com.runto.cources.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.runto.cources.R;

@Route(path = "/course/courseLeaveCode")
public class CourseLeaveCodeActivity extends Activity {

    //CourseLeaveRecordBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.course_leave_record);
//        binding = DataBindingUtil.setContentView(this, R.layout.course_leave_record);
//        binding.courseLeaveCodeBack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onBackPressed();
//            }
//        });
    }
}
