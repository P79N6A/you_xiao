package com.runtoinfo.youxiao.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
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
import com.runtoinfo.youxiao.entity.SelectSchoolEntity;
import com.runtoinfo.youxiao.utils.Utils;

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
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @SuppressLint("WrongConstant")
    public void showPopupWindows(List<SelectSchoolEntity> list, View view){

        View v = LayoutInflater.from(context).inflate(R.layout.fragment_home_popupwindow, null);
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        int[] mScreen = getScreen(context);
        int mHeight = view.getBottom() - view.getTop();//获取控件的高度
        int mWidth = view.getWidth();

        listView = v.findViewById(R.id.fragment_home_item_list);
        adapter = new ListViewAdapter(activity, list);
        listView.setAdapter(adapter);

        int y = Utils.getTotalHeightofListView(listView) + 50;

        popupWindow = new PopupWindow(v, ViewGroup.LayoutParams.WRAP_CONTENT, y);

        popupWindow.setOutsideTouchable(true);//设置电机外部取消窗口
        //popupWindow.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.back_ground_radius_8dp));
        //popupWindow.getBackground().setAlpha(100);//设置透明度
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_MODE_CHANGED);//防止软键盘遮挡



        popupWindow.showAsDropDown(view, 0, 0);


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
