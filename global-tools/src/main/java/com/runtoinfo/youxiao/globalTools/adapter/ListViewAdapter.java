package com.runtoinfo.youxiao.globalTools.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.runtoinfo.httpUtils.bean.GeoAreaEntity;
import com.runtoinfo.youxiao.globalTools.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by QiaoJunChao on 2018/9/17.
 */

public class ListViewAdapter extends BaseAdapter {

    public List<GeoAreaEntity> dataList = new ArrayList<>();
    public LayoutInflater inflater;
    public Context mContext;

    public ListViewAdapter(Context context, List<GeoAreaEntity> dataList){
        this.mContext = context;
        this.dataList = dataList;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        inflater = LayoutInflater.from(mContext);
        if (convertView == null){
            convertView = inflater.inflate(R.layout.activity_geoarea_item, null);
            viewHolder = new ViewHolder();
            viewHolder.textView = convertView.findViewById(R.id.geoArea_item_text);
            viewHolder.textView.setText(dataList.get(position).getName());
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        return convertView;
    }

    class ViewHolder{
        public TextView textView;
    }

    public List<GeoAreaEntity> getDataList(){
        return  dataList;
    }
}
