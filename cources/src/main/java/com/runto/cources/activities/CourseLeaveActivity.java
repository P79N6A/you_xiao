package com.runto.cources.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.runto.cources.R;
import com.runto.cources.databinding.FragmentCourseLeaveBinding;
import com.runtoinfo.youxiao.globalTools.timepicker.DatePickerView;

import java.util.ArrayList;

@Route(path = "/course/leaveActivity")
public class CourseLeaveActivity extends Activity {

    FragmentCourseLeaveBinding binding;
    private Dialog dialog;
    private DatePickerView leave_code;
    private EditText leave_comment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.fragment_course_leave);
        binding = DataBindingUtil.setContentView(this, R.layout.fragment_course_leave);
        initDialog();
        initView();
    }

    public void initView(){

        binding.courseLeaveSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });

        leave_comment = findViewById(R.id.course_leave_comment);


        binding.courseLeaveBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void initDialog(){
        dialog = new Dialog(this, R.style.time_dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.leave_comment_select);
        dialog.setCancelable(false);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        WindowManager manager = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(dm);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = dm.widthPixels;
        window.setAttributes(lp);

        initDialogView();
    }

    public void initDialogView(){

        leave_code =dialog.findViewById(R.id.leave_select_picker);
        ArrayList<String> codeList = new ArrayList<String>();
        codeList.add("今天家里有事");
        codeList.add("身体不舒服");
        codeList.add("就想请个假");
        codeList.add("其他");

        leave_code.setData(codeList);

        leave_code.setCanScroll(true);
        leave_code.setSelected(0);
        leave_code.setIsLoop(false);
        leave_code.setOnSelectListener(new DatePickerView.onSelectListener() {
            @Override
            public void onSelect(String text) {
                leave_comment.setText(text);
            }
        });

        dialog.findViewById(R.id.dialog_leave_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                dialog.dismiss();
            }
        });

        dialog.findViewById(R.id.dialog_leave_finish).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //leave_code.
                dialog.dismiss();
            }
        });
    }
}
