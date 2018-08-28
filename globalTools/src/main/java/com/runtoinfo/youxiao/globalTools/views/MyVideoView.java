package com.runtoinfo.youxiao.globalTools.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.VideoView;

/**
 * Created by Administrator on 2018/7/30 0030.
 */

public class MyVideoView extends VideoView {
    public MyVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    //重写测量方法，让视频按照布局显示
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = getDefaultSize(0, widthMeasureSpec);
        int height = getDefaultSize(0, heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

}
