package com.runtoinfo.event.adapter;

import android.app.Activity;
import android.content.Context;
import android.widget.ImageView;

import com.runtoinfo.event.R;
import com.runtoinfo.event.entity.EventEntity;
import com.runtoinfo.teacher.utils.HttpUtils;
import com.runtoinfo.youxiao.globalTools.adapter.BaseViewHolder;
import com.runtoinfo.youxiao.globalTools.adapter.UniversalRecyclerAdapter;

import java.util.List;

/**
 * Created by QiaoJunChao on 2018/8/20.
 */

public class EventRecyclerAdapter extends UniversalRecyclerAdapter<EventEntity> {

    public Activity context;
    public List<EventEntity> dataList;
    public int LayoutId;
    public EventRecyclerAdapter(Activity mContext, List<EventEntity> mDatas, int mLayoutId) {
        super(mContext, mDatas, mLayoutId);
        this.context = mContext;
        this.dataList = mDatas;
        this.LayoutId = mLayoutId;
    }

    @Override
    protected void convert(Context mContext, BaseViewHolder holder, EventEntity eventEntity, int position) {
         HttpUtils.postPhoto(context, eventEntity.getCover(),((ImageView) holder.getView(R.id.activity_comment_imageView)));
        holder.setText(R.id.activity_name, eventEntity.getEventName());
        holder.setText(R.id.activity_time, eventEntity.getStartDate().split("T")[0] + "至" + eventEntity.getEndString().split("T")[0]);
    }
}
