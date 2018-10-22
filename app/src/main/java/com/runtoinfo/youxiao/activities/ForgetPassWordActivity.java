package com.runtoinfo.youxiao.activities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.databinding.DataBindingUtil;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jaeger.library.StatusBarUtil;
import com.runtoinfo.httpUtils.HttpEntity;
import com.runtoinfo.httpUtils.bean.HttpLoginHead;
import com.runtoinfo.httpUtils.utils.HttpUtils;
import com.runtoinfo.youxiao.R;
import com.runtoinfo.youxiao.globalTools.utils.Entity;
import com.runtoinfo.youxiao.databinding.LoginResetPasswordBinding;
import com.runtoinfo.youxiao.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

@Route(path = "/main/resetPassword")
public class ForgetPassWordActivity extends BaseActivity {

    LoginResetPasswordBinding binding;
    HttpLoginHead head;
    ProgressDialog dialog;
    boolean isSuccess = false;
    public HttpUtils httpUtils;

    @Override
    protected void initView() {
        binding = DataBindingUtil.setContentView( this,R.layout.login_reset_password);
        StatusBarUtil.setTranslucent(this);
        httpUtils = new HttpUtils(this);
        String json = getIntent().getExtras().getString("LoginHttpHead");
        head = new Gson().fromJson(json, new TypeToken<HttpLoginHead>(){}.getType());
        dialog = new ProgressDialog(this);
        initEvent();
    }

    @Override
    protected void initData() {

    }

