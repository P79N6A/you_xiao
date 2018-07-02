package com.runtoinfo.personal_center.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.runtoinfo.personal_center.R;

/**
 * Created by Administrator on 2018/6/29 0029.
 */

public class SelectPictureDialog extends Dialog {
    Activity activity;
    public SelectPictureDialog(Activity activity) {
        super(activity);
        this.activity = activity;
    }

    public static class ViewHolder{
        public TextView takePhoto, selectPhoto, cancel;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_avatar_layout);
        ViewHolder holder = new ViewHolder();
        holder.takePhoto = findViewById(R.id.personal_take_photo);
        holder.selectPhoto = findViewById(R.id.personal_from_pic);
        holder.cancel = findViewById(R.id.personal_cancle);

        holder.takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build("/camera/activity").navigation(activity, 1001);
                dismiss();
            }
        });
        holder.selectPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                activity.startActivityForResult(intent, 1001);
                dismiss();
            }
        });
        holder.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
