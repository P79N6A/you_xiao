package com.runtoinfo.personal_center.ui;

import android.app.TimePickerDialog;
import android.content.Context;

/**
 * Created by Administrator on 2018/6/22 0022.
 */

public class TimeSelectPicker extends TimePickerDialog{


    public TimeSelectPicker(Context context, OnTimeSetListener listener, int hourOfDay, int minute, boolean is24HourView) {
        super(context, listener, hourOfDay, minute, is24HourView);
    }
}
