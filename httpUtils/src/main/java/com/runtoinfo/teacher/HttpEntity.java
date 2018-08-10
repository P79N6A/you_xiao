package com.runtoinfo.teacher;

/**
 * Created by QiaoJunChao on 2018/8/10.
 */

public class HttpEntity {

    public final static String MAIN_URL = "http://api.11youxiao.com/";
    /**
     * 获取手机号下的信息
     */
    public final static String GET_ORGANIZATION_INFO = "/api/services/app/User/GetOrganizationInfo";
    /**
     * 登录
     */
    public final static String LOGIN_URL = "/api/TokenAuth/LoginByCaptcha";
    /**
     * 获取验证码
     */
    public final static String GET_CAPTION_CODE = "/api/services/app/Captcha/GetCaptchaCode";
    /**
     * 忘记密码
     */
    public final static String FORGET_PASSWORD = "/api/services/app/User/ForgetPassword";
}
