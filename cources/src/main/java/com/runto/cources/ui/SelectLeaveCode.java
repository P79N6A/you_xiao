package com.runto.cources.ui;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.runto.cources.R;
import com.runtoinfo.youxiao.common_ui.timepicker.DatePickerView;

/**
 * Created by Administrator on 2018/7/19 0019.
 */

public class SelectLeaveCode extends Dialog {

    public DatePickerView dataPickerView;
    public SelectLeaveCode(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.leave_comment_select);
        initView();
    }

    public void initView(){
        dataPickerView = findViewById(R.id.leave_select_picker);
    }
}
