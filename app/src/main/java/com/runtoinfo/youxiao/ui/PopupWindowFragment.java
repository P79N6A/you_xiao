package com.runtoinfo.youxiao.ui;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.runtoinfo.youxiao.R;
import com.runtoinfo.youxiao.adapter.ListViewAdapter;

import java.util.List;

/**
 * Created by Administrator on 2018/5/28 0028.
 */

public class PopupWindowFragment {

    public Context context;
    public ListView listView;
    public ListViewAdapter adapter;
    public PopupWindowFragment(Context context){
        this.context = context;
    }

    public PopupWindow popupWindow;
    @SuppressLint("WrongConstant")
    public void showPopupWindows(List<Drawable> list, int gravity, int x, int y){

        View v = LayoutInflater.from(context).inflate(R.layout.fragment_home_popupwindow, null);

        popupWindow = new PopupWindow(v, 200,200,/*ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,*/ true);
        //popupWindow.setBackgroundDrawable(R.drawable.);
        listView = v.findViewById(R.id.fragment_home_item_list);
        adapter = new ListViewAdapter(context, list);
        listView.setAdapter(adapter);

        popupWindow.setOutsideTouchable(true);//设置电机外部取消窗口
        popupWindow.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.in_button_bg));
        popupWindow.getBackground().setAlpha(80);//设置透明度
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_MODE_CHANGED);//防止软键盘遮挡
        //popupWindow.showAsDropDown(v);
        popupWindow.showAtLocation(v, gravity, x, y);
    }

}
