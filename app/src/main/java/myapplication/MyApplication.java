package myapplication;

import android.app.Activity;
import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.sdk.android.feedback.impl.FeedbackAPI;
import com.alibaba.sdk.android.feedback.util.ErrorCode;
import com.alibaba.sdk.android.feedback.util.FeedbackErrorCallback;
import com.alibaba.sdk.android.httpdns.HttpDns;
import com.alibaba.sdk.android.httpdns.HttpDnsService;
import com.alibaba.sdk.android.man.MANService;
import com.alibaba.sdk.android.man.MANServiceProvider;
import com.alibaba.sdk.android.push.CloudPushService;
import com.alibaba.sdk.android.push.CommonCallback;
import com.alibaba.sdk.android.push.noonesdk.PushServiceFactory;
import com.alibaba.sdk.android.push.register.GcmRegister;
import com.alibaba.sdk.android.push.register.HuaWeiRegister;
import com.alibaba.sdk.android.push.register.MiPushRegister;
import com.runtoinfo.youxiao.globalTools.utils.Entity;
import com.runtoinfo.youxiao.globalTools.utils.SPUtils;
import com.squareup.leakcanary.LeakCanary;
import com.taobao.sophix.SophixManager;
import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.smtt.sdk.QbSdk;
import com.ut.mini.internal.UTTeamWork;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Stack;
import java.util.concurrent.Callable;

import static com.tencent.bugly.Bugly.applicationContext;

/**
 * Created by Administrator on 2018/5/8 0008.
 */
@SuppressWarnings("all")
public class MyApplication extends Application {

    private static final String TAG = "AppApplication";
    private static Stack<Activity> activityStack;
    private static MyApplication singleton;
    public static SPUtils spUtils;
    public CloudPushService pushService;
    public boolean isUpdate = false;
    public int activityCount = 0;

    public interface MsgDisplayListener {
        void handle(String msg);
    }

    public static MsgDisplayListener msgDisplayListener = null;
    public static StringBuilder cacheMsg = new StringBuilder();

    @Override
    public void onCreate() {
        super.onCreate();
        singleton = this;
        spUtils = new SPUtils(this);
        SophixManager.getInstance().queryAndLoadNewPatch();
        initArouter();
        checkPermission();
        initManService();
        initFeedbackService();
        initHttpDnsService();
        //initPushService(this);
        initBackgoundCallBack();
        initQbSdk();
        //initPushAuxiliaryChannel();
        CrashReport.initCrashReport(getApplicationContext(), "446920bced", true);
    }

    public static MyApplication getInstance() {
        return singleton;
    }

    public void initLeakCanary(){
        if (LeakCanary.isInAnalyzerProcess(this)){
            return;
        }
        LeakCanary.install(this);
    }

    /**
     * ARouter 初始化
     */
    public void initArouter() {
        // 打印日志
        ARouter.openLog();
        // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        ARouter.openDebug();
        // 尽可能早，推荐在Application中初始化
        ARouter.init(this);
    }

    /**
     * 初始化X5WebView控件
     */
    public void initQbSdk(){
        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {

            @Override
            public void onViewInitFinished(boolean arg0) {
                // TODO Auto-generated method stub
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                Log.d("app", " onViewInitFinished is " + arg0);
            }

            @Override
            public void onCoreInitFinished() {
                // TODO Auto-generated method stub
            }
        };
        //x5内核初始化接口
        QbSdk.initX5Environment(getApplicationContext(),  cb);
        QbSdk.setDownloadWithoutWifi(true);
    }

