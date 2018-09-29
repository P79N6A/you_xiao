package com.youxiao.comment.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.runtoinfo.httpUtils.CPRCBean.CPRCDataEntity;
import com.runtoinfo.httpUtils.CPRCBean.CPRCTypeEntity;
import com.runtoinfo.httpUtils.CPRCBean.CommentRequestResultEntity;
import com.runtoinfo.httpUtils.HttpEntity;
import com.runtoinfo.httpUtils.utils.HttpUtils;
import com.runtoinfo.youxiao.globalTools.adapter.BaseViewHolder;
import com.runtoinfo.youxiao.globalTools.adapter.UniversalRecyclerAdapter;
import com.runtoinfo.youxiao.globalTools.utils.DialogMessage;
import com.runtoinfo.youxiao.globalTools.utils.Entity;
import com.runtoinfo.youxiao.globalTools.utils.IntentDataType;
import com.runtoinfo.youxiao.globalTools.utils.SPUtils;
import com.youxiao.comment.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by QiaoJunChao on 2018/8/31.
 */
@SuppressWarnings("all")
public class CommentPublishAdapter extends UniversalRecyclerAdapter<CommentRequestResultEntity> {
    public List<CommentRequestResultEntity> dataList = new ArrayList<>();
    public Activity activity;
    public SPUtils spUtils ;
    public BaseViewHolder baseViewHolder;

    public CommentRequestResultEntity temResultEntity;
    public Handler dataHandler;
    public Handler handler;
    public HttpUtils httpUtils;
    public CommentPublishAdapter(Handler handler, Activity mContext, List<CommentRequestResultEntity> mDatas, int mLayoutId) {
        super(handler, mContext, mDatas, mLayoutId);
        this.dataList = mDatas;
        this.activity = mContext;
        this.spUtils = new SPUtils(mContext);
        this.dataHandler = handler;
        httpUtils = new HttpUtils(mContext);
    }

    @Override
    protected void convert(final Context mContext, final BaseViewHolder holder, final CommentRequestResultEntity commentPublishItemEntity, int position) {
        this.baseViewHolder = holder;
        holder.setText(R.id.comment_publish_name, commentPublishItemEntity.getNickName());
        if (commentPublishItemEntity.getCr() == 2 && temResultEntity != null){
            holder.setText(R.id.comment_publish_details,
                    commentPublishItemEntity.getContent() + " //@" + temResultEntity.getNickName() + ":" + temResultEntity.getContent());
        }else {
            temResultEntity = null;
            holder.setText(R.id.comment_publish_details, commentPublishItemEntity.getContent());
        }
        Glide.with(mContext).load(HttpEntity.IMAGE_HEAD + commentPublishItemEntity.getUserAvatar())
                .into((ImageView) holder.getView(R.id.comment_publish_item_img));
        String[] time = new String[2];
        if (commentPublishItemEntity.getApprovedTime() != null) {
             time = commentPublishItemEntity.getApprovedTime().split("T");
        }
        //时间格式要进行更改，改为“1分钟之前”，目前是显示的日期
        holder.setText(R.id.comment_publish_time, time[0] /*+ "  " + time[1]*/);
        //因数据不完善，本是int类型数据，得到的是null
        Log.e("comment", String.valueOf(commentPublishItemEntity.getReplyNumber() ));
        holder.setText(R.id.comment_publish_reply, String.valueOf(commentPublishItemEntity.getReplyNumber() + "回复"));
        if (commentPublishItemEntity.hasPraise) {
            holder.setBackgroundResource(R.id.comment_publish_praise, R.drawable.comment_praised);
        }else {
            holder.setBackgroundResource(R.id.comment_publish_praise, R.drawable.comment_praise);
        }
        //赞的点击事件
        final ImageView imageView =  holder.getView(R.id.comment_publish_praise);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!commentPublishItemEntity.hasPraise) {
                    CPRCDataEntity entity = new CPRCDataEntity();
                    entity.setToken(spUtils.getString(Entity.TOKEN));
                    entity.setUserId(spUtils.getInt(Entity.USER_ID));
                    entity.setType(CPRCTypeEntity.PRAISE);
                    entity.setTarget(commentPublishItemEntity.getId());
                    entity.setTargetType(CPRCTypeEntity.TARGET_COMMENT);
                    httpUtils.postComment(handler, entity);
                    imageView.setBackgroundResource(R.drawable.comment_praised);
                }else{
                    httpUtils.putAllStatue(handler, commentPublishItemEntity.getId(), spUtils.getString(Entity.TOKEN));
                    imageView.setBackgroundResource(R.drawable.comment_praise);
                }
            }
        });

        holder.getView(R.id.comment_publish_reply).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String json = new Gson().toJson(commentPublishItemEntity);
                temResultEntity = commentPublishItemEntity;
                switch (commentPublishItemEntity.getType()){
                    case 0://回复的评论
                        ARouter.getInstance().build("/comment/replyComment").withString(IntentDataType.DATA, json)
                                .navigation();
                        break;
                    case 1://回复的回复
                        Message msg = new Message();
                        msg.what = 13;
                        msg.obj = json;
                        dataHandler.sendMessage(msg);
                        break;
                    case 2:
                        break;
                }
            }
        });

        handler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 2){
                    DialogMessage.showToast(activity, "点赞成功");
                }else if (msg.what == 30){
                    holder.setBackgroundResource(R.id.comment_publish_praise, R.drawable.comment_praise);
                }
            }
        };

    }


}
