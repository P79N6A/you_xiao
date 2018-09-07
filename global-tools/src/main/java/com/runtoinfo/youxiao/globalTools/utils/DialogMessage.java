package com.runtoinfo.youxiao.globalTools.utils;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.runtoinfo.youxiao.globalTools.R;
import com.victor.loading.rotate.RotateLoading;


/**
 * Created by QiaoJunChao on 2018/8/21.
 */

public class DialogMessage {

    public static RotateLoading rotateLoading;
    public static Dialog bottomDialog;


    //等待dialog
    public static void createDialog(Context context, ProgressDialog progressDialog, String msg){
        if (progressDialog == null) progressDialog = new ProgressDialog(context);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage(msg);
        progressDialog.show();
    }

    /**
     * 等待加载dialog
     * @param context
     * @param dialog
     * @param isShow
     */
    public static void showLoading(Context context, Dialog dialog, boolean isShow){
        if (dialog == null) dialog = new Dialog(context);
        if (isShow) {
            View view = LayoutInflater.from(context).inflate(R.layout.dialog_layout, null);
            rotateLoading = view.findViewById(R.id.dialog_loading);
            dialog.setContentView(view);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
            rotateLoading.start();
        } else if (rotateLoading != null){
            rotateLoading.stop();
            dialog.dismiss();
        }
    }

    public static void showToast(Context context, String msg){
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static void showBottomDialog(final Handler handler,final int type, final Context context, boolean flag){
        if (flag) {
            bottomDialog = new Dialog(context, R.style.inputDialog);
            bottomDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            bottomDialog.setContentView(R.layout.dialog_comment_layout);
            setWindowBottom(bottomDialog, context);
            bottomDialog.show();

            bottomDialog.findViewById(R.id.comment_submit).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EditText editText = (EditText) bottomDialog.findViewById(R.id.comment_msg_edit);
                    Message msg= new Message();
                    switch (type){
                        case 0:
                            msg.what = 10;//直接评论
                            break;
                        case 1:
                            msg.what = 11;//直接回复
                            break;
                        case 2:
                            msg.what = 12;//回复回复
                            break;
                            default:
                                msg.what = 20;
                    }
                    msg.obj = editText.getText().toString();
                    handler.sendMessage(msg);
                }
            });
        }else{
            if (bottomDialog != null && bottomDialog.isShowing())
            bottomDialog.dismiss();
            bottomDialog = null;
        }
    }

    public static void setWindowBottom(Dialog dialog, Context context){
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(dm);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = dm.widthPixels;
        window.setAttributes(lp);
    }
}