    public void initEvent(){
        //获取验证码
        binding.resetGetVerificationCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.resetGetVerificationCode.setBackgroundResource(R.drawable.background_verification_wait);
                binding.resetGetVerificationCode.setEnabled(false);
                spUtils.setString(Entity.PHONE_NUMBER, binding.resetPasswordPhone.getText().toString());
                timers();
                httpUtils.get(HttpEntity.MAIN_URL + HttpEntity.GET_CAPTION_CODE, binding.resetPasswordPhone.getText().toString());
            }
        });

        binding.resetPasswordVerificationCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (binding.resetPasswordVerificationCode.length() == 4){
                    Utils.creatDialog(dialog);
                    httpUtils.postValidate(handler, HttpEntity.MAIN_URL + HttpEntity.CAPTCHA_VALIDATE,
                            binding.resetPasswordPhone.getText().toString(), binding.resetPasswordVerificationCode.getText().toString());
                }
            }
        });

        binding.resetPasswordPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (binding.resetPasswordPhone.length() == 11){
                    binding.resetGetVerificationCode.setEnabled(true);
                    binding.resetGetVerificationCode.setBackground(getResources().getDrawable(R.drawable.background_verification_selected));
                }else{
                    binding.resetGetVerificationCode.setEnabled(false);
                    binding.resetGetVerificationCode.setBackground(getResources().getDrawable(R.drawable.background_verification_button));
                }
            }
        });

        binding.resetPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (binding.resetPasswordAgain.length() > 0) {
                    if (!binding.resetPasswordAgain.getText().toString().equals(binding.resetPassword.getText().toString())) {
                        Toast.makeText(ForgetPassWordActivity.this, "输入的密码不一致", Toast.LENGTH_SHORT).show();
                        binding.resetPasswordButton.setEnabled(false);
                        binding.resetPasswordButton.setBackgroundResource(R.drawable.background_login_button);
                    }else{
                        binding.resetPasswordButton.setEnabled(true);
                        binding.resetPasswordButton.setBackgroundResource(R.drawable.background_login_button_selected);
                    }
                }
            }
        });

        binding.resetPasswordAgain.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (binding.resetPassword.length() > 0) {
                    if (!binding.resetPasswordAgain.getText().toString().equals(binding.resetPassword.getText().toString())) {
                        Toast.makeText(ForgetPassWordActivity.this, "输入的密码不一致", Toast.LENGTH_SHORT).show();
                        binding.resetPasswordButton.setEnabled(false);
                        binding.resetPasswordButton.setBackgroundResource(R.drawable.background_login_button);
                    }else{
                        binding.resetPasswordButton.setEnabled(true);
                        binding.resetPasswordButton.setBackgroundResource(R.drawable.background_login_button_selected);
                    }
                }
            }
        });

        binding.resetPassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (binding.resetPasswordVerificationCode.length() == 0){
                    Toast.makeText(ForgetPassWordActivity.this, "请输入验证码", Toast.LENGTH_SHORT).show();
                    return true;
                }
                if (!isSuccess){
                    Toast.makeText(ForgetPassWordActivity.this, "请输入正确的验证码", Toast.LENGTH_SHORT).show();
                    return true;
                }
                return false;
            }
        });

        binding.resetPasswordAgain.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (binding.resetPasswordVerificationCode.length() == 0){
                    Toast.makeText(ForgetPassWordActivity.this, "请输入验证码", Toast.LENGTH_SHORT).show();
                    return true;
                }
                if (!isSuccess){
                    Toast.makeText(ForgetPassWordActivity.this, "请输入正确的验证码", Toast.LENGTH_SHORT).show();
                    return true;
                }
                return false;
            }
        });

        binding.resetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isEmpty()) {

//                    switch (binding.resetPasswordButton.getTag().toString()){
//                        case "next":
//                            binding.resetPasswordPhone.setVisibility(View.GONE);
//                            binding.resetVeriLayout.setVisibility(View.VISIBLE);
//                            binding.resetPasswordButton.setVisibility(View.GONE);
//                            binding.resetPasswordButton.setTag("reset");
//                            break;
//                        case "reset":
//                            break;
//                    }
                    if (!spUtils.getString(Entity.PHONE_NUMBER).equals(binding.resetPasswordPhone.getText().toString())){
                        Toast.makeText(ForgetPassWordActivity.this, "手机号码已更换，请重新获取验证码", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (!binding.resetPasswordAgain.getText().toString().equals(binding.resetPassword.getText().toString())) {
                        Toast.makeText(ForgetPassWordActivity.this, "两次输入的密码不一致", Toast.LENGTH_SHORT).show();
                    }else{
                        Utils.creatDialog(dialog);
                        head.setUserName(binding.resetPasswordPhone.getText().toString());
                        head.setNewPassWord(binding.resetPassword.getText().toString());
                        httpUtils.postForgetPassWord(handler, HttpEntity.MAIN_URL + HttpEntity.REST_PASSWORD, binding.resetPasswordPhone.getText().toString(), binding.resetPassword.getText().toString());
                    }
                }
            }
        });

        binding.resetPasswordBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @SuppressLint("HandlerLeak")
    public Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    dialog.cancel();
                    Toast.makeText(ForgetPassWordActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                    ForgetPassWordActivity.this.finish();
                    break;
                case 1:
                    dialog.cancel();
                    Toast.makeText(ForgetPassWordActivity.this, "修改失败，请稍后重试！", Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    timer.cancel();
                    task.cancel();
                    binding.resetGetVerificationCode.setText("重新获取");
                    binding.resetGetVerificationCode.setEnabled(true);
                    binding.resetGetVerificationCode.setBackgroundResource(R.drawable.background_verification_selected);
                    break;
                case 3:
                    dialog.dismiss();
                    isSuccess = true;
//                    if(isSuccess){
//                        binding.resetVeriLayout.setVisibility(View.GONE);
//                        binding.resetPassword.setVisibility(View.VISIBLE);
//                        binding.resetPasswordAgain.setVisibility(View.VISIBLE);
//                        binding.resetPasswordButton.setVisibility(View.VISIBLE);
//                        binding.resetPasswordButton.setText("重置密码");
//                        binding.resetPasswordButton.setEnabled(false);
//                    }

                    Toast.makeText(ForgetPassWordActivity.this, "验证成功", Toast.LENGTH_SHORT).show();
                    break;
                case 4:
                    dialog.dismiss();
                    isSuccess = false;
                    Toast.makeText(ForgetPassWordActivity.this, "验证码失效", Toast.LENGTH_SHORT).show();
                    binding.resetPasswordVerificationCode.setText("");
                    break;
                case 5:
                    binding.resetGetVerificationCode.setText(time + "s");
                    break;
            }
        }
    };

    public boolean isEmpty(){
        List<EditText> edList = new ArrayList<EditText>(){};
        edList.add(binding.resetPasswordPhone);
        edList.add(binding.resetPasswordVerificationCode);
        edList.add(binding.resetPassword);
        edList.add(binding.resetPasswordAgain);
        for (int i = 0; i < edList.size(); i++){
            EditText editText = edList.get(i);
            if (editText.length() > 0 && !TextUtils.isEmpty(editText.getText())){

            }else{
                return true;
            }
        }
        return false;
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
                    handler.sendEmptyMessage(2);
                }
                else
                {
                    handler.sendEmptyMessage(5);
                }
            }
        };
        timer.schedule(task, 1000, 1000);
    }
}
