package com.runtoinfo.youxiao.globalTools.utils;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public static float px2other(Context context, int unit, float value) {
        DisplayMetrics metrics = new DisplayMetrics();
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

    public static int getOffSet(int page) {
        return (page - 1) * 10;
    }

    /**
     * 验证手机号
     * * @author lipw
     * * @date   2017年4月5日上午11:34:07
     * * @param mobiles 手机号码
     * * @return 有效返回true,否则返回false
     */
    public static boolean isMobileNO(String mobiles) {
        // Pattern p =
        // Pattern.compile("^((147)|(17[0-9])|(13[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$");
        Pattern p = Pattern.compile("^((13[0-9])|(15[^4])|(18[0-9])|(17[0-9])|(147))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

}
