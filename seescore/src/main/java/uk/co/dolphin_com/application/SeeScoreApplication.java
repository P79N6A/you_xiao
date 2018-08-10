package uk.co.dolphin_com.application;

import android.app.Application;

import com.alibaba.android.arouter.launcher.ARouter;

/**
 * Created by QiaoJunChao on 2018/8/7.
 */

public class SeeScoreApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        initArouter();
    }
    public void initArouter(){
        ARouter.openLog();     // 打印日志
        ARouter.openDebug();   // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        ARouter.init( this ); // 尽可能早，推荐在Application中初始化
    }
}
