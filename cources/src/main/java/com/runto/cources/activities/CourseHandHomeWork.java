package com.runto.cources.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.donkingliang.imageselector.utils.ImageSelector;
import com.runto.cources.R;
import com.runto.cources.adapter.GridViewAdapter;
import com.runto.cources.adapter.ImageAdapter;
import com.runto.cources.databinding.CourseHandHomeworkBinding;

import java.util.ArrayList;

@Route(path = "/course/handHomeWork")
public class CourseHandHomeWork extends Activity {

    public CourseHandHomeworkBinding binding;
    public Dialog selectDialog = null;
    public GridViewAdapter gridViewAdapter;
    private static final int REQUEST_CODE = 0x00000011;
    public ArrayList<String> dataList = new ArrayList<>();
    private ImageAdapter mAdapter;
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.course_hand_homework);
        binding = DataBindingUtil.setContentView(this, R.layout.course_hand_homework);
        initDialog();
        initView();
    }

    public void initView(){
        binding.courseAddPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectDialog.show();
            }
        });
        binding.rvImage.setLayoutManager(new GridLayoutManager(this, 3));
        mAdapter = new ImageAdapter(CourseHandHomeWork.this);
        binding.rvImage.setAdapter(mAdapter);

        gridViewAdapter = new GridViewAdapter(this, dataList);
        binding.courseHomeworkGrid.setAdapter(gridViewAdapter);
        binding.courseHomeworkGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == parent.getChildCount() - 1) {
                    ImageSelector.builder()
                            .useCamera(true) // 设置是否使用拍照
                            .setSingle(false)  //设置是否单选
                            .setMaxSelectCount(0) // 图片的最大选择数量，小于等于0时，不限数量。
                            .start(CourseHandHomeWork.this, REQUEST_CODE); // 打开相册
                } else {

                }
            }
        });
    }

    public void initDialog(){
        selectDialog = new Dialog(CourseHandHomeWork.this);
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
                ImageSelector.builder()
                        .useCamera(true) // 设置是否使用拍照
                        .setSingle(false)  //设置是否单选
                        .setMaxSelectCount(0) // 图片的最大选择数量，小于等于0时，不限数量。
                        .start(CourseHandHomeWork.this, REQUEST_CODE); // 打开相册
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

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && data != null) {
            //dataList
            ArrayList<String> images = data.getStringArrayListExtra(ImageSelector.SELECT_RESULT);
            dataList.addAll(images);
            //mAdapter.refresh(dataList);
//            dataList.addAll(images);
//            gridViewAdapter.notifyDataSetChanged();
        }
    }

}
