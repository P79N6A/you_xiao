package com.runtoinfo.youxiao.adapter;

import android.Manifest;
import android.app.Activity;
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

import com.runtoinfo.teacher.HttpEntity;
import com.runtoinfo.teacher.utils.HttpUtils;
import com.runtoinfo.youxiao.R;
import com.runtoinfo.youxiao.entity.SelectSchoolEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/5/28 0028.
 */

public class ListViewAdapter extends BaseAdapter implements AdapterView.OnItemClickListener{

    public List<SelectSchoolEntity> list = new ArrayList<>();
    public Activity context;
    public Map<String, Bitmap> map = new HashMap<>();

    public ListViewAdapter(Activity context,  List<SelectSchoolEntity> list){
        this.context = context;
        this.list = list;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    public class ViewHolder{
        TextView brandName;
        TextView schoolName;
        ImageView orgLogo;
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
            holder.orgLogo = convertView.findViewById(R.id.select_image);
            holder.brandName = convertView.findViewById(R.id.select_lv_img);
            holder.schoolName = convertView.findViewById(R.id.select_lv_tv);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }
        List<String> listName = list.get(position).getSchoolName();
        for (int i = 0; i < listName.size(); i++) {
            holder.schoolName.setText(listName.get(i));
            holder.brandName.setText(list.get(position).getOrgName());
            getImage(list.get(position).getImgPath(), holder);
        }
        return convertView;
    }

    private void getImage(String url, ViewHolder holder){
        HttpUtils.postAsynchronous(context, url, holder.orgLogo);
        //holder.orgLogo.setImageBitmap(map.get("bitmap"));
    }
}
