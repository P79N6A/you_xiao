package com.runtoinfo.information.adapter;

import android.app.Activity;
import android.content.Context;
import android.widget.ImageView;

import com.runtoinfo.httpUtils.CPRCBean.PraiseEntity;
import com.runtoinfo.httpUtils.HttpEntity;
import com.runtoinfo.httpUtils.utils.HttpUtils;
import com.runtoinfo.information.R;
import com.runtoinfo.youxiao.globalTools.adapter.BaseViewHolder;
import com.runtoinfo.youxiao.globalTools.adapter.UniversalRecyclerAdapter;

import java.util.List;

/**
 * Created by QiaoJunChao on 2018/9/5.
 */

public class PraiseAdapter extends UniversalRecyclerAdapter<PraiseEntity> {

    public Activity activity;

    public PraiseAdapter(Activity mContext, List<PraiseEntity> mDatas, int mLayoutId) {
        super(mContext, mDatas, mLayoutId);
        this.activity  = mContext;
    }

    @Override
    protected void convert(Context mContext, BaseViewHolder holder, PraiseEntity praiseEntity, int position) {
        HttpUtils.postSrcPhoto(activity, HttpEntity.IMAGE_HEAD + praiseEntity.getPraiseAvatar(), (ImageView) holder.getView(R.id.infor_praise_user_image));
        holder.setText(R.id.praise_name, praiseEntity.getPraiser());
        holder.setText(R.id.praise_time, praiseEntity.getPraiseTime());
        holder.setText(R.id.praise_default_name, praiseEntity.getPraiser());
        holder.setText(R.id.praise_default_comment, praiseEntity.getPraisedContent());
        HttpUtils.postPhoto(activity, HttpEntity.IMAGE_HEAD + praiseEntity.getTargetCover(), (ImageView) holder.getView(R.id.praise_ava_img));
        holder.setText(R.id.praise_ava_title, praiseEntity.getTargetTitle());
        holder.setText(R.id.praise_publisher, praiseEntity.getTargetPublisher());
    }
}
