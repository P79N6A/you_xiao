package com.runtoinfo.youxiao.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.runtoinfo.youxiao.R;
import com.runtoinfo.youxiao.adapter.ListViewAdapter;

import java.sql.Wrapper;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/5/28 0028.
 */

public class PopupWindowFragment {

    public Context context;
    public ListView listView;
    public ListViewAdapter adapter;
    public Activity activity;
    public PopupWindowFragment(Context context, Activity activity){
        this.context = context;
        this.activity = activity;
    }

    public PopupWindow popupWindow;
    @SuppressLint("WrongConstant")
    public void showPopupWindows(List<Map<String, Object>> list, /*int gravity, int x, int y,*/ View view){

        View v = LayoutInflater.from(context).inflate(R.layout.fragment_home_popupwindow, null);
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        int[] mScreen = getScreen(context);
        int mHeight = view.getBottom() - view.getTop();//获取控件的高度
        popupWindow = new PopupWindow(v, ViewGroup.LayoutParams.WRAP_CONTENT, 250);
        listView = v.findViewById(R.id.fragment_home_item_list);
        adapter = new ListViewAdapter(context, list);
        listView.setAdapter(adapter);

        popupWindow.setOutsideTouchable(true);//设置电机外部取消窗口
        popupWindow.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.background_select_school));
        //popupWindow.getBackground().setAlpha(100);//设置透明度
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_MODE_CHANGED);//防止软键盘遮挡
        //popupWindow.showAsDropDown(v);
        
        popupWindow.showAsDropDown(view);

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams params = activity.getWindow().getAttributes();
                params.alpha=1f;
                activity.getWindow().setAttributes(params);
            }
        });
    }

    public static int[] getScreen(Context mContext)
    {
        WindowManager mManager = (WindowManager) mContext.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        mManager.getDefaultDisplay().getMetrics(outMetrics);
        return new int[]{outMetrics.widthPixels, outMetrics.heightPixels};
    }
}
