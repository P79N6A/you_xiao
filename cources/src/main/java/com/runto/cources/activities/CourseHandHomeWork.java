package com.runto.cources.activities;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.dmcbig.mediapicker.PickerActivity;
import com.dmcbig.mediapicker.PickerConfig;
import com.dmcbig.mediapicker.TakePhotoActivity;
import com.dmcbig.mediapicker.entity.Media;
import com.donkingliang.imageselector.utils.ImageSelector;
import com.runto.cources.R;
import com.runto.cources.adapter.GridViewAdapter;
import com.runto.cources.adapter.ImageAdapter;
import com.runto.cources.databinding.CourseHandHomeworkBinding;
import com.runtoinfo.teacher.HttpEntity;
import com.runtoinfo.teacher.utils.HttpUtils;
import com.runtoinfo.youxiao.common_ui.adapter.BaseViewHolder;
import com.runtoinfo.youxiao.common_ui.adapter.UniversalRecyclerAdapter;
import com.runtoinfo.youxiao.common_ui.utils.DialogMessage;
import com.runtoinfo.youxiao.common_ui.utils.Entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Route(path = "/course/handHomeWork")
public class CourseHandHomeWork extends BaseActivity {

    public CourseHandHomeworkBinding binding;
    public Dialog selectDialog = null;
    public GridViewAdapter gridViewAdapter;
    private static final int REQUEST_CODE = 0x00000011;
    private ImageAdapter mAdapter;
    public List<Media> dataList = new ArrayList<>();
    public ArrayList<Media> select = new ArrayList<>();
    public List<String> filePathList = new ArrayList<>();
    public ProgressDialog progressDialog;

    public void initView(){
        binding = DataBindingUtil.setContentView(this, R.layout.course_hand_homework);
        progressDialog = new ProgressDialog(this);
        initDialog();
    }

    public Handler handler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    if (progressDialog != null) progressDialog.dismiss();
                    postHomeWork();
                    break;
                case 404:
                    if (progressDialog != null) progressDialog.dismiss();
                    DialogMessage.showToast(CourseHandHomeWork.this, "上传失败");
                    break;
            }
        }
    };

    public void postHomeWork(){
        List<Map<String, Object>> listMap = new ArrayList<>();
        for (int i = 0; i < filePathList.size(); i++){
            int defaultType = mAdapter.getList().get(i).mediaType;
            String path = filePathList.get(i);
            Map<String, Object> map = new HashMap<>();
            int type;
            switch (defaultType){
                case 1:
                    type = 0;
                    map.put("type", type);
                    break;
            }
            map.put("path", path);
            listMap.add(map);
        }
        Map<String, Object> pra = new HashMap<>();
        pra.put("url", HttpEntity.MAIN_URL + HttpEntity.HAND_HOME_WORK);
        pra.put("courseId", spUtils.getInt(Entity.COURSE_ID));
        pra.put("courseInsId", spUtils.getInt(Entity.COURSE_INST_ID));
        pra.put("token", spUtils.getString(Entity.TOKEN));
        pra.put("userId", spUtils.getString(Entity.USER_ID));
        pra.put("remark", binding.courseHandWorkNots.getText().toString());

        HttpUtils.postHomeWork(handler, pra, listMap);
    }

    @Override
    protected void initData() {
        Media media = new Media(null, null, 0, 1, 0, R.drawable.course_take_photo, null);
        dataList.add(media);

        binding.courseAddPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectDialog.show();
            }
        });
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        gridLayoutManager.setAutoMeasureEnabled(true);
        binding.rvImage.setLayoutManager(gridLayoutManager);
        mAdapter = new ImageAdapter(CourseHandHomeWork.this, dataList, R.layout.adapter_image);
        binding.rvImage.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new UniversalRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (position == mAdapter.getItemCount() - 1) {
                    selectDialog.show();
                }
            }
        });
        binding.activityImgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        binding.courseHomeWorkCommitUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> dataMap = new HashMap<>();
                dataMap.put("url", HttpEntity.MAIN_URL + HttpEntity.POST_ALI_SERVER);
                dataMap.put("token",spUtils.getString(Entity.TOKEN));
                DialogMessage.createDialog(CourseHandHomeWork.this, progressDialog, "正在上传，请稍后...");
                HttpUtils.postVideoPhoto(handler, dataMap, mAdapter.dataList, filePathList);
            }
        });
    }

    public void initDialog(){
        selectDialog = new Dialog(CourseHandHomeWork.this, R.style.dialog);
        selectDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        selectDialog.setContentView(R.layout.course_add_home_work);
        selectDialog.setCancelable(true);
        Window window = selectDialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        WindowManager manager = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(dm);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = dm.widthPixels;
        window.setAttributes(lp);

        selectDialog.findViewById(R.id.select_type_phone).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectDialog.dismiss();
                Intent intent =new Intent(CourseHandHomeWork.this, PickerActivity.class);
                intent.putExtra(PickerConfig.SELECT_MODE,PickerConfig.PICKER_IMAGE_VIDEO);//default image and video (Optional)
                long maxSize=188743680L;//long long long
                intent.putExtra(PickerConfig.MAX_SELECT_SIZE,maxSize); //default 180MB (Optional)
                intent.putExtra(PickerConfig.MAX_SELECT_COUNT,15);  //default 40 (Optional)
                intent.putExtra(PickerConfig.DEFAULT_SELECTED_LIST,select); // (Optional)
                CourseHandHomeWork.this.startActivityForResult(intent,200);
            }
        });

        selectDialog.findViewById(R.id.select_type_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectDialog.dismiss();
                selectDialog.cancel();
            }
        });

        selectDialog.findViewById(R.id.select_type_take_photo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectDialog.dismiss();
                Intent intent =new Intent(CourseHandHomeWork.this, TakePhotoActivity.class); //Take a photo with a camera
                CourseHandHomeWork.this.startActivityForResult(intent,200);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200 && resultCode == PickerConfig.RESULT_CODE) {
            select=data.getParcelableArrayListExtra(PickerConfig.EXTRA_RESULT);
            for(Media media:select){
                Log.i("media",media.path);
                Log.e("media","s:"+media.size);
                //dataList.add(dataList.size() - 1, media);
                mAdapter.addItem(media, mAdapter.getItemCount() - 1);
                //mAdapter.notifyItemInserted(dataList.size() - 1);
                mAdapter.notifyDataSetChanged();
            }
        }
    }

}
