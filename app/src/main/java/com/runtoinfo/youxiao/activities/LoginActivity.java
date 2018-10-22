package com.runtoinfo.youxiao.activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.google.gson.Gson;
import com.jaeger.library.StatusBarUtil;
import com.runtoinfo.httpUtils.HttpEntity;
import com.runtoinfo.httpUtils.bean.HttpLoginHead;
import com.runtoinfo.httpUtils.utils.HttpUtils;
import com.runtoinfo.youxiao.R;
import com.runtoinfo.youxiao.adapter.ListViewAdapter;
import com.runtoinfo.youxiao.globalTools.utils.DialogMessage;
import com.runtoinfo.youxiao.globalTools.utils.Entity;
import com.runtoinfo.youxiao.databinding.ActivityLoginBinding;
import com.runtoinfo.youxiao.entity.SelectSchoolEntity;
import com.runtoinfo.youxiao.globalTools.utils.IntentDataType;

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

@SuppressWarnings("all")
@Route(path = "/main/LoginActivity")
public class LoginActivity extends BaseActivity {

    public ActivityLoginBinding binding;
    public List<SelectSchoolEntity> schoolList = new ArrayList<>();//学校集合
    public ListViewAdapter mAdapter;
    public Map<String, List> dataMap = new HashMap<>();
    public HttpLoginHead loginHead;
    public ProgressDialog progressDialog = null;
    public Dialog dialog;
    public boolean isLoadUserName = true;
    public HttpUtils httpUtils;
    private long firstTime = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        //FitStatusUI.setImmersionStateMode(this);
        binding = DataBindingUtil.setContentView(LoginActivity.this, R.layout.activity_login);

