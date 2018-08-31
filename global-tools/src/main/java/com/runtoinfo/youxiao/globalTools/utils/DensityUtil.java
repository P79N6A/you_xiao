package com.runtoinfo.youxiao.globalTools.utils;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Created by QiaoJunChao on 2018/8/20.
 */

public class DensityUtil {

    private static final int COMPLEX_UNIT_PX = 0;
    private static final int COMPLEX_UNIT_DIP = 1;
    private static final int COMPLEX_UNIT_SP = 2;
    private static final int COMPLEX_UNIT_PT = 3;
    private static final int COMPLEX_UNIT_IN = 4;
    private static final int COMPLEX_UNIT_MM = 5;

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 根据手机屏幕转换 使用
     */

    public static float px2other(Context context, int unit, float value){
        DisplayMetrics metrics=new DisplayMetrics();
        //获得DisplayMetrics对象方法一
        //dm=context.getResources().getDisplayMetrics();
        //获得DisplayMetrics对象方法二
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(metrics);

        switch (unit) {
            case COMPLEX_UNIT_PX:
                return value;
            case COMPLEX_UNIT_DIP:
                return value * metrics.density;
            case COMPLEX_UNIT_SP:
                return value * metrics.scaledDensity;
            case COMPLEX_UNIT_PT:
                return value * metrics.xdpi * (1.0f / 72);
            case COMPLEX_UNIT_IN:
                return value * metrics.xdpi;
            case COMPLEX_UNIT_MM:
                return value * metrics.xdpi * (1.0f / 25.4f);
        }
        return 0;
    }
}
