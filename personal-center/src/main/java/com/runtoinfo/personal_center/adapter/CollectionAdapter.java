package com.runtoinfo.personal_center.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.runtoinfo.httpUtils.CenterEntity.CollectionEntity;
import com.runtoinfo.httpUtils.HttpEntity;
import com.runtoinfo.personal_center.R;
import com.runtoinfo.youxiao.globalTools.adapter.BaseViewHolder;
import com.runtoinfo.youxiao.globalTools.adapter.UniversalRecyclerAdapter;

import java.util.List;

/**
 * Created by QiaoJunChao on 2018/9/5.
 */
@SuppressWarnings("all")
public class CollectionAdapter extends UniversalRecyclerAdapter<CollectionEntity> {

    public Activity activity;
    public int type;
    //public HttpUtils httpUtils;
    private OnDeleteClickLister mDeleteClickListener;
    public CollectionAdapter(Activity mContext, List<CollectionEntity> mDatas, int mLayoutId, int type) {
        super(mContext, mDatas, mLayoutId);
        this.activity = mContext;
        this.type = type;
        //httpUtils = new HttpUtils(mContext);
    }

    @Override
    protected void convert(Context mContext, BaseViewHolder holder, CollectionEntity collectionEntity, int position) {

        View view = holder.getView(R.id.delete_collection);
        view.setTag(position);
        if (!view.hasOnClickListeners()) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mDeleteClickListener != null) {
                        mDeleteClickListener.onDeleteClick(v, (Integer) v.getTag());
                    }
                }
            });
        }

        Glide.with(mContext).load(HttpEntity.IMAGE_HEAD + collectionEntity.getTargetCover().get(0)).into((ImageView) holder.getView(R.id.record_course_img));
        //httpUtils.postSrcPhoto(activity, HttpEntity.IMAGE_HEAD + collectionEntity.getTargetCover().get(0), (ImageView) holder.getView(R.id.record_course_img));
        holder.setText(R.id.record_title, collectionEntity.getTargetTitle());
        switch (type){
            case 0:
            case 1:
                holder.setText(R.id.record_time, collectionEntity.getTargetPublisher());
                break;
            case 5:
                holder.setText(R.id.record_time, collectionEntity.getTargetRemark());
                holder.setText(R.id.record_collection_price, "￥" + collectionEntity.getTargetPrice());
                break;
        }
    }

    public void setOnDeleteClickListener(OnDeleteClickLister listener) {
        this.mDeleteClickListener = listener;
    }

    public interface OnDeleteClickLister {
        void onDeleteClick(View view, int position);
    }
}
