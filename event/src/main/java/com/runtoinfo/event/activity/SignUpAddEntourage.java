package com.runtoinfo.event.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Layout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.runtoinfo.event.R;
import com.runtoinfo.event.adapter.EventAddMemberAdapter;
import com.runtoinfo.event.databinding.ActivityAddEntourageBinding;
import com.runtoinfo.event.databinding.ActivityAddItemLayoutBinding;
import com.runtoinfo.event.dialog.SignUpSuccess;
import com.runtoinfo.event.entity.AddMemberEntity;
import com.runtoinfo.teacher.HttpEntity;
import com.runtoinfo.teacher.bean.AddMemberBean;
import com.runtoinfo.teacher.utils.HttpUtils;
import com.runtoinfo.youxiao.common_ui.utils.DialogMessage;
import com.runtoinfo.youxiao.common_ui.utils.Entity;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

@Route(path = "/event/signUpAddMember")
public class SignUpAddEntourage extends EventBaseActivity {

    public ActivityAddEntourageBinding binding;
    public List<AddMemberBean> dataList = new ArrayList<>();
    public int index = 0, upIndex = 0;
    public LayoutInflater inflate;
    public ProgressDialog progressDialog;
    public SignUpSuccess signUpSuccess;
    public EventAddMemberAdapter adapter;
    public AddMemberBean intentBean;

    public void initView(){
        binding = DataBindingUtil.setContentView( SignUpAddEntourage.this, R.layout.activity_add_entourage);
    }

    @Override
    protected void initData() {
        intentBean =new Gson().fromJson( getIntent().getExtras().getString("json"), new TypeToken<AddMemberBean>(){}.getType());
        //添加一个
        dataList = new ArrayList<>();
        dataList.add(new AddMemberBean());
        adapter = new EventAddMemberAdapter(SignUpAddEntourage.this, dataList, R.layout.activity_add_item_layout);
        binding.addMemberRecyclerView.setHasFixedSize(false);
        binding.addMemberRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.addMemberRecyclerView.setAdapter(adapter);
        progressDialog = new ProgressDialog(this);
        signUpSuccess = new SignUpSuccess(SignUpAddEntourage.this);
    }

    public void initEvent(){
        binding.activitySignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (adapter.getList().size() == 1) {
                    AddMemberBean bean = adapter.getList().get(0);
                    if (TextUtils.isEmpty(bean.getMemberType()) || TextUtils.isEmpty(bean.getPhoneNumber()) || TextUtils.isEmpty(bean.getName())) {
                        Toast.makeText(SignUpAddEntourage.this, "请完善随行人员信息", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                DialogMessage.createDialog(SignUpAddEntourage.this, progressDialog, "正在上传信息...");
                HttpUtils.postAddMember(handler, HttpEntity.MAIN_URL + HttpEntity.CAMPAIGN_ADD_MEMBER, intentBean, spUtils.getString(Entity.TOKEN));
            }
        });

        binding.activityAddAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddMemberBean addMemberBean =new AddMemberBean();
                index++;
                adapter.addItem(addMemberBean, index);
            }
        });
    }


    @SuppressLint("HandlerLeak")
    public Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    int size = adapter.getList().size();
                    if (upIndex == size) {
                        progressDialog.dismiss();
                        signUpSuccess.show();
                        upIndex = 0;
                        index = 0;
                    } else {
                        HttpUtils.postAddMember(handler, HttpEntity.MAIN_URL + HttpEntity.CAMPAIGN_ADD_MEMBER, adapter.getList().get(upIndex), spUtils.getString(Entity.TOKEN));
                        upIndex++;
                    }
                    break;
                case 404:
                    if (progressDialog.isShowing()){
                        progressDialog.setMessage("报名失败，请重新报名");
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                progressDialog.dismiss();
                            }
                        }, 1000);
                    }
                    break;
            }
        }
    };

}
