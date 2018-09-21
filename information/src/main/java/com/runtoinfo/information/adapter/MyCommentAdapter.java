package com.runtoinfo.information.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;

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

public class MyCommentAdapter extends UniversalRecyclerAdapter<MyCommentEntity> {

    public Activity activity;
    public Handler handler;
    public BaseViewHolder handlerHolder;
    public HttpUtils httpUtils;
    public MyCommentAdapter(Handler handler, Activity context, List<MyCommentEntity> mDatas, int mLayoutId) {
        super(handler, context, mDatas, mLayoutId);
        this.activity = context;
        httpUtils = new HttpUtils(context);
    }

    @Override
    protected void convert(Context mContext, BaseViewHolder holder, final MyCommentEntity myCommentEntity, int position) {
        handlerHolder = holder;
        holder.setText(R.id.reply_user_name, myCommentEntity.getReplyer());
        holder.setText(R.id.reply_time, myCommentEntity.getReplyTime());
        httpUtils.postSrcPhoto(activity, HttpEntity.IMAGE_HEAD + myCommentEntity.getReplyerAvatar(), (ImageView) holder.getView(R.id.infor_praise_user_ava));
        holder.setText(R.id.reply_details, setStringColor(myCommentEntity.getReplyContent()));
        httpUtils.postPhoto(activity, HttpEntity.IMAGE_HEAD + myCommentEntity.getTargetCover(), (ImageView) holder.getView(R.id.reply_image));
        holder.setText(R.id.target_title, myCommentEntity.getTargetTitle());
        holder.setText(R.id.target_publish, myCommentEntity.getTargetPublisher());

        holder.getView(R.id.reply_reply).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogMessage.showBottomDialog(handler, -1, activity, true);
            }
        });

        handler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case 20:
                        String content = msg.obj.toString();
                        CPRCDataEntity entity = new CPRCDataEntity();
                        entity.setType(CPRCTypeEntity.REPLY);
                        entity.setParentId(myCommentEntity.getReplyId());
                        entity.setParentType(CPRCTypeEntity.TARGET_REPLY);
                        entity.setTarget(myCommentEntity.getTargetId());
                        entity.setTargetType(myCommentEntity.getTargetType());
                        entity.setContent(content);
                        httpUtils.postComment(handler, entity);
                        break;
                    case 3:
                        CommentRequestResultEntity resultEntity = new Gson().fromJson(msg.obj.toString(), new TypeToken<CommentRequestResultEntity>(){}.getType());
                        MyCommentEntity commentEntity = new MyCommentEntity();
                        String rContent = resultEntity.getContent() + "//@" + resultEntity.getNickName() + ":";
                        commentEntity.setReplyContent(rContent + myCommentEntity.getReplyContent());
                        commentEntity.setReplyTime(TimeUtil.iso8601ToDate(resultEntity.getApprovedTime(), 1));
                        commentEntity.setReplyer(resultEntity.getNickName());
                        commentEntity.setTargetCover(myCommentEntity.getTargetCover());
                        commentEntity.setTargetTitle(myCommentEntity.getTargetTitle());
                        commentEntity.setTargetPublisher(myCommentEntity.getTargetPublisher());
                        addItem(commentEntity, 0);
                        break;
                }
            }
        };
    }
    //改变字符串中某字段的样式颜色
    public SpannableStringBuilder setStringColor(String stringColor){
        SpannableStringBuilder spanString = new SpannableStringBuilder(stringColor);
        ForegroundColorSpan span1 = new ForegroundColorSpan(Color.parseColor("#27acf7"));
        spanString.setSpan(span1, stringColor.indexOf("@"), stringColor.indexOf(":"), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        /*spanString.setSpan(span, 0, 11, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);*/
        return spanString;
    }


}
