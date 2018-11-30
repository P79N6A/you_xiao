package com.runtoinfo.event.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.databinding.DataBindingUtil;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.runtoinfo.event.R;
import com.runtoinfo.event.adapter.EventAddMemberAdapter;
import com.runtoinfo.event.databinding.ActivityAddEntourageBinding;
import com.runtoinfo.event.dialog.SignUpSuccess;
import com.runtoinfo.httpUtils.HttpEntity;
import com.runtoinfo.httpUtils.bean.AddMemberBean;
import com.runtoinfo.httpUtils.bean.EventAddResultBean;
import com.runtoinfo.httpUtils.bean.RequestDataEntity;
import com.runtoinfo.httpUtils.utils.HttpUtils;
import com.runtoinfo.youxiao.globalTools.utils.DialogMessage;
import com.runtoinfo.youxiao.globalTools.utils.Entity;
import com.runtoinfo.youxiao.globalTools.utils.IntentDataType;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Route(path = "/event/signUpAddMember")
public class SignUpAddEntourage extends EventBaseActivity {

    public ActivityAddEntourageBinding binding;
    public List<AddMemberBean> dataList = new LinkedList<>();
    public LayoutInflater inflate;
    public ProgressDialog progressDialog;
    public SignUpSuccess signUpSuccess;
    public EventAddMemberAdapter adapter;
    //public int campaignId;
    public RequestDataEntity requestDataEntity;
    public HttpUtils httpUtils;
    public EventAddResultBean resultBean;

    public void initView(){
        binding = DataBindingUtil.setContentView( SignUpAddEntourage.this, R.layout.activity_add_entourage);
        setStatusBar(binding.eventAddLayout);
        httpUtils = new HttpUtils(this);
        requestDataEntity = new RequestDataEntity();
        requestDataEntity.setUrl(HttpEntity.MAIN_URL + HttpEntity.CAMPAIGN_ADD_MEMBER);
        requestDataEntity.setToken(spUtils.getString(Entity.TOKEN));
    }

    @Override
    protected void initData() {
        //campaignId = getIntent().getExtras().getInt(IntentDataType.DATA);
        String data = getIntent().getExtras().getString(IntentDataType.DATA);
        resultBean = new Gson().fromJson(data, new TypeToken<EventAddResultBean>(){}.getType());
        //添加一个
        dataList = new ArrayList<>();
        dataList.add(new AddMemberBean());
        adapter = new EventAddMemberAdapter(SignUpAddEntourage.this, dataList, R.layout.activity_add_item_layout);
        binding.addMemberRecyclerView.setHasFixedSize(false);
        binding.addMemberRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter.setHasStableIds(true);
        binding.addMemberRecyclerView.setAdapter(adapter);
        progressDialog = new ProgressDialog(this);
        signUpSuccess = new SignUpSuccess(SignUpAddEntourage.this);
    }

    public void initEvent(){
        binding.activitySignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               upLoadMember(3);
            }
        });

        binding.activityAddAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upLoadMember(2);
                dataList.add(new AddMemberBean());
                adapter.notifyDataSetChanged();
            }
        });

        binding.activityImgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void upLoadMember(int requestType){
        AddMemberBean bean = adapter.getList().get(adapter.getItemCount() - 1);
        bean.setParentId(String.valueOf(resultBean.getId()));
        bean.setCampaignId(resultBean.getCampusId());
        bean.setUserId(resultBean.getUserId());
        if (TextUtils.isEmpty(bean.getMemberType()) || TextUtils.isEmpty(bean.getPhoneNumber()) || TextUtils.isEmpty(bean.getName())) {
            Toast.makeText(SignUpAddEntourage.this, "请完善随行人员信息", Toast.LENGTH_SHORT).show();
            return;
        }
        DialogMessage.createDialog(SignUpAddEntourage.this, progressDialog, "正在上传信息...");
        httpUtils.postAddMember(handler, requestDataEntity, bean, requestType);
    }


    @SuppressLint("HandlerLeak")
    public Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    progressDialog.dismiss();

                    break;
                case 3:
                    if (progressDialog != null){
                        progressDialog.dismiss();
                        signUpSuccess.show();
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
