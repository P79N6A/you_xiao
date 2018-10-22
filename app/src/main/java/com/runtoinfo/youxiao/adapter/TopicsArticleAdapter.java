package com.runtoinfo.youxiao.adapter;

import android.app.Activity;
import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.runtoinfo.httpUtils.bean.TopiceHttpResultEntity;
import com.runtoinfo.httpUtils.utils.HttpUtils;
import com.runtoinfo.youxiao.R;
import com.runtoinfo.youxiao.globalTools.adapter.BaseViewHolder;
import com.runtoinfo.youxiao.globalTools.adapter.UniversalRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by QiaoJunChao on 2018/8/30.
 */

public class TopicsArticleAdapter extends UniversalRecyclerAdapter<TopiceHttpResultEntity> {

    public Activity mContext;
    public List<TopiceHttpResultEntity> dataList = new ArrayList<>();
    public HttpUtils httpUtils;


    public TopicsArticleAdapter(Activity mContext, List<TopiceHttpResultEntity> mDatas, int mLayoutId) {
        super(mContext, mDatas, mLayoutId);
        this.mContext = mContext;
        this.dataList = mDatas;
        httpUtils = new HttpUtils(mContext);
    }

    @Override
    protected void convert(Context mContext, BaseViewHolder holder, TopiceHttpResultEntity topicsEntity, int position) {
        int size = topicsEntity.getCoverImgs().size();
        if (size > 0) {
            Glide.with(mContext).load(topicsEntity.getCoverImgs().get(0)).into((ImageView) holder.getView(R.id.topics_img_view));
        }
        holder.setText(R.id.topics_name, topicsEntity.getTitle());
        holder.setText(R.id.topics_announcer, topicsEntity.getPublisher());
        holder.setText(R.id.topics_comment, String.valueOf(topicsEntity.getCommentNumber()));
        holder.setText(R.id.topics_praise, String.valueOf(topicsEntity.getPraiseNumber()));
        holder.setText(R.id.topics_readed, String.valueOf(topicsEntity.getReplyNumber()));
    }
}
