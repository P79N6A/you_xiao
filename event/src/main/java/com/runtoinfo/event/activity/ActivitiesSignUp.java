package com.runtoinfo.event.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.databinding.DataBindingUtil;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.alibaba.android.arouter.launcher.ARouter;
import com.google.gson.Gson;
import com.runtoinfo.event.R;
import com.runtoinfo.event.databinding.ActivitySiginUpBinding;
import com.runtoinfo.event.dialog.SignUpSuccess;
import com.runtoinfo.teacher.HttpEntity;
import com.runtoinfo.teacher.bean.AddMemberBean;
import com.runtoinfo.teacher.utils.HttpUtils;
import com.runtoinfo.youxiao.common_ui.utils.Entity;

public class ActivitiesSignUp extends EventBaseActivity {

    ActivitySiginUpBinding binding;
    ProgressDialog progressDialog;
    public AddMemberBean addMember;
    public String gender;

    @Override
    protected void initView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sigin_up);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initEvent() {
        binding.activitySignAlone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEmpty()) {
                    addMember = new AddMemberBean();
                    addMember.setAge(binding.activityMemberAge.getText().toString());
                    addMember.setName(binding.eventStudentName.getText().toString());
                    addMember.setPhoneNumber(binding.memberPhoneNumber.getText().toString());
                    addMember.setUserId(spUtils.getString(com.runtoinfo.youxiao.common_ui.utils.Entity.USER_ID));
                    addMember.setGender(gender);
                    HttpUtils.postAddMember(mHandler, HttpEntity.MAIN_URL + HttpEntity.CAMPAIGN_ADD_MEMBER, addMember, spUtils.getString(Entity.TOKEN));
                }else{
                    Toast.makeText(ActivitiesSignUp.this, "请完善报名信息", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.activityAddFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEmpty()) {
                    addMember = new AddMemberBean();
                    addMember.setAge(binding.activityMemberAge.getText().toString());
                    addMember.setName(binding.eventStudentName.getText().toString());
                    addMember.setPhoneNumber(binding.memberPhoneNumber.getText().toString());
                    addMember.setUserId(spUtils.getString(Entity.USER_ID));
                    addMember.setGender(gender);
                    String json = new Gson().toJson(addMember);
                    ARouter.getInstance().build("/event/signUpAddMember").withString("json", json).navigation();
                }else{
                    Toast.makeText(ActivitiesSignUp.this, "请完善报名信息", Toast.LENGTH_SHORT).show();
                }
            }
        });
        binding.activityImgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @SuppressLint("HandlerLeak")
    public Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    SignUpSuccess signUpSuccess = new SignUpSuccess(ActivitiesSignUp.this);
                    signUpSuccess.show();
                    break;
                case 404:
                    Toast.makeText(ActivitiesSignUp.this, "请求错误", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    public boolean isEmpty(){
        gender = binding.memberMan.isChecked() ? "0" : (binding.memberWomen.isChecked() ? "1" : null);
        if (binding.activityMemberAge.length() > 0
                && binding.eventStudentName.length() > 0
                && binding.memberPhoneNumber.length() > 0 && !TextUtils.isEmpty(gender)){
            return true;
        }
        return false;
    }
}
