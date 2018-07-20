package com.runtoinfo.event.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.runtoinfo.event.R;
import com.runtoinfo.event.dialog.SignUpSuccess;

public class SignUpAddEntourage extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_entourage);
        findViewById(R.id.activity_sign_up).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignUpSuccess signUpSuccess = new SignUpSuccess(SignUpAddEntourage.this);
                signUpSuccess.show();
            }
        });
    }

}
