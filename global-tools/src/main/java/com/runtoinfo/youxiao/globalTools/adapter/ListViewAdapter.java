package com.runtoinfo.youxiao.globalTools.adapter;

import android.content.Context;
import android.view.LayoutInflater;

import com.runtoinfo.httpUtils.bean.GeoAreaEntity;
import com.runtoinfo.youxiao.globalTools.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by QiaoJunChao on 2018/9/17.
 */

public class ListViewAdapter extends UniversalRecyclerAdapter<GeoAreaEntity> {

    public List<GeoAreaEntity> dataList = new ArrayList<>();
    public LayoutInflater inflater;
    public Context mContext;

    public ListViewAdapter(Context context, List<GeoAreaEntity> dataList, int layoutId){
        super(context, dataList, layoutId);
        this.mContext = context;
        this.dataList = dataList;
    }

    @Override
    protected void convert(Context mContext, BaseViewHolder holder, GeoAreaEntity geoAreaEntity, int position) {
        holder.setText(R.id.geoArea_item_text, geoAreaEntity.getName());
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public List<GeoAreaEntity> getDataList(){
        return  dataList;
    }
}
