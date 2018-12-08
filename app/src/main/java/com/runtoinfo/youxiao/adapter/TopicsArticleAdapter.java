package com.runtoinfo.youxiao.adapter;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.runtoinfo.httpUtils.HttpEntity;
import com.runtoinfo.httpUtils.bean.SchoolDynamicsNewEntity;
import com.runtoinfo.httpUtils.utils.HttpUtils;
import com.runtoinfo.youxiao.R;
import com.runtoinfo.youxiao.globalTools.adapter.BaseViewHolder;
import com.runtoinfo.youxiao.globalTools.adapter.UniversalRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by QiaoJunChao on 2018/8/30.
 */

public class TopicsArticleAdapter extends UniversalRecyclerAdapter<SchoolDynamicsNewEntity> {

    public Activity mContext;
    public List<SchoolDynamicsNewEntity> dataList = new ArrayList<>();
    public HttpUtils httpUtils;


    public TopicsArticleAdapter(Activity mContext, List<SchoolDynamicsNewEntity> mDatas, int mLayoutId) {
        super(mContext, mDatas, mLayoutId);
        this.mContext = mContext;
        this.dataList = mDatas;
        httpUtils = new HttpUtils(mContext);
    }

    @Override
    protected void convert(Context mContext, BaseViewHolder holder, SchoolDynamicsNewEntity topicsEntity, int position) {
        int size = topicsEntity.getCoverImgs().size();
        if (size > 0) {
            Glide.with(mContext).load(HttpEntity.FILE_HEAD + topicsEntity.getCoverImgs().get(0)).into((ImageView) holder.getView(R.id.topics_img_view));
        }
        holder.setText(R.id.topics_name, topicsEntity.getTitle());
        holder.setText(R.id.topics_announcer, "发布者：" + topicsEntity.getPublisher());
        holder.setText(R.id.topics_comment, (!TextUtils.isEmpty(String.valueOf(topicsEntity.getCommentNumber()))) ? "0" : String.valueOf(topicsEntity.getCommentNumber()));
        holder.setText(R.id.topics_praise, (!TextUtils.isEmpty(String.valueOf(topicsEntity.getPraiseNumber()))) ? "0" : String.valueOf(topicsEntity.getCommentNumber()));
        holder.setText(R.id.topics_readed, (!TextUtils.isEmpty(String.valueOf(topicsEntity.getReplyNumber()))) ? "0" : String.valueOf(topicsEntity.getReplyNumber()));
    }
}
