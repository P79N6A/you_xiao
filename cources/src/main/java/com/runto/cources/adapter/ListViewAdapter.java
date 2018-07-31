package com.runto.cources.adapter;

import android.content.Context;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.runto.cources.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/7/30 0030.
 */

public class ListViewAdapter extends BaseAdapter {

    public List<String> dataList = new ArrayList<>();
    public Context context;
    public LayoutInflater inflater;
    public ListViewAdapter(Context context, List<String> dataList){
        this.context = context;
        this.dataList = dataList;
        this.inflater = LayoutInflater.from(context);
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
        if (convertView == null){
            convertView = inflater.inflate(R.layout.course_list_listview_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.itemType = convertView.findViewById(R.id.list_item_course_type);
            viewHolder.courseName = convertView.findViewById(R.id.list_item_course_name);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.courseName.setText(dataList.get(position));
        return convertView;
    }

    public class ViewHolder{
        TextView itemType;
        TextView courseName;
    }
}
