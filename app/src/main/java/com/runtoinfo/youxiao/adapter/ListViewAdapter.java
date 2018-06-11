package com.runtoinfo.youxiao.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.runtoinfo.youxiao.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/5/28 0028.
 */

public class ListViewAdapter extends BaseAdapter implements AdapterView.OnItemClickListener{

    public List<Drawable> list = new ArrayList<>();
    public Context context;

    public ListViewAdapter(Context context,  List<Drawable> list){
        this.context = context;
        this.list = list;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    public class ViewHolder{
        ImageView imageView;
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
            convertView = inflater.inflate(R.layout.frag_home_menu_item, null);
            holder = new ViewHolder();
            holder.imageView = convertView.findViewById(R.id.fragment_home_item);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.imageView.setBackground(list.get(position));

        return convertView;
    }
}
