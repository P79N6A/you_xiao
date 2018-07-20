package com.runtoinfo.event.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.runtoinfo.event.R;
import com.runtoinfo.event.dialog.SignUpSuccess;

public class ActivitiesSignUp extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sigin_up);

        findViewById(R.id.activity_sign_alone).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignUpSuccess signUpSuccess = new SignUpSuccess(ActivitiesSignUp.this);
                signUpSuccess.show();
            }
        });

        findViewById(R.id.activity_add_follow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ActivitiesSignUp.this, SignUpAddEntourage.class));
            }
        });
    }
}
