package com.runtoinfo.event.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.databinding.DataBindingUtil;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.google.gson.Gson;
import com.runtoinfo.event.R;
import com.runtoinfo.event.databinding.ActivitySiginUpBinding;
import com.runtoinfo.event.dialog.SignUpSuccess;
import com.runtoinfo.httpUtils.HttpEntity;
import com.runtoinfo.httpUtils.bean.AddMemberBean;
import com.runtoinfo.httpUtils.bean.RequestDataEntity;
import com.runtoinfo.httpUtils.utils.HttpUtils;
import com.runtoinfo.youxiao.globalTools.utils.Entity;
import com.runtoinfo.youxiao.globalTools.utils.IntentDataType;

@Route(path = "/event/activitySignUp")
public class ActivitiesSignUp extends EventBaseActivity {

    ActivitySiginUpBinding binding;
    ProgressDialog progressDialog;
    public AddMemberBean addMember;
    public String gender;
    public RequestDataEntity requestDataEntity;
    public int requestType;
    public int campaignId;

    @Override
    protected void initView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sigin_up);
        campaignId = getIntent().getExtras().getInt(IntentDataType.DATA);
        requestDataEntity = new RequestDataEntity();
        requestDataEntity.setToken(spUtils.getString(Entity.TOKEN));
        requestDataEntity.setUrl(HttpEntity.MAIN_URL + HttpEntity.CAMPAIGN_ADD_MEMBER);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initEvent() {
        binding.activitySignAlone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestType = 0;
                addDataAndRequest();
            }
        });

        binding.activityAddFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestType = 1;
                addDataAndRequest();

            }
        });
        binding.activityImgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void addDataAndRequest(){
        if (isEmpty()) {
            addMember = new AddMemberBean();
            addMember.setAge(binding.activityMemberAge.getText().toString());
            addMember.setName(binding.eventStudentName.getText().toString());
            addMember.setPhoneNumber(binding.memberPhoneNumber.getText().toString());
            addMember.setCampaignId(campaignId);
            addMember.setUserId(spUtils.getInt(Entity.USER_ID));
            addMember.setGender(gender);
            HttpUtils.postAddMember(mHandler, requestDataEntity,  addMember, requestType);

        }else{
            Toast.makeText(ActivitiesSignUp.this, "请完善报名信息", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("HandlerLeak")
    public Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    if (requestType == 0) {
                        SignUpSuccess signUpSuccess = new SignUpSuccess(ActivitiesSignUp.this);
                        signUpSuccess.show();
                    }else{
                        ARouter.getInstance().build("/event/signUpAddMember").withInt(IntentDataType.DATA, (int) msg.obj).navigation();
                    }
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
