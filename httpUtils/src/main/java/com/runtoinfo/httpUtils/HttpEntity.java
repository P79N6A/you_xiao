package com.runtoinfo.httpUtils;

/**
 * Created by QiaoJunChao on 2018/8/10.
 */

public class HttpEntity {

    public final static String MAIN_URL = "https://api.11youxiao.com";

    public final static String IMAGE_HEAD = "https://image.11youxiao.com/";

    public final static String FILE_HEAD = "https://file.11youxiao.com/";
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
     * 修改用户信息
     */
    public final static String UPDATE_USER_INFORMATION = "/api/services/app/User/ChangProfile";
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
    /**
     * 获取主页今日卡中的课程列表
     */
    public final static String GET_HOME_COURSE_DATA = "/api/services/app/CourseSchedule/GetPunchedCourseList";
    /**
     * 签到
     */
    public final static String  POST_SIGNIN_COURSE = "/api/services/app/CourseSchedule/SignInCourse";
    /**
     * 提交作业
     */
    public final static String HAND_HOME_WORK = "/api/services/app/CourseSchedule/HandinHomework";
    /**
     * 上传多个文件
     */
    public final static String POST_ALI_SERVER = "/api/services/app/File/UploadMore";
    /**
     * 上传单个文件
     */
    public final static String POST_ALI_ONE_FILE = "/api/services/app/File/Upload";
    /**
     * 请假
     */
    public final static String COURSE_LEAVE = "/api/services/app/Leave/Create";
    /**
     * 专题
     */
    public final static String COURSE_TOPICS = "/api/services/app/SpecialTopic/GetAll";
    /**
     * 创建评论、回复、赞、收藏
     */
    public final static String COURSE_COMMENT_CREATE = "/api/services/app/Comment/Create";
    /**
     * 删除评论
     */
    public final static String DELETE_COMMENT_CREATE = "/api/services/app/Comment/Delete";
    /**
     * 获取评论
     */
    public final static String GET_COMMENT_ALL = "/api/services/app/Comment/GetAll";
    /**
     * 更新状态
     */
    public final static String UPDATE_COMMENT_ALL = "/api/services/app/Comment/Update";
    /**
     * 获取我的评论
     */
    public final static String GET_REPLY_ME = "/api/services/app/Comment/GetReplyMe";
    /**
     * 获取赞我的记录
     */
    public final static String GET_PRAISE_ME = "/api/services/app/Comment/GetPraiseMe";
    /**
     * 获取我的收藏
     */
    public final static String GET_COLLECTION_ME = "/api/services/app/Comment/GetMyFavorite";
    /**
     * 获取用户未读消息数量
     */
    public final static String GET_NOTIFICATION_COUNT = "/api/services/app/Notification/GetUserNotificationCount";
    /**
     * 获取指定的消息
     */
    public final static String GET_ASYNC_NOTIFICATION = "/api/services/app/Notification/GetUserNotificationAsync";
    /**
     * 获取用户未读消息
     */
    public final static String GET_USER_NOTIFICATION_UNREAD = "/api/services/app/Notification/GetUserNotificationsAsync";
    /**
     * 更新消息状态 （单个）
     */
    public final static String UPDATE_USER_NOTIFICATION_STATUE = "/api/services/app/Notification/ReadUserNotification";
    /**
     * 更改消息状态为已读（批量）
     */
    public final static String UPDATE_USER_NOTIFICATION_STATUE_ALL = "/api/services/app/Notification/BatchReadUserNotification";
    /**
     * 获取课表
     */
    public final static String GET_USER_COURSE_LIST = "/api/services/app/CourseSchedule/GetTimetableByUser";
    /**
     * 获取学些轨迹
     */
    public final static String GET_LEARN_TACKS = "/api/services/app/OnlineCourse/GetLearningTacks";
    /**
     * 获取校长电话
     */
    public final static String GET_SCHOOL_SETTING = "/api/services/app/CampusSetting/GetSetting";
    /**
     * 切换学校
     */
    public final static String SWITCH_CAMPUS = "/api/TokenAuth/SwitchCampus";
    /**
     * 地址选择
     */
    public final static String GET_GEO_AREA = "/api/services/app/GeoArea/GetAll";
    /**
     * 检索请假
     */
    public final static String GET_LEAVE_RECORD = "/api/services/app/Leave/GetAll";
    /**
     * 检测版本
     */
    public final static String CHECK_VERSION = "/api/services/app/AppUpgrade/GetAll";
    /**
     * 用户分页加载课程
     */
    public final static String GET_COURSE_RECORD = "/api/services/app/CourseSchedule/GetAllTimetableByUser";
}
