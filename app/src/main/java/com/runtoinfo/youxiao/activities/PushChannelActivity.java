package com.runtoinfo.youxiao.activities;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.alibaba.sdk.android.push.AndroidPopupActivity;

import java.util.Map;

/**
 * Created by QiaoJunChao on 2018/11/8.
 */

public class PushChannelActivity extends AndroidPopupActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    @Override
    protected void onSysNoticeOpened(String s, String s1, Map<String, String> map) {
        Log.e("Popup", s + " : " + s1 + "---" + map);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
