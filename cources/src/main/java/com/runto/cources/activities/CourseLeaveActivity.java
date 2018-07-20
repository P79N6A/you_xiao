package com.runto.cources.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;

import com.runto.cources.R;

public class CourseLeaveActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_course_leave);

        findViewById(R.id.course_leave_select).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressDialog dialog = new ProgressDialog(CourseLeaveActivity.this);

                //dialog.addContentView();
            }
        });
    }
}
