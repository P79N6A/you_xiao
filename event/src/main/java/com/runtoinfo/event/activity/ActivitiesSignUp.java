package com.runtoinfo.event.activity;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;

import com.runtoinfo.event.R;
import com.runtoinfo.event.databinding.ActivitySiginUpBinding;
import com.runtoinfo.event.databinding.ActivitySignUpSuccessBinding;
import com.runtoinfo.event.dialog.SignUpSuccess;

public class ActivitiesSignUp extends Activity {

    ActivitySiginUpBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sigin_up);

        binding.activitySignAlone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignUpSuccess signUpSuccess = new SignUpSuccess(ActivitiesSignUp.this);
                signUpSuccess.show();
            }
        });

        binding.activityAddFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ActivitiesSignUp.this, SignUpAddEntourage.class));
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
