package com.runto.cources.activities;

import android.app.Activity;
import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.runto.cources.R;
@Route(path = "/course/handHomeWork")
public class CourseHandHomeWork extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course_hand_homework);
    }
}
