package com.runto.cources.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.donkingliang.imageselector.entry.Image;
import com.runto.cources.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/7/23 0023.
 */

public class GridViewAdapter extends BaseAdapter {

    public List<String> dataList = new ArrayList<>();
    public LayoutInflater inflater;
    public Context context;

    public GridViewAdapter(Context context, List<String> dataList){
        this.context = context;
        this.dataList = dataList;
        this.inflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        int count = dataList == null ? 1 : dataList.size() + 1;
        return count;
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

        ViewHolder viewHolder = null;
        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.hand_work_gridview_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.deleterImage = convertView.findViewById(R.id.home_work_grid_deleter);
            viewHolder.dataImage = convertView.findViewById(R.id.home_work_grid_image);
            viewHolder.textView = convertView.findViewById(R.id.home_work_tv);
            convertView.setTag(viewHolder);
        }else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (position < dataList.size()) {
            //代表+号之前的需要正常显示图片
            String picUrl = dataList.get(position); //图片路径
            viewHolder.dataImage.setBackground(new BitmapDrawable(BitmapFactory.decodeFile(picUrl)));
            viewHolder.deleterImage.setVisibility(View.VISIBLE);
            viewHolder.textView.setVisibility(View.GONE);
            //Glide.with(context).load(new File(picUrl)).into(viewHolder.dataImage);
        } else {
            viewHolder.deleterImage.setVisibility(View.GONE);
            viewHolder.textView.setVisibility(View.VISIBLE);
            viewHolder.dataImage.setImageResource(R.drawable.course_take_photo);//最后一个显示加号图片
        }

        return convertView;
    }

    class ViewHolder{
        ImageView dataImage;
        TextView textView;
        ImageView deleterImage;
    }
}
