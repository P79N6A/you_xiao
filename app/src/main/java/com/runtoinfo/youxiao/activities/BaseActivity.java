package com.runtoinfo.youxiao.activities;

import android.Manifest;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.alibaba.sdk.android.man.MANService;
import com.alibaba.sdk.android.man.MANServiceProvider;
import com.gyf.barlibrary.ImmersionBar;
import com.jaeger.library.StatusBarUtil;
import com.runtoinfo.youxiao.R;
import com.runtoinfo.youxiao.globalTools.utils.SPUtils;

import java.lang.reflect.Field;

import myapplication.MyApplication;

/**
 * Created by Administrator on 2018/5/8 0008.
 */

public abstract class BaseActivity extends FragmentActivity {

    public SPUtils spUtils;
    public Bundle saveBundle;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE};
    private static int REQUEST_PERMISSION_CODE = 1;

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
        checkPermission();
        initShared();
        initView();
        //添加Activity到堆栈
        MyApplication.getInstance().addActivity(this);
        initData();
        setStatusBar();
    }

    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.colorPrimary));
    }

    public void checkPermission() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, REQUEST_PERMISSION_CODE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_CODE) {
            for (int i = 0; i < permissions.length; i++) {
                Log.i("MainActivity", "申请的权限为：" + permissions[i] + ",申请结果：" + grantResults[i]);
            }
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        MANService manService = MANServiceProvider.getService();
        manService.getMANPageHitHelper().pageDisAppear(this);
    }

    public void getView(View view) {
        ImmersionBar.with(this).titleBar(view).init();
    }

    protected abstract void initView();

    protected abstract void initData();

    public void initShared() {
        spUtils = new SPUtils(this);
    }


    public void finished() {
        MyApplication.getInstance().finishAllActivity();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ImmersionBar.with(this).destroy();
    }
}
