package com.runtoinfo.information.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.runtoinfo.information.R;
import com.runtoinfo.httpUtils.CPRCBean.CPRCDataEntity;
import com.runtoinfo.httpUtils.CPRCBean.CPRCTypeEntity;
import com.runtoinfo.httpUtils.CPRCBean.CommentRequestResultEntity;
import com.runtoinfo.httpUtils.CPRCBean.MyCommentEntity;
import com.runtoinfo.httpUtils.HttpEntity;
import com.runtoinfo.httpUtils.utils.HttpUtils;
import com.runtoinfo.youxiao.globalTools.adapter.BaseViewHolder;
import com.runtoinfo.youxiao.globalTools.adapter.UniversalRecyclerAdapter;
import com.runtoinfo.youxiao.globalTools.utils.DialogMessage;
import com.runtoinfo.youxiao.globalTools.utils.TimeUtil;

import java.util.List;

/**
 * Created by QiaoJunChao on 2018/9/4.
 */

@SuppressWarnings("all")
public class MyCommentAdapter extends UniversalRecyclerAdapter<MyCommentEntity> {

    public Activity activity;
    public Handler handler;
    public BaseViewHolder handlerHolder;
    public HttpUtils httpUtils;
    public CommentListener  listener;
    public MyCommentAdapter(Handler handler, Activity context, List<MyCommentEntity> mDatas, int mLayoutId) {
        super(handler, context, mDatas, mLayoutId);
        this.activity = context;
        httpUtils = new HttpUtils(context);
    }

    @Override
    protected void convert(Context mContext, BaseViewHolder holder, final MyCommentEntity myCommentEntity, final int position) {
        handlerHolder = holder;
        holder.setText(R.id.reply_user_name, myCommentEntity.getReplyer());
        holder.setText(R.id.reply_time, myCommentEntity.getReplyTime());
        Glide.with(mContext).load(HttpEntity.IMAGE_HEAD + myCommentEntity.getReplyerAvatar()).into((ImageView) holder.getView(R.id.infor_praise_user_ava));
        holder.setText(R.id.reply_details, myCommentEntity.getReplyContent());
        Glide.with(mContext).load(HttpEntity.IMAGE_HEAD + myCommentEntity.getTargetCover()).into((ImageView) holder.getView(R.id.reply_image));

        holder.setText(R.id.target_title, myCommentEntity.getTargetTitle());
        holder.setText(R.id.target_publish, myCommentEntity.getTargetPublisher());

        holder.getView(R.id.reply_reply).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //DialogMessage.showBottomDialog(handler, -1, activity, true);
                listener.commentListenner(v, position, myCommentEntity);
            }
        });
    }

    public interface CommentListener{
       public void commentListenner(View v, int positon, MyCommentEntity commentEntity);
    }

    public void setCommentListener(CommentListener listener){
        this.listener = listener;
    }
}
