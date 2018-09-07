package com.runtoinfo.event.adapter;

import android.app.Activity;
import android.content.Context;
import android.widget.ImageView;

import com.runtoinfo.event.R;
import com.runtoinfo.httpUtils.bean.MyEventEntity;
import com.runtoinfo.httpUtils.utils.HttpUtils;
import com.runtoinfo.youxiao.globalTools.adapter.BaseViewHolder;
import com.runtoinfo.youxiao.globalTools.adapter.UniversalRecyclerAdapter;

import java.util.List;

/**
 * Created by QiaoJunChao on 2018/8/20.
 */

public class EventRecyclerAdapter extends UniversalRecyclerAdapter<MyEventEntity> {

    public Activity context;
    public List<MyEventEntity> dataList;
    public int LayoutId;
    public int type;
    public EventRecyclerAdapter(Activity mContext, List<MyEventEntity> mDatas, int mLayoutId, int type) {
        super(mContext, mDatas, mLayoutId);
        this.context = mContext;
        this.dataList = mDatas;
        this.LayoutId = mLayoutId;
    }

    @Override
    protected void convert(Context mContext, BaseViewHolder holder, MyEventEntity eventEntity, int position) {
        HttpUtils.postPhoto(context, eventEntity.getCover(),((ImageView) holder.getView(R.id.activity_comment_imageView)));
        holder.setText(R.id.activity_name, eventEntity.getName());
        if (type == 1) {
            holder.setText(R.id.activity_time, eventEntity.getStartDate().split("T")[0] + "至" + eventEntity.getEndTime().split("T")[0]);
        }else{
            holder.setText(R.id.activity_time, eventEntity.getStartDate().split("T")[0]);
        }
    }
}
