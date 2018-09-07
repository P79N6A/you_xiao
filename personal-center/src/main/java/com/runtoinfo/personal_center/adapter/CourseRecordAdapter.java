package com.runtoinfo.personal_center.adapter;

import android.content.Context;

import com.runtoinfo.httpUtils.CenterEntity.CourseRecordEntity;
import com.runtoinfo.youxiao.globalTools.adapter.BaseViewHolder;
import com.runtoinfo.youxiao.globalTools.adapter.UniversalRecyclerAdapter;

import java.util.List;

/**
 * Created by QiaoJunChao on 2018/9/5.
 */

public class CourseRecordAdapter extends UniversalRecyclerAdapter<CourseRecordEntity> {

    public CourseRecordAdapter(Context mContext, List<CourseRecordEntity> mDatas, int mLayoutId) {
        super(mContext, mDatas, mLayoutId);
    }

    @Override
    protected void convert(Context mContext, BaseViewHolder holder, CourseRecordEntity courseRecordEntity, int position) {

    }
}