    private void initManService() {
        /**
         * 初始化Mobile Analytics服务
         */
        // 获取MAN服务
        MANService manService = MANServiceProvider.getService();
        //用户注册埋点
        manService.getMANAnalytics().userRegister("usernick");
        //用户登录埋点
        manService.getMANAnalytics().updateUserAccount("usernick", "userid");
        //用户注销埋点
        manService.getMANAnalytics().updateUserAccount("", "");
        // 打开调试日志
        manService.getMANAnalytics().turnOnDebug();
        manService.getMANAnalytics().setAppVersion("3.0");
        // MAN初始化方法之一，通过插件接入后直接在下发json中获取appKey和appSecret初始化
        manService.getMANAnalytics().init(this, getApplicationContext());
        // MAN另一初始化方法，手动指定appKey和appSecret
        // String appKey = "******";
        // String appSecret = "******";
        // manService.getMANAnalytics().init(this, getApplicationContext(), appKey, appSecret);
        // 若需要关闭 SDK 的自动异常捕获功能可进行如下操作,详见文档5.4
        //manService.getMANAnalytics().turnOffCrashReporter();
        // 通过此接口关闭页面自动打点功能，详见文档4.2
        manService.getMANAnalytics().turnOffAutoPageTrack();
        // 设置渠道（用以标记该app的分发渠道名称），如果不关心可以不设置即不调用该接口，渠道设置将影响控制台【渠道分析】栏目的报表展现。如果文档3.3章节更能满足您渠道配置的需求，就不要调用此方法，按照3.3进行配置即可
        //manService.getMANAnalytics().setChannel("某渠道");
        // 若AndroidManifest.xml 中的 android:versionName 不能满足需求，可在此指定；
        // 若既没有设置AndroidManifest.xml 中的 android:versionName，也没有调用setAppVersion，appVersion则为null
        //manService.getMANAnalytics().setAppVersion("2.0");
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("debug_api_url", "https://service-usertrack.alibaba-inc.com/upload_records_from_client");
        map.put("debug_key", "aliyun_sdk_utDetection");
        map.put("debug_sampling_option", "true");
        UTTeamWork.getInstance().turnOnRealTimeDebug(map);
    }

