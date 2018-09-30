package com.runtoinfo.event.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import com.alibaba.android.arouter.launcher.ARouter;
import com.runtoinfo.event.R;

/**
 * Created by Administrator on 2018/7/17 0017.
 */
public class SignUpSuccess extends Dialog {
    //public ActivitySignUpSuccessBinding binding;
    public Context context;

    public SignUpSuccess(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_success);
        findViewById(R.id.activity_dialog_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });
        findViewById(R.id.activity_return_home).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build("/main/mainActivity").navigation();
                cancel();
            }
        });
    }

}
