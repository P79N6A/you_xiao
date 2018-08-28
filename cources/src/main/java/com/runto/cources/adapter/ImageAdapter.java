package com.runto.cources.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.dmcbig.mediapicker.entity.Media;
import com.runto.cources.R;
import com.runto.cources.bean.HomeWorkEntity;
import com.runtoinfo.youxiao.common_ui.adapter.BaseViewHolder;
import com.runtoinfo.youxiao.common_ui.adapter.UniversalRecyclerAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ImageAdapter extends UniversalRecyclerAdapter<Media> {

    private Context mContext;
    private ArrayList<String> mImages;
    private LayoutInflater mInflater;
    public List<Media> dataList;

    public ImageAdapter(Context context, List<Media> mData, int layoutId) {
        super(context, mData, layoutId);
        mContext = context;
        this.dataList = mData;
        this.mInflater = LayoutInflater.from(mContext);
    }

    public ArrayList<String> getImages() {
        return mImages;
    }


    @Override
    protected void convert(Context mContext, BaseViewHolder holder, Media media, int position) {

        switch (media.mediaType){
            case 1:
                if (position != dataList.size() - 1) {
                    holder.getView(R.id.home_work_tv).setVisibility(View.GONE);
                }
                holder.getView(R.id.home_work_image).setVisibility(View.VISIBLE);
                holder.getView(R.id.home_work_video).setVisibility(View.GONE);
                if (media.path != null)
                ((ImageView)holder.getView(R.id.home_work_image)).setBackground(Drawable.createFromPath(media.path));
                break;
            case 0:
                if (position != dataList.size() - 1) holder.getView(R.id.home_work_tv).setVisibility(View.GONE);
                holder.getView(R.id.home_work_image).setVisibility(View.GONE);
                holder.getView(R.id.home_work_video).setVisibility(View.VISIBLE);
                if (media.path != null)
                    ((VideoView)holder.getView(R.id.home_work_video)).setVideoURI(Uri.parse(media.path));
                break;
        }

    }


}