        progressDialog = new ProgressDialog(LoginActivity.this);
        dialog = new Dialog(this, R.style.dialog);
        loginHead = new HttpLoginHead();
        httpUtils = new HttpUtils(this);
        initEvent();
    }

    @Override
    protected void initData() {
        setUserName();
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColor(this, 0xfcf5f7, 80);
    }

    public void initEvent() {
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
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                //if (binding.loginMobilePhone.getText().replaceAll())
                String text = binding.loginMobilePhone.getText().toString().replaceAll("\\s*", "");
                if (text.length() == 11) {
                    httpUtils.getAsy(mHandler, dataMap, HttpEntity.MAIN_URL + HttpEntity.GET_ORGANIZATION_INFO, text);
                    loginHead.setUserName(text);
                    spUtils.setString(com.runtoinfo.youxiao.globalTools.utils.Entity.PHONE_NUMBER, text);
                    binding.loginGetVerification.setBackgroundResource(R.drawable.background_verification_selected);
                    binding.loginGetVerification.setEnabled(true);
                } else {
                    binding.loginGetVerification.setBackgroundResource(R.drawable.background_verification_button);

                    binding.loginGetVerification.setEnabled(false);
                }

                if (binding.loginGetVerification.length() != 0 && binding.loginPassword.length() != 0) {
                    binding.loginBt.setEnabled(true);
                    binding.loginBt.setBackgroundResource(R.drawable.background_login_button_selected);
                } else {
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
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (binding.loginGetVerification.length() != 0 && binding.loginPassword.length() != 0) {
                    binding.loginBt.setEnabled(true);
                    binding.loginBt.setBackgroundResource(R.drawable.background_login_button_selected);
                } else {
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
//                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) binding.loginPassword.getLayoutParams();
//                params.addRule(RelativeLayout.START_OF, R.id.login_get_verification);
                binding.loginBt.setTag("VER_CODE");
                if (binding.loginPassword.length() > 0) {
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
//                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) binding.loginPassword.getLayoutParams();
//                params.addRule(RelativeLayout.START_OF, R.id.login_img_pw_vis);
                binding.loginBt.setTag("PASS_WORD");
                if (binding.loginPassword.length() > 0) {
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
                binding.loginGetVerification.setBackgroundResource(R.drawable.background_verification_wait);
                binding.loginGetVerification.setEnabled(false);
                timers();

                httpUtils.get(HttpEntity.MAIN_URL + HttpEntity.GET_CAPTION_CODE, binding.loginMobilePhone.getText().toString());
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
                String tag = binding.loginPassword.getTag().toString();
                switch (tag) {
                    case "PASSWORD_OFF":
                        binding.loginImgPwVis.setImageDrawable(getResources().getDrawable(R.drawable.login_password_on));
                        binding.loginPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        binding.loginPassword.setSelection(binding.loginPassword.getText().length());
                        binding.loginPassword.setTag("PASSWORD_ON");
                        break;
                    case "PASSWORD_ON":
                        binding.loginImgPwVis.setImageDrawable(getResources().getDrawable(R.drawable.login_password_off));
                        binding.loginPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        binding.loginPassword.setSelection(binding.loginPassword.getText().length());
                        binding.loginPassword.setTag("PASSWORD_OFF");
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

    /**
     * 将保存的用户名填入输入框
     */
    public void setUserName() {
        if (!TextUtils.isEmpty(spUtils.getString(Entity.PHONE_NUMBER))) {
            String phone = spUtils.getString(Entity.PHONE_NUMBER);
            binding.loginMobilePhone.setText(phone);
            isLoadUserName = false;
        }
    }


    /**
     * 选择的学校的实体
     *
     * @param dynamics
     */
    public void setSelectSchool(SelectSchoolEntity dynamics) {
        loginHead.setCampusId(dynamics.getId());
        spUtils.setInt(Entity.CAMPUS_ID, dynamics.getId());
        loginHead.setTenancyName(dynamics.getTenancyName());
        loginHead.setTenantId(dynamics.getTenantId());
        spUtils.setInt(Entity.TENANT_ID, Integer.parseInt(dynamics.getTenantId()));
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

        String schoolData = new Gson().toJson(schoolList);
        spUtils.setString(Entity.SCHOOL_DATA, schoolData);
        //mHandler.sendEmptyMessage(6);
    }

    /**
     * 计时器
     */
    public int time;
    private Timer timer = null;
    private TimerTask task = null;

    public void timers() {
        time = 30;
        timer = new Timer();
        task = new TimerTask() {
            @Override
            public void run() {
                time--;
                if (time == 0) {
                    mHandler.sendEmptyMessage(2);
                } else {
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
    public Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    switch (binding.loginBt.getTag().toString()) {
                        case "PASS_WORD":

                            httpUtils.post(mHandler, HttpEntity.MAIN_URL + HttpEntity.LOGIN_URL_AUTH, loginHead);
                            break;
                        case "VER_CODE":
                            httpUtils.postCaptcha(mHandler, HttpEntity.MAIN_URL + HttpEntity.LOGIN_URL_CAPTCHA, loginHead);
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
                case 3://选择完成学校进行跳转
                    String response = msg.obj.toString();
                    if (response != null && !response.equals("null")) {
                        getToken(response);
                    }
                    break;
                case 4:
                    if (progressDialog != null) progressDialog.cancel();
                    Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                    break;
                case 5:
                    initSelectSchoolData();
                    if (progressDialog != null) progressDialog.dismiss();
                    break;
                case 6:
                    if (dialog != null)
                        DialogMessage.showLoading(LoginActivity.this, dialog, false);
                    String json = new Gson().toJson(schoolList);
                    break;
                case 400:
                    String result = msg.obj.toString();
                    if (progressDialog != null) progressDialog.dismiss();
                    DialogMessage.showToast(LoginActivity.this, result);
            }
        }
    };

    public void getToken(final String json) {
        HttpUtils.executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject J = new JSONObject(json);
                    Log.e("LoginUserId", json.toString());
                    String token = J.getString("accessToken");
                    spUtils.setString(Entity.TOKEN, token);
                    spUtils.setInt(Entity.USER_ID, J.getInt("userId"));
                    loginHead.setToken(token);

                    LoginActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (progressDialog != null) progressDialog.cancel();
                            String json = new Gson().toJson(schoolList);
                            ARouter.getInstance().build(Entity.MAIN_ACTIVITY_PATH).withString(IntentDataType.DATA, json).navigation();
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!TextUtils.isEmpty(spUtils.getString(Entity.TOKEN)))
            ARouter.getInstance().build(Entity.MAIN_ACTIVITY_PATH).navigation();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isLoadUserName)
            setUserName();
    }

    @Override
    public void onPause() {
        super.onPause();
        isLoadUserName = true;
    }

    @Override
    public void onBackPressed() {
        long secondTime = System.currentTimeMillis();
        if (secondTime - firstTime < 1000) {
            finished();
        } else {
            Toast.makeText(LoginActivity.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            firstTime = System.currentTimeMillis();
        }
    }
}