    private void initFeedbackService() {
        /**
         * 添加自定义的error handler
         */
        FeedbackAPI.addErrorCallback(new FeedbackErrorCallback() {
            @Override
            public void onError(Context context, String errorMessage, ErrorCode code) {
                Toast.makeText(context, "ErrMsg is: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
        FeedbackAPI.addLeaveCallback(new Callable() {
            @Override
            public Object call() throws Exception {
                Log.d("DemoApplication", "custom leave callback");
                return null;
            }
        });
        /**
         * 建议放在此处做初始化
         */
        //默认初始化
        FeedbackAPI.init(this);
        /**
         * 自定义参数演示
         */
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("loginTime", "登录时间");
            jsonObject.put("visitPath", "登陆，关于，反馈");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        FeedbackAPI.setAppExtInfo(jsonObject);
        /**
         * 以下是设置UI
         */
        //设置默认联系方式
        FeedbackAPI.setDefaultUserContactInfo("13800000000");
        //沉浸式任务栏，控制台设置为true之后此方法才能生效
        FeedbackAPI.setTranslucent(true);
        //设置返回按钮图标
        //FeedbackAPI.setBackIcon(R.drawable.ali_feedback_common_back_btn_bg);
        //设置标题栏"历史反馈"的字号，需要将控制台中此字号设置为0
        FeedbackAPI.setHistoryTextSize(20);
        //设置标题栏高度，单位为像素
        FeedbackAPI.setTitleBarHeight(100);
    }

    private void initHttpDnsService() {
        // 初始化httpdns
        //HttpDnsService httpdns = HttpDns.getService(getApplicationContext(), accountID);
        HttpDnsService httpdns = HttpDns.getService(getApplicationContext());
        //this.setPreResoveHosts();
        // 允许过期IP以实现懒加载策略
        //httpdns.setExpiredIPEnabled(true);
    }

    /**
     * 检测应用处在什么状态
     */
    public void initBackgoundCallBack() {
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {}

            @Override
            public void onActivityStarted(Activity activity) {
                activityCount++;
            }

            @Override
            public void onActivityResumed(Activity activity) {}

            @Override
            public void onActivityPaused(Activity activity) {}

            @Override
            public void onActivityStopped(Activity activity) {
                activityCount--;
                if (activityCount == 0 && spUtils.getBoolean(Entity.SOP_HIX_SUCCESS)) {
                    SophixManager.getInstance().killProcessSafely();
                }
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {}

            @Override
            public void onActivityDestroyed(Activity activity) {
                spUtils.setBoolean(Entity.SOP_HIX_SUCCESS, false);
            }
        });
    }

    /**
     * 初始化云推送通道
     *
     * @param applicationContext
     */
    public void initPushService(final Context applicationContext) {
        PushServiceFactory.init(applicationContext);
        pushService = PushServiceFactory.getCloudPushService();
        pushService.register(applicationContext, new CommonCallback() {
            @Override
            public void onSuccess(String response) {
                Log.e("bindAccount", "消息推送初始化成功");
            }

            @Override
            public void onFailed(String errorCode, String errorMessage) {
            }
        });

        pushService.bindAccount(String.valueOf(spUtils.getInt(Entity.USER_ID)), new CommonCallback() {
            @Override
            public void onSuccess(String s) {
                Log.e("bindAccount", "绑定成功");
                Log.e("bindAccount", pushService.getDeviceId());
            }

            @Override
            public void onFailed(String s, String s1) {
            }
        });


    }

    /**
     * 解绑设备号
     */
    public void unbindAccount() {
        pushService.unbindAccount(new CommonCallback() {
            @Override
            public void onSuccess(String s) {
                Log.e("unbindAccount", "解绑成功");
            }

            @Override
            public void onFailed(String s, String s1) {

            }
        });
    }

    /**
     * 初始化推送辅助通道
     */
    public void initPushAuxiliaryChannel(){
        // 注册方法会自动判断是否支持小米系统推送，如不支持会跳过注册。
        MiPushRegister.register(getApplicationContext(), "2882303761517883630", "5181788332630");
        // 注册方法会自动判断是否支持华为系统推送，如不支持会跳过注册。
        HuaWeiRegister.register(getApplicationContext());
        //GCM/FCM辅助通道注册
        //GcmRegister.register(this, sendId, applicationId); //sendId/applicationId为步骤获得的参数
    }

    public void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            // 通知渠道的id
            String id = "1";
            // 用户可以看到的通知渠道的名字.
            CharSequence name = "notification channel";
            // 用户可以看到的通知渠道的描述
            String description = "notification description";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(id, name, importance);
            // 配置通知渠道的属性
            mChannel.setDescription(description);
            // 设置通知出现时的闪灯（如果 android 设备支持的话）
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.GREEN);
            // 设置通知出现时的震动（如果 android 设备支持的话）
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            //最后在notificationmanager中创建该通知渠道
            mNotificationManager.createNotificationChannel(mChannel);
        }
    }

    /**
     * 判断是否是第一次登录
     */
    public boolean isFirstLogin() {

        SharedPreferences sp = this.getSharedPreferences("YouXiao", MODE_PRIVATE);
        boolean isFirst = sp.getBoolean("isFirst", true);
        if (isFirst) {
            sp.edit().putBoolean("isFirst", false).apply();
        }
        return isFirst;
    }

    /**
     * add Activity 添加Activity到栈
     */
    public void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<Activity>();
        }
        activityStack.add(activity);
    }

    /**
     * get current Activity 获取当前Activity（栈中最后一个压入的）
     */
    public Activity currentActivity() {
        Activity activity = activityStack.lastElement();
        return activity;
    }

    /**
     * 结束当前Activity（栈中最后一个压入的）
     */
    public void finishActivity() {
        Activity activity = activityStack.lastElement();
        finishActivity(activity);
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
            }
        }
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            if (null != activityStack.get(i)) {
                activityStack.get(i).finish();
            }
        }
        activityStack.clear();
    }

    /**
     * 退出应用程序
     */
    public void AppExit() {
        try {
            finishAllActivity();
        } catch (Exception e) {
        }
    }


}
