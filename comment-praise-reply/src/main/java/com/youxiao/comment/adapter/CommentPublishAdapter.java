package com.youxiao.comment.adapter;

import android.app.Activity;
import android.content.Context;

import com.runtoinfo.teacher.CPRCBean.CommentPublishItemEntity;
import com.runtoinfo.youxiao.globalTools.adapter.BaseViewHolder;
import com.runtoinfo.youxiao.globalTools.adapter.UniversalRecyclerAdapter;
import com.youxiao.comment.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by QiaoJunChao on 2018/8/31.
 */

public class CommentPublishAdapter extends UniversalRecyclerAdapter<CommentPublishItemEntity> {
    public List<CommentPublishItemEntity> dataList = new ArrayList<>();
    public Activity activity;
    public CommentPublishAdapter(Activity mContext, List<CommentPublishItemEntity> mDatas, int mLayoutId) {
        super(mContext, mDatas, mLayoutId);
        this.dataList = mDatas;
        this.activity = mContext;
    }

    @Override
    protected void convert(Context mContext, BaseViewHolder holder, CommentPublishItemEntity commentPublishItemEntity, int position) {
        holder.setText(R.id.comment_publish_name, commentPublishItemEntity.getUserName());
        holder.setText(R.id.comment_publish_details, commentPublishItemEntity.getCommentDetail());
        holder.setText(R.id.comment_publish_time, commentPublishItemEntity.getCommentTime());
        holder.setText(R.id.comment_publish_reply, commentPublishItemEntity.getReplyNumber());
        if (commentPublishItemEntity.isPraise()) {
            holder.setBackgroundResource(R.id.comment_publish_praise, R.drawable.comment_praised);
        }else holder.setBackgroundResource(R.id.comment_publish_praise, R.drawable.comment_praise);
    }
}
