package com.runtoinfo.event.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.runtoinfo.event.R;

public class ActivitiesEventDetails extends Activity {

    //ActivitySignUpSuccessBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);
        //binding = DataBindingUtil.setContentView(this, R.layout.activity_event_details);
        findViewById(R.id.activity_sign_up_now).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ActivitiesEventDetails.this, ActivitiesSignUp.class));
            }
        });
    }
}
