package com.runtoinfo.youxiao.globalTools.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.runtoinfo.youxiao.globalTools.R;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by QiaoJunChao on 2018/8/20.
 */
@SuppressWarnings("all")
public class DensityUtil {

    private static final int COMPLEX_UNIT_PX = 0;
    private static final int COMPLEX_UNIT_DIP = 1;
    private static final int COMPLEX_UNIT_SP = 2;
    private static final int COMPLEX_UNIT_PT = 3;
    private static final int COMPLEX_UNIT_IN = 4;
    private static final int COMPLEX_UNIT_MM = 5;
    private static final String TAG_FAKE_STATUS_BAR_VIEW = "statusBarView";
    private static final String TAG_MARGIN_ADDED = "marginAdded";


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

    //改变字符串中某字段的样式颜色
    public static SpannableStringBuilder setStringColor(String stringColor) {
        if (!TextUtils.isEmpty(stringColor)) {
            SpannableStringBuilder spanString = new SpannableStringBuilder(stringColor);
            List<int[]> indexList = getIndex(stringColor);
            if (stringColor.contains("//@")) {
                for (int i = 0; i < indexList.size(); i++) {
                    int[] index = indexList.get(i);
                    int start = index[0];
                    int end = index[1];
                    ForegroundColorSpan span1 = new ForegroundColorSpan(Color.parseColor("#27acf7"));
                    spanString.setSpan(span1, start, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                }
            }
            return spanString;
        }
        return null;
    }

    public static List<int[]> getIndex(String text){
        List<int[]> indexList = new ArrayList<>();
        int b = text.indexOf("@");
        int c = text.indexOf(":");
        while (b != -1 && c != -1){
            int[] index = new int[2];
            index[0] = b;
            index[1] = c;
            indexList.add(index);

            b = text.indexOf("@", c);
            if (c + 1 < text.length())
                c = text.indexOf(":", c + 1);
        }
        return indexList;
    }

    /**
     * 通过正则表达式的方式获取字符串中指定字符的个数
     * @param text 指定的字符串
     * @return 指定字符的个数
     */
    public static int pattern(String text) {
        // 根据指定的字符构建正则
        Pattern pattern = Pattern.compile("cs");
        // 构建字符串和正则的匹配
        Matcher matcher = pattern.matcher(text);
        int count = 0;
        // 循环依次往下匹配
        while (matcher.find()){ // 如果匹配,则数量+1
            count++;
        }
        return  count;
    }

    /**
     * 获取状态栏高度 不适用与沉浸式
     * @param activity 上下文
     * @return 状态栏高度
     */
    public static int getToolBarHeight(Activity activity){
        Rect frame = new Rect();//创建一个空的矩形对象
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);//获得顶层窗口的装饰视图，即状态栏，然后把状态栏显示的框架填充给刚刚我们创建的矩形对象，再通过矩形对象来获取状态栏高度
        int statusBarHeight = frame.top;// 获取状态栏高度：frame.top
        //Log.v("zxy1", statusBarHeight+"");//打印出来的值为：38，即状态栏高度为38px
        View v = activity.getWindow().findViewById(Window.ID_ANDROID_CONTENT);// /获得根视图，
        int allHeight = v.getTop();// 状态栏和标题栏的总高度
        return allHeight;
    }

