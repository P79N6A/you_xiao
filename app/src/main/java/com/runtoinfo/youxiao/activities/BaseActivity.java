package com.runtoinfo.youxiao.activities;

import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.alibaba.sdk.android.man.MANService;
import com.alibaba.sdk.android.man.MANServiceProvider;
import com.gyf.barlibrary.ImmersionBar;
import com.runtoinfo.youxiao.globalTools.utils.SPUtils;

import java.lang.reflect.Field;

import myapplication.MyApplication;

/**
 * Created by Administrator on 2018/5/8 0008.
 */

public abstract class BaseActivity extends FragmentActivity {

    public SPUtils spUtils;
    public Bundle saveBundle;


    @Override
    public void onResume() {
        super.onResume();
        MANService manService = MANServiceProvider.getService();
        manService.getMANPageHitHelper().pageAppear(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.saveBundle = savedInstanceState;
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        initShared();
        initView();
        //添加Activity到堆栈
        MyApplication.getInstance().addActivity(this);
        initData();
    }

    @Override
    public void onPause() {
        super.onPause();
        MANService manService = MANServiceProvider.getService();
        manService.getMANPageHitHelper().pageDisAppear(this);
    }

    public void getView(View view){
        ImmersionBar.with(this).titleBar(view).init();
    }

    //protected abstract int getLayoutId();
    protected abstract void initView();
    protected abstract void initData();

    public void initShared(){
        spUtils = new SPUtils(this);
    }

    public void initState(LinearLayout linear_bar) {

        //当系统版本为4.4或者4.4以上时可以使用沉浸式状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            //
            linear_bar.setVisibility(View.VISIBLE);
            //获取到状态栏的高度
            int statusHeight = getStatusBarHeight();
            //动态的设置隐藏布局的高度
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) linear_bar.getLayoutParams();
            params.height = statusHeight;
            linear_bar.setLayoutParams(params);

            getView(linear_bar);
        }
    }
    /**
     * 通过反射的方式获取状态栏高度
     *
     * @return
     */
    private int getStatusBarHeight() {
        try {
            Class<?> c = Class.forName("com.android.internal.R$dimen");
            Object obj = c.newInstance();
            Field field = c.getField("status_bar_height");
            int x = Integer.parseInt(field.get(obj).toString());
            return getResources().getDimensionPixelSize(x);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void finished(){
        MyApplication.getInstance().finishAllActivity();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ImmersionBar.with(this).destroy();

        //结束Activity&从栈中移除该Activity
        //MyApplication.getInstance().finishAllActivity();
    }

    public void refresh(){
        onCreate(saveBundle);
    }

}
