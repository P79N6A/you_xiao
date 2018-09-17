package com.runtoinfo.personal_center.adapter;

import android.content.Context;
import android.view.View;

import com.runtoinfo.httpUtils.CenterEntity.LearnTrackEntity;
import com.runtoinfo.personal_center.R;
import com.runtoinfo.youxiao.globalTools.adapter.BaseViewHolder;
import com.runtoinfo.youxiao.globalTools.adapter.UniversalRecyclerAdapter;

import java.util.List;

/**
 * Created by QiaoJunChao on 2018/9/5.
 */

public class LearningTrackAdapter extends UniversalRecyclerAdapter<LearnTrackEntity> {

    public LearningTrackAdapter(Context mContext, List<LearnTrackEntity> mDatas, int mLayoutId) {
        super(mContext, mDatas, mLayoutId);
    }

    @Override
    protected void convert(Context mContext, BaseViewHolder holder, LearnTrackEntity learnTrackEntity, int position) {
        holder.setText(R.id.record_progress, learnTrackEntity.getProgress() + "%");
        holder.getView(R.id.record_course_goon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //去往课程详情
            }
        });
    }
}
