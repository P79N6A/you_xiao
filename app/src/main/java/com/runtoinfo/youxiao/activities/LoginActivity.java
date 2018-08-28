package com.runtoinfo.youxiao.activities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.alibaba.android.arouter.launcher.ARouter;
import com.google.gson.Gson;
import com.runtoinfo.teacher.HttpEntity;
import com.runtoinfo.teacher.bean.HttpLoginHead;
import com.runtoinfo.teacher.utils.HttpUtils;
import com.runtoinfo.youxiao.R;
import com.runtoinfo.youxiao.adapter.ListViewAdapter;
import com.runtoinfo.youxiao.globalTools.utils.DialogMessage;
import com.runtoinfo.youxiao.globalTools.utils.Entity;
import com.runtoinfo.youxiao.databinding.ActivityLoginBinding;
import com.runtoinfo.youxiao.entity.SelectSchoolEntity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Response;


public class LoginActivity extends BaseActivity {

    public ActivityLoginBinding binding;
    public List<SelectSchoolEntity> schoolList = new ArrayList<>();//学校集合
    public ListViewAdapter mAdapter;
    public Map<String, List> dataMap = new HashMap<>();
    public HttpLoginHead loginHead;
    public ProgressDialog progressDialog = null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        //FitStatusUI.setImmersionStateMode(this);
        binding = DataBindingUtil.setContentView(LoginActivity.this, R.layout.activity_login);
        progressDialog = new ProgressDialog(LoginActivity.this);
        loginHead = new HttpLoginHead();
        initEvent();
    }

    @Override
    protected void initData() {
        setUserName();
    }

    public void initEvent(){
        //登录
        binding.loginBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //createDialog();
                switch (schoolList.size()) {
                    case 1:
                        setSelectSchool(schoolList.get(0));
                        break;
                    case 0:
                        Toast.makeText(LoginActivity.this, "账号或密码不正确", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        binding.loginDefaultNew.setVisibility(View.GONE);
                        binding.loginInclude.setVisibility(View.VISIBLE);
                        break;

                }
            }
        });

        /**
         * 账号输入监听
         */
        binding.loginMobilePhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                //if (binding.loginMobilePhone.getText().replaceAll())
                String text = binding.loginMobilePhone.getText().toString().replaceAll("\\s*", "");
                if (text.length() == 11)
                {
                    HttpUtils.getAsy(mHandler, dataMap, HttpEntity.MAIN_URL + HttpEntity.GET_ORGANIZATION_INFO, text);
                    loginHead.setUserName(text);
                    spUtils.setString(com.runtoinfo.youxiao.globalTools.utils.Entity.PHONE_NUMBER, text);
                    binding.loginGetVerification.setBackgroundResource(R.drawable.background_verification_selected);
                    binding.loginGetVerification.setEnabled(true);
                }
                else
                {
                    binding.loginGetVerification.setBackgroundResource(R.drawable.background_verification_button);

                    binding.loginGetVerification.setEnabled(false);
                }

                if (binding.loginGetVerification.length() != 0 && binding.loginPassword.length() != 0)
                {
                    binding.loginBt.setEnabled(true);
                    binding.loginBt.setBackgroundResource(R.drawable.background_login_button_selected);
                }
                else
                {
                    binding.loginBt.setEnabled(false);
                    binding.loginBt.setBackgroundResource(R.drawable.background_login_button);
                }
            }
        });

        /**
         * 密码输入监听
         */
        binding.loginPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (binding.loginGetVerification.length() != 0 && binding.loginPassword.length() != 0)
                {
                    binding.loginBt.setEnabled(true);
                    binding.loginBt.setBackgroundResource(R.drawable.background_login_button_selected);
                }
                else
                {
                    binding.loginBt.setEnabled(false);
                    binding.loginBt.setBackgroundResource(R.drawable.background_login_button);
                }
                loginHead.setPassWord(binding.loginPassword.getText().toString());
            }
        });

        /**
         * 切换为验证码登录
         */
        binding.loginVerificationCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.loginVerificationCode.setVisibility(View.GONE);
                binding.passwordLogin.setVisibility(View.VISIBLE);
                binding.loginGetVerification.setVisibility(View.VISIBLE);
                binding.loginImgPwVis.setVisibility(View.GONE);
                binding.loginBt.setTag("VER_CODE");
                if (binding.loginPassword.length() > 0)
                {
                    binding.loginPassword.setText("");
                }
            }
        });

        /**
         * 切换为密码登录
         */
        binding.passwordLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.passwordLogin.setVisibility(View.GONE);
                binding.loginVerificationCode.setVisibility(View.VISIBLE);
                binding.loginGetVerification.setVisibility(View.GONE);
                binding.loginImgPwVis.setVisibility(View.VISIBLE);
                binding.loginBt.setTag("PASS_WORD");
                if (binding.loginPassword.length() > 0)
                {
                    binding.loginPassword.setText("");
                }
            }
        });

        /**
         * 获取验证码
         */
        binding.loginGetVerification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.loginGetVerification.setBackgroundResource(R.color.color_gray);
                binding.loginGetVerification.setEnabled(false);
                timers();

                HttpUtils.get(HttpEntity.MAIN_URL + HttpEntity.GET_CAPTION_CODE, binding.loginMobilePhone.getText().toString());
            }
        });

        /**
         * 选择学校
         */
        binding.loginSelectLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //填入登录需要的参数
                mAdapter.setIndex(position);
                mAdapter.notifyDataSetChanged();
                setSelectSchool(schoolList.get(position));
                binding.loginDefaultNew.setVisibility(View.VISIBLE);
                binding.loginInclude.setVisibility(View.GONE);
            }
        });

        /**
         * 回退
         */
        binding.loginBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.loginInclude.setVisibility(View.GONE);
                binding.loginDefaultNew.setVisibility(View.VISIBLE);
            }
        });

        /**
         * 密码显示与隐藏
         */
        binding.loginImgPwVis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (binding.loginPassword.getTag().toString()){
                    case "PASSWORD_OFF":
                        binding.loginImgPwVis.setImageDrawable(getResources().getDrawable(R.drawable.login_password_off));
                        binding.loginPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        binding.loginPassword.setSelection(binding.loginPassword.getText().length());
                        binding.loginPassword.setTag("PASSWORD_OFF");
                        break;
                    case "PASSWORD_ON":
                        binding.loginImgPwVis.setImageDrawable(getResources().getDrawable(R.drawable.login_password_on));
                        binding.loginPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        binding.loginPassword.setSelection(binding.loginPassword.getText().length());
                        binding.loginPassword.setTag("PASSWORD_ON");
                        break;
                }
            }
        });
        /**
         * 忘记密码
         */
        binding.loginForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ARouter.getInstance().build(Entity.REST_PASS_WORD).withString("LoginHttpHead", new Gson().toJson(loginHead)).navigation();
            }
        });
    }

    public void setUserName(){
        //
        if (!TextUtils.isEmpty(spUtils.getString(com.runtoinfo.youxiao.globalTools.utils.Entity.PHONE_NUMBER))){
            String phone = spUtils.getString(com.runtoinfo.youxiao.globalTools.utils.Entity.PHONE_NUMBER);
            binding.loginMobilePhone.setText(phone);
            if (phone.length() == 11){
                HttpUtils.getAsy(mHandler, dataMap, HttpEntity.MAIN_URL + HttpEntity.GET_ORGANIZATION_INFO, phone);
                loginHead.setUserName(phone);
                binding.loginGetVerification.setBackgroundResource(R.drawable.background_verification_selected);
                binding.loginGetVerification.setEnabled(true);
            }
        }
    }


    /**
     * 选择的学校的实体
     * @param dynamics
     */
    public void setSelectSchool(SelectSchoolEntity dynamics){
        loginHead.setCampusId(dynamics.getId());
        loginHead.setTenancyName(dynamics.getTenancyName());
        loginHead.setTenantId(dynamics.getTenantId());
        DialogMessage.createDialog(LoginActivity.this, progressDialog, "正在接收数据，请稍后...");
        mHandler.sendEmptyMessage(0);
    }

    public void initSelectSchoolData() {
        schoolList.clear();
        Log.e("SIZE", dataMap.size() + "");
        if (dataMap != null && dataMap.size() > 0) {
            List<List<String>> school = dataMap.get("school");
            List orgList = dataMap.get("org");
            List head = dataMap.get("head");
            List img = dataMap.get("img");
            for (int i = 0; i < orgList.size(); i++) {
                SelectSchoolEntity schoolEntity = new SelectSchoolEntity();
                schoolEntity.setSchoolName(school.get(i));
                schoolEntity.setOrgName((String) orgList.get(i));
                schoolEntity.setId(((HttpLoginHead) head.get(i)).getCampusId());
                schoolEntity.setTenancyName(((HttpLoginHead) head.get(i)).getTenancyName());
                schoolEntity.setTenantId(((HttpLoginHead) head.get(i)).getTenantId());
                schoolEntity.setImgPath((String) img.get(i));
                schoolList.add(schoolEntity);
            }
        }
        mAdapter = new ListViewAdapter(this, schoolList);
        binding.loginSelectLv.setAdapter(mAdapter);
    }

    /**
     * 计时器
     */
    public int time;
    private Timer timer = null;
    private TimerTask task = null;

    public void timers(){
        time = 30;
        timer = new Timer();
        task = new TimerTask() {
            @Override
            public void run() {
                time--;
                if (time == 0)
                {
                    mHandler.sendEmptyMessage(2);
                }
                else
                {
                    Message msg = new Message();
                    msg.what = 1;
                    msg.obj = time + "";
                    mHandler.sendMessage(msg);
                }
            }
        };
        timer.schedule(task, 1000, 1000);
    }

    @SuppressLint("HandlerLeak")
    public Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what)
            {
                case 0:
                    switch (binding.loginBt.getTag().toString()){
                        case "PASS_WORD":
                            HttpUtils.post(mHandler,HttpEntity.MAIN_URL + HttpEntity.LOGIN_URL_AUTH, loginHead);
                            break;
                        case "VER_CODE":
                            HttpUtils.postCaptcha(mHandler, HttpEntity.MAIN_URL + HttpEntity.LOGIN_URL_CAPTCHA, loginHead);
                            break;
                    }

                    break;
                case 1:
                    binding.loginGetVerification.setText(time + "s");
                    break;
                case 2:
                    timer.cancel();
                    task.cancel();
                    binding.loginGetVerification.setText("重新获取");
                    binding.loginGetVerification.setEnabled(true);
                    binding.loginGetVerification.setBackgroundResource(R.drawable.background_verification_selected);
                    break;
                case 3:
                    Response response = (Response) msg.obj;
                    if (response != null && response.code() == 200){
                        getToken(response);
                        String json = new Gson().toJson(schoolList);
                        ARouter.getInstance().build(Entity.MAIN_ACTIVITY_PATH).navigation();
                    }
                    break;
                case 4:
                    if (progressDialog != null) progressDialog.cancel();
                    Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                    break;
                case 5:
                    initSelectSchoolData();

                    if(progressDialog != null) progressDialog.dismiss();
                    break;

            }
        }
    };

    public void getToken(final Response response){
        HttpUtils.executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject J = new JSONObject(response.body().string());
                    JSONObject json = J.getJSONObject("result");
                    String token = json.getString("accessToken");
                    spUtils.setString(Entity.TOKEN, token);
                    spUtils.setString(Entity.USER_ID, json.getString("userId"));
                    loginHead.setToken(token);

                    LoginActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (progressDialog != null) progressDialog.cancel();
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        String isLoged = spUtils.getString(Entity.TOKEN);
        if (!TextUtils.isEmpty(isLoged)){
            ARouter.getInstance().build(Entity.MAIN_ACTIVITY_PATH).navigation();
        }
    }
}