    /**
     * 通过系统resource 获取状态栏高度
     * @param activity 上下文
     * @return 状态栏高度
     */
    public static int getStatuesBarHeight(Activity activity){
        /**
         * 获取状态栏高度——方法1
         * */
        int statusBarHeight = -1;
        //获取status_bar_height资源的ID
        int resourceId = activity.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            //根据资源ID获取响应的尺寸值
            statusBarHeight = activity.getResources().getDimensionPixelSize(resourceId);
        }
        //Log.e("WangJ", "状态栏-方法1:" + statusBarHeight1);
        return statusBarHeight;
    }


    /**
     * 4.4 - 5.0 改变状态栏颜色
     * @param activity
     * @param statusColor
     */
    static void setStatusBarColor4(Activity activity, int statusColor) {
        Window window = activity.getWindow();
        //设置Window为全透明
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        ViewGroup mContentView = (ViewGroup) window.findViewById(Window.ID_ANDROID_CONTENT);
        //获取父布局
        View mContentChild = mContentView.getChildAt(0);
        //获取状态栏高度
        int statusBarHeight = getStatuesBarHeight(activity);

        //如果已经存在假状态栏则移除，防止重复添加
        removeFakeStatusBarViewIfExist(activity);
        //添加一个View来作为状态栏的填充
        addFakeStatusBarView(activity, statusColor, statusBarHeight);
        //设置子控件到状态栏的间距
        addMarginTopToContentChild(mContentChild, statusBarHeight);
        //不预留系统栏位置
        if (mContentChild != null) {
            ViewCompat.setFitsSystemWindows(mContentChild, false);
        }
        //如果在Activity中使用了ActionBar则需要再将布局与状态栏的高度跳高一个ActionBar的高度，否则内容会被ActionBar遮挡
        int action_bar_id = activity.getResources().getIdentifier("action_bar", "id", activity.getPackageName());
        View view = activity.findViewById(action_bar_id);
        if (view != null) {
            TypedValue typedValue = new TypedValue();
            if (activity.getTheme().resolveAttribute(R.attr.actionBarSize, typedValue, true)) {
                int actionBarHeight = TypedValue.complexToDimensionPixelSize(typedValue.data, activity.getResources().getDisplayMetrics());
                setContentTopPadding(activity, actionBarHeight);
            }
        }
    }


    /**
     * 5.0以上改变状态栏颜色
     * @param activity
     * @param statusColor
     */
    public static void setStatusBarColor(Activity activity, int statusColor) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            //取消状态栏透明
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //添加Flag把状态栏设为可绘制模式
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //设置状态栏颜色
            window.setStatusBarColor(statusColor);
            //设置系统状态栏处于可见状态
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
            //让view不根据系统窗口来调整自己的布局
            ViewGroup mContentView = (ViewGroup) window.findViewById(Window.ID_ANDROID_CONTENT);
            View mChildView = mContentView.getChildAt(0);
            if (mChildView != null) {
                ViewCompat.setFitsSystemWindows(mChildView, false);
                ViewCompat.requestApplyInsets(mChildView);
            }
        }else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setStatusBarColor4(activity, statusColor);
        }
    }

    private static void removeFakeStatusBarViewIfExist(Activity activity) {
        Window window = activity.getWindow();
        ViewGroup mDecorView = (ViewGroup) window.getDecorView();

        View fakeView = mDecorView.findViewWithTag(TAG_FAKE_STATUS_BAR_VIEW);
        if (fakeView != null) {
            mDecorView.removeView(fakeView);
        }
    }

    private static View addFakeStatusBarView(Activity activity, int statusBarColor, int statusBarHeight) {
        Window window = activity.getWindow();
        ViewGroup mDecorView = (ViewGroup) window.getDecorView();

        View mStatusBarView = new View(activity);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, statusBarHeight);
        layoutParams.gravity = Gravity.TOP;
        mStatusBarView.setLayoutParams(layoutParams);
        mStatusBarView.setBackgroundColor(statusBarColor);
        mStatusBarView.setTag(TAG_FAKE_STATUS_BAR_VIEW);

        mDecorView.addView(mStatusBarView);
        return mStatusBarView;
    }

    private static void addMarginTopToContentChild(View mContentChild, int statusBarHeight) {
        if (mContentChild == null) {
            return;
        }
        if (!TAG_MARGIN_ADDED.equals(mContentChild.getTag())) {
            FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mContentChild.getLayoutParams();
            lp.topMargin += statusBarHeight;
            mContentChild.setLayoutParams(lp);
            mContentChild.setTag(TAG_MARGIN_ADDED);
        }
    }

    static void setContentTopPadding(Activity activity, int padding) {
        ViewGroup mContentView = (ViewGroup) activity.getWindow().findViewById(Window.ID_ANDROID_CONTENT);
        mContentView.setPadding(0, padding, 0, 0);
    }

    /**
     * 动态设置控件高度
     * @param activity
     * @param view
     */
    public static void setViewHeight(Activity activity, View view){
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view.getLayoutParams();
        params.height = DensityUtil.getStatuesBarHeight(activity);
        view.setLayoutParams(params);
    }

    /**
     * 动态设置控件距离上边距
     * @param activity
     * @param view
     * @param heitht
     */
    public static void setMargin(Activity activity, View view){
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view.getLayoutParams();
        params.setMargins(0, DensityUtil.getStatuesBarHeight(activity), 0, 0);
        view.setLayoutParams(params);
    }

    public static void setMargin(Activity activity, View view, int height){
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view.getLayoutParams();
        params.setMargins(0, DensityUtil.getStatuesBarHeight(activity) + height, 0, 0);
        view.setLayoutParams(params);
    }

    public static void setViewHeight(Activity activity, View view, int height){
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view.getLayoutParams();
        params.height += DensityUtil.getStatuesBarHeight(activity);
        view.setLayoutParams(params);
    }

}
