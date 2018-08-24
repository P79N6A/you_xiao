package com.runtoinfo.youxiao.common_ui.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;

/**
 * Created by QiaoJunChao on 2018/8/21.
 */

public class DialogMessage {
    //等待dialog
    public static void createDialog(Context context, ProgressDialog progressDialog, String msg){
        if (progressDialog == null) progressDialog = new ProgressDialog(context);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage(msg);
        progressDialog.show();
    }

    public static void showToast(Context context, String msg){
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}
