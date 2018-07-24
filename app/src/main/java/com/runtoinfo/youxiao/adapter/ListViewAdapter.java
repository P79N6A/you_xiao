package com.runtoinfo.youxiao.adapter;

import android.Manifest;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.runtoinfo.youxiao.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/5/28 0028.
 */

public class ListViewAdapter extends BaseAdapter implements AdapterView.OnItemClickListener{

    public List<Map<String, Object>> list = new ArrayList<>();
    public Context context;

    public ListViewAdapter(Context context,  List<Map<String, Object>> list){
        this.context = context;
        this.list = list;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    public class ViewHolder{
        ImageView imageView;
        TextView textView;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.login_select_lv_item, null);
            holder = new ViewHolder();
            holder.imageView = convertView.findViewById(R.id.select_lv_img);
            holder.textView = convertView.findViewById(R.id.select_lv_tv);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.textView.setText(list.get(position).get("text").toString());
        holder.imageView.setImageBitmap((Bitmap) list.get(position).get("image"));

        return convertView;
    }
}
