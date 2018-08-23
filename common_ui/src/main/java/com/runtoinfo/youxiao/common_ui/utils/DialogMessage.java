package com.runtoinfo.youxiao.common_ui.utils;

import android.app.ProgressDialog;
import android.content.Context;

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
}
