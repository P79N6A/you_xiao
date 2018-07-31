package com.runto.cources.ui;

import android.app.Activity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.runto.cources.R;

/**
 * Created by Administrator on 2018/7/27 0027.
 */

public class PopupWindowShowUpView extends PopupWindow{
    private int popupWidth;
    private int popupHeight;

    public PopupWindowShowUpView(Activity context/*, BusTicket busTicket, int size, boolean isInsurance*/) {
        super(context);
        View view = LayoutInflater.from(context).inflate(R.layout.player_turn_speed_dialog, null);
        // 设置可以获得焦点
        setFocusable(true);
        // 设置弹窗内可点击
        setTouchable(true);
        // 设置弹窗外可点击
        setOutsideTouchable(true);
        setWidth(/*ViewGroup.LayoutParams.WRAP_CONTENT*/450);
        setHeight(/*ViewGroup.LayoutParams.WRAP_CONTENT*/200);
        setAnimationStyle(R.style.popup_animation);
        getBackground().setAlpha(0);
        setContentView(view);
        //获取自身的长宽高
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        popupHeight = view.getMeasuredHeight();
        popupWidth = view.getMeasuredWidth();
    }
    public void showUp(View v) {
        //获取需要在其上方显示的控件的位置信息
        int[] location = new int[2];
        v.getLocationOnScreen(location);
        //在控件上方显示
        showAtLocation(v, Gravity.NO_GRAVITY, (location[0] + v.getWidth() / 2) - popupWidth / 2 + v.getWidth()/2, location[1] - popupHeight);
    }
}
