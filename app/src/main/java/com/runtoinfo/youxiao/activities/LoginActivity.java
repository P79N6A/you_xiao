package com.runtoinfo.youxiao.activities;

import android.annotation.SuppressLint;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.alibaba.android.arouter.launcher.ARouter;
import com.runtoinfo.youxiao.R;
import com.runtoinfo.youxiao.databinding.ActivityLoginBinding;

import java.util.Timer;
import java.util.TimerTask;


public class LoginActivity extends BaseActivity {

    public ActivityLoginBinding binding;
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
        binding.loginBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build("/main/mainAcitivity").navigation();
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
    }

    @Override
    protected void initData() {

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
}
