package com.runtoinfo.youxiao.activities;

import android.annotation.SuppressLint;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.AdapterView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.runtoinfo.youxiao.R;
import com.runtoinfo.youxiao.adapter.ListViewAdapter;
import com.runtoinfo.youxiao.databinding.ActivityLoginBinding;
import com.runtoinfo.youxiao.databinding.LoginSelectSchoolBinding;
import com.runtoinfo.youxiao.entity.SelectSchoolEntity;
import com.runtoinfo.youxiao.utils.Entity;
import com.runtoinfo.youxiao.utils.SPUtils;

import java.util.ArrayList;
import java.util.IllegalFormatCodePointException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class LoginActivity extends BaseActivity {

    public ActivityLoginBinding binding;
    public List<SelectSchoolEntity> schoolList = new ArrayList<>();//学校集合
    public ListViewAdapter mAdapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {

        binding = DataBindingUtil.setContentView(LoginActivity.this, R.layout.activity_login);

        //登录
        binding.loginBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (schoolList.size()) {
                    case 1:
                        ARouter.getInstance().build(Entity.MAIN_ACTIVITY_PATH).navigation();
                        break;
                        default:
                            initSelectSchoolData();
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
            }
        });

        /**
         * 切换验证码登录
         */
        binding.loginVerificationCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.loginVerificationCode.setVisibility(View.GONE);
                binding.passwordLogin.setVisibility(View.VISIBLE);
                binding.loginGetVerification.setVisibility(View.VISIBLE);
                binding.loginImgPwVis.setVisibility(View.GONE);
                if (binding.loginPassword.length() > 0)
                {
                    binding.loginPassword.setText("");
                }
            }
        });

        /**
         * 切换密码登录
         */
        binding.passwordLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.passwordLogin.setVisibility(View.GONE);
                binding.loginVerificationCode.setVisibility(View.VISIBLE);
                binding.loginGetVerification.setVisibility(View.GONE);
                binding.loginImgPwVis.setVisibility(View.VISIBLE);
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
            }
        });

        /**
         * 选择学校
         */
        binding.loginSelectLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ARouter.getInstance().build(Entity.MAIN_ACTIVITY_PATH).navigation();
                binding.loginDefaultNew.setVisibility(View.VISIBLE);
                binding.loginInclude.setVisibility(View.GONE);
                SPUtils.setBoolean("LOGIN_ON", true);
                LoginActivity.this.finish();
            }
        });

        binding.loginBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.loginInclude.setVisibility(View.GONE);
                binding.loginDefaultNew.setVisibility(View.VISIBLE);
            }
        });

        binding.loginImgPwVis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (binding.loginPassword.getTag().toString()){
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
    }

    @Override
    protected void initData() {

    }

    public void initSelectSchoolData() {
        schoolList.clear();
        for (int i = 0; i < 4; i++){
            SelectSchoolEntity schoolEntity = new SelectSchoolEntity();
            schoolEntity.setSchoolName("育雅学堂");
            schoolEntity.setDrawable(getResources().getDrawable(R.drawable.login_school_logo));
            schoolList.add(schoolEntity);
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
                case 1:
                    binding.loginGetVerification.setText(time + "s");
                    break;
                case 2:
                    timer.cancel();
                    task.cancel();
                    binding.loginGetVerification.setText("重新获取");
                    binding.loginGetVerification.setEnabled(true);
                    binding.loginGetVerification.setBackgroundResource(R.color.color_blue);
                    break;
            }
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        boolean isLoged = SPUtils.getBoolean("LOGIN_ON");
        if (isLoged){
            ARouter.getInstance().build(Entity.MAIN_ACTIVITY_PATH).navigation();
        }
    }
}
