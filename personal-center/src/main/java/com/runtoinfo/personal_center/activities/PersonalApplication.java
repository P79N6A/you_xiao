package com.runtoinfo.personal_center.activities;

import android.app.Application;
import android.content.Context;

import com.alibaba.android.arouter.launcher.ARouter;
import com.lljjcoder.style.citylist.utils.CityListLoader;

/**
 * Created by Administrator on 2018/6/20 0020.
 */

public class PersonalApplication extends Application {

    //在自己的Application中添加如下代码
    //public RefWatcher refWatcher;
    @Override
    public void onCreate() {
        super.onCreate();
        initARouter();
        initCity();
    }

    public void initARouter(){
        ARouter.openLog();     // 打印日志
        ARouter.openDebug();   // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        ARouter.init( this ); // 尽可能早，推荐在Application中初始化
    }

    public void initCity(){
        /**
         * 预先加载一级列表所有城市的数据
         */
        CityListLoader.getInstance().loadCityData(this);
        /**
         * 预先加载三级列表显示省市区的数据
         */
        CityListLoader.getInstance().loadProData(this);
        //refWatcher = LeakCanary.install(this);
    }

    //在自己的Application中添加如下代码
//    public static RefWatcher getRefWatcher(Context context) {
//        PersonalApplication application = (PersonalApplication) context.getApplicationContext();
//        return application.refWatcher;
//    }
}
