package com.runtoinfo.youxiao.globalTools.utils;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.runtoinfo.youxiao.globalTools.R;
import com.victor.loading.rotate.RotateLoading;

/**
 * Created by QiaoJunChao on 2018/8/21.
 */

public class DialogMessage {

    public static RotateLoading rotateLoading;


    //等待dialog
    public static void createDialog(Context context, ProgressDialog progressDialog, String msg){
        if (progressDialog == null) progressDialog = new ProgressDialog(context);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage(msg);
        progressDialog.show();
    }

    public static void showLoading(Context context, Dialog dialog, boolean isShow){
        if (dialog == null) dialog = new Dialog(context);
        if (isShow) {
            View view = LayoutInflater.from(context).inflate(R.layout.dialog_layout, null);
            rotateLoading = view.findViewById(R.id.dialog_loading);
            dialog.setContentView(view);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
            rotateLoading.start();
        } else{
            rotateLoading.stop();
            dialog.dismiss();
        }
    }

    public static void showToast(Context context, String msg){
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}
