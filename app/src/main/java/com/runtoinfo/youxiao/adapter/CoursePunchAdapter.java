package com.runtoinfo.youxiao.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.runtoinfo.httpUtils.HttpEntity;
import com.runtoinfo.httpUtils.bean.HomeCourseEntity;
import com.runtoinfo.httpUtils.bean.RequestDataEntity;
import com.runtoinfo.httpUtils.utils.HttpUtils;
import com.runtoinfo.youxiao.R;
import com.runtoinfo.youxiao.globalTools.adapter.BaseViewHolder;
import com.runtoinfo.youxiao.globalTools.adapter.UniversalRecyclerAdapter;
import com.runtoinfo.youxiao.globalTools.utils.DialogMessage;
import com.runtoinfo.youxiao.globalTools.utils.TimeUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by qiaojunchao on 2018/7/4 0004.
 */
@SuppressWarnings("all")
public class CoursePunchAdapter extends UniversalRecyclerAdapter<HomeCourseEntity> {

    public Activity context;
    public List<HomeCourseEntity> list;
    private int layoutId;
    private View.OnClickListener mOnClickListener;
    private TextView tSignIn;
    private HttpUtils httpUtils;


    public CoursePunchAdapter(Activity mContext, List<HomeCourseEntity> mDatas, int mLayoutId) {
        super(mContext, mDatas, mLayoutId);
        this.context = mContext;
        this.list = mDatas;
        this.layoutId = mLayoutId;
        httpUtils = new HttpUtils(mContext);
    }

    @Override
    protected void convert(Context mContext, final BaseViewHolder holder, final HomeCourseEntity homeCourseEntity, int position) {
        holder.setText(R.id.home_course_name, homeCourseEntity.getCourseName());
        holder.setText(R.id.home_course_time, /*TimeUtil.iso8601ToDate(*/homeCourseEntity.getBeginTime()/*, 1)*/);
        httpUtils.postSrcPhoto(context, homeCourseEntity.getCoverPhoto(),(ImageView) holder.getView(R.id.home_img_course));
        holder.getView(R.id.home_details).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build("/cources/colorfulActivity").navigation();
            }
        });
        tSignIn = holder.getView(R.id.home_course_sign);
        if (homeCourseEntity.isSignIn){
            tSignIn.setText("已签");
            tSignIn.setBackgroundResource(R.drawable.home_sign_finish);
            tSignIn.setEnabled(false);
        }else{
            tSignIn.setText("签到");
            tSignIn.setBackgroundResource(R.drawable.home_sign_in);
            tSignIn.setEnabled(true);
        }

        holder.setOnClickListener(R.id.home_course_sign, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> map = new HashMap<>();
                map.put("CourseInstId", homeCourseEntity.getCourseInstId());
                map.put("url", HttpEntity.MAIN_URL + HttpEntity.POST_SIGNIN_COURSE);
                map.put("token", homeCourseEntity.getToken());
                httpUtils.postSignIn(handler, map);
            }
        });
    }

    public void setOnClickListener(View.OnClickListener listener){
        this.mOnClickListener = listener;
    }

    public Handler handler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    tSignIn.setText("已签");
                    tSignIn.setBackgroundResource(R.drawable.home_sign_finish);
                    tSignIn.setEnabled(false);
                    break;
                case 404:
                    DialogMessage.showToast(context, "签到失败");
                    break;
            }
        }
    };

}
