package com.runtoinfo.teacher;

/**
 * Created by QiaoJunChao on 2018/8/10.
 */

public class HttpEntity {

    public final static String MAIN_URL = "http://api.11youxiao.com";
    /**
     * 获取手机号下的信息
     */
    public final static String GET_ORGANIZATION_INFO = "/api/services/app/User/GetOrganizationInfo";
    /**
     * 验证码登录
     */
    public final static String LOGIN_URL_CAPTCHA = "/api/TokenAuth/LoginByCaptcha";
    /**
     * 密码登录
     */
    public final static String LOGIN_URL_AUTH = "/api/TokenAuth/Authenticate";
    /**
     * 获取验证码
     */
    public final static String GET_CAPTION_CODE = "/api/services/app/Captcha/GetCaptchaCode";
    /**
     * 忘记密码
     */
    public final static String REST_PASSWORD = "/api/services/app/User/ResetPassword";
    /**
     * 验证 验证码
     */
    public final static String CAPTCHA_VALIDATE = "/api/services/app/Captcha/Validate";
    /**
     * 获取新闻列表
     */
    public final static String SCHOOL_NEWS_ALL = "/api/services/app/News/GetAll";
    /**
     * 新闻内容
     */
    public final static String SCHOOL_NEWS_CONTENT = "/api/services/app/News/Get";
}
