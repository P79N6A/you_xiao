package com.runtoinfo.youxiao;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by QiaoJunChao on 2018/12/7.
 */

public class ActivityCallBack implements Application.ActivityLifecycleCallbacks {

    public String TAG = "CallBack";
    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        Log.e(TAG, "created");
    }

    @Override
    public void onActivityStarted(Activity activity) {
        Log.e(TAG, "started");
    }

    @Override
    public void onActivityResumed(Activity activity) {
        Log.e(TAG, "resumed");
    }

    @Override
    public void onActivityPaused(Activity activity) {
        Log.e(TAG, "paused");
    }

    @Override
    public void onActivityStopped(Activity activity) {
        Log.e(TAG, "stopped");
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        Log.e(TAG, "state");
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        Log.e(TAG, "destroy");
    }
}
