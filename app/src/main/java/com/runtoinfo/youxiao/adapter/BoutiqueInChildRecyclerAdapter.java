package com.runtoinfo.youxiao.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.runtoinfo.httpUtils.bean.CourseDataEntity;
import com.runtoinfo.httpUtils.utils.HttpUtils;
import com.runtoinfo.youxiao.R;
import com.runtoinfo.youxiao.globalTools.adapter.BaseViewHolder;
import com.runtoinfo.youxiao.globalTools.adapter.UniversalRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/7/26 0026.
 */

public class BoutiqueInChildRecyclerAdapter extends UniversalRecyclerAdapter<CourseDataEntity> {

    public Context context;
    public LayoutInflater inflater;
    public List<CourseDataEntity> recyclerList = new ArrayList<>();
    public OnItemClickListener onItemClickListener;
    public HttpUtils httpUtils;

    public BoutiqueInChildRecyclerAdapter(Context mContext, List<CourseDataEntity> mDatas, int mLayoutId) {
        super(mContext, mDatas, mLayoutId);
        this.context = mContext;
        this.recyclerList = mDatas;
    }

    public BoutiqueInChildRecyclerAdapter(Context mContext, List<CourseDataEntity> mDatas) {
        super(mContext, mDatas);
    }

    @Override
    protected void convert(Context mContext, BaseViewHolder holder, CourseDataEntity courseDataEntity, int position) {
        holder.setText(R.id.boutique_recycler_title, courseDataEntity.getName());
        //holder.setText(R.id.boutique_recycler_time, String.valueOf(courseDataEntity.getStartTime().split("T")[0]));
        holder.setText(R.id.boutique_recycler_price, "¥" + String.valueOf(courseDataEntity.getPrice()));
        String purchaseNumber = TextUtils.isEmpty(String.valueOf(courseDataEntity.getPurchasedNumber())) ? courseDataEntity.getPurchasedNumber() : "0";
        holder.setText(R.id.boutique_recycler_number, purchaseNumber + " 人购买");
        holder.setText(R.id.boutique_recycler_time, courseDataEntity.getStartTime());
        Glide.with(context).load(courseDataEntity.getCover()).into((ImageView) holder.getView(R.id.in_child_recycler_imageView));
    }

    @Override
    public int getItemCount() {
        return this.recyclerList.size();
    }

}
