package com.runtoinfo.youxiao.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.runtoinfo.youxiao.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/6/8 0008.
 */

public class SelectSchoolAdapter extends BaseAdapter {

    public Context context;
    public Map<String, Bitmap> map = new HashMap<>();
    public List<String> nameList = new ArrayList<>();
    public SelectSchoolAdapter(Context context, Map<String, Bitmap> map){
        this.context = context;
        this.map = map;
        this.nameList.addAll(map.keySet());
    }

    public class ViewHold{
        ImageView imageView;
        TextView textView;
    }

    @Override
    public int getCount() {
        return map.size();
    }

    @Override
    public Object getItem(int position) {
        return nameList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint({"BaseViewHolder", "InflateParams", "WrongViewCast"})
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHold viewHold = null;
        LayoutInflater inflater = LayoutInflater.from(context);
        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.login_select_lv_item, null);
            viewHold = new ViewHold();
            viewHold.imageView = convertView.findViewById(R.id.select_lv_img);
            viewHold.textView = convertView.findViewById(R.id.select_lv_tv);
            convertView.setTag(viewHold);
        }
        else
        {
            viewHold =(ViewHold)convertView.getTag();
        }
        viewHold.imageView.setImageBitmap(map.get(nameList.get(position)));
        viewHold.textView.setText(nameList.get(position));
        return convertView;
    }
}
