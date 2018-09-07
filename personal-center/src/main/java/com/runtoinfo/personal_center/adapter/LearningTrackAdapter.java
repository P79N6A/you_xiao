package com.runtoinfo.personal_center.adapter;

import android.content.Context;

import com.runtoinfo.httpUtils.CenterEntity.LearnTrackEntity;
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

    }
}
