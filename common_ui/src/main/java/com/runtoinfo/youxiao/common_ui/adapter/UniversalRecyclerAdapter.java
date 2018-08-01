package com.runtoinfo.youxiao.common_ui.adapter;

import android.content.Context;
import android.opengl.Visibility;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/7/31 0031.
 */

public class UniversalRecyclerAdapter extends RecyclerView.Adapter{

    public List<View> viewList = new ArrayList<>();
    public Context context;
    public List<String> dataList = new ArrayList<>();
    public List<Integer> idList = new ArrayList<>();
    public int layoutViewId;

    public UniversalRecyclerAdapter(Context context, int layoutViewId, List<View> viewList){
        this.context = context;
        this.viewList = viewList;
        this.layoutViewId = layoutViewId;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public ViewHolder(View itemView) {
            super(itemView);
            for (int i =0; i < viewList.size(); i++) {
                View v = viewList.get(i);
                       v = itemView.findViewById(idList.get(i));
            }
        }
    }
}
