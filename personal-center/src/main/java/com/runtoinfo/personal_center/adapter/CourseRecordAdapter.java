package com.runtoinfo.personal_center.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.runtoinfo.httpUtils.bean.CourseEntity;
import com.runtoinfo.httpUtils.utils.HttpUtils;
import com.runtoinfo.personal_center.R;
import com.runtoinfo.youxiao.globalTools.adapter.BaseViewHolder;
import com.runtoinfo.youxiao.globalTools.adapter.UniversalRecyclerAdapter;

import java.util.List;

/**
 * Created by QiaoJunChao on 2018/9/5.
 */
@SuppressWarnings("all")
public class CourseRecordAdapter extends UniversalRecyclerAdapter<CourseEntity> {


    public HttpUtils httpUtils;
    public Activity activity;
    public List<CourseEntity> courseEntityList;
    public CourseRecordAdapter(Activity mContext, List<CourseEntity> mDatas, int mLayoutId) {
        super(mContext, mDatas, mLayoutId);
        httpUtils = new HttpUtils(mContext);
        this.courseEntityList = mDatas;
        this.activity = mContext;
    }

    @Override
    protected void convert(Context mContext, BaseViewHolder holder, CourseEntity courseRecordEntity, int position) {
        Glide.with(mContext).load(courseRecordEntity.getCoverPhoto()).into((ImageView) holder.getView(R.id.record_course_img));
        holder.setText(R.id.record_course_title, courseRecordEntity.getClassName());
        holder.setText(R.id.record_course_teacher, courseRecordEntity.getTeacherName());
        holder.setText(R.id.record_course_update_time, courseRecordEntity.getDate());
    }
}
