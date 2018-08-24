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
    /**
     * 新闻阅读量
     */
    public final static String NEWS_READ_NUMBER = "/api/services/app/News/ReadNews";
    /**
     * 活动中心
     */
    public final static String SCHOOL_CAMPAIGN = "/api/services/app/Campaign/GetAll";
    /**
     * 添加报名人员
     */
    public final static String CAMPAIGN_ADD_MEMBER = "/api/services/app/Campaign/AddMember";
    /**
     * 修改报名人员
     */
    public final static String UPDATE_MODIFY_MEMBER = "/api/services/app/Campaign/ModifyMember";
    /**
     *删除报名人员
     */
    public final static String DELETE_MODIFY_MEMBER = "/api/services/app/Campaign/DeleteMember";
    /**
     * 获取用户活动
     */
    public final static String GET_CAMPAIGN_BY_USER = "/api/services/app/Campaign/GetCampaignByUser";
    /**
     * 获取精品课类别
     */
    public final static String GET_COURSE_TYPE = "/api/services/app/DictItem/GetAll";
    /**
     * 课程分类 子类
     */
    public final static String GET_COURSE_CHILD_TYPE = "/api/services/app/CourseSubject/GetAll";
    /**
     * 获取所有的课程内容
     */
    public final static String GET_COURSE_CHILD_ALL = "/api/services/app/OnlineCourse/GetAll";

}
