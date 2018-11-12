package com.youxiao.comment.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jaeger.library.StatusBarUtil;
import com.runtoinfo.httpUtils.CPRCBean.CPRCDataEntity;
import com.runtoinfo.httpUtils.CPRCBean.CPRCTypeEntity;
import com.runtoinfo.httpUtils.CPRCBean.CommentRequestResultEntity;
import com.runtoinfo.httpUtils.HttpEntity;
import com.runtoinfo.httpUtils.bean.RequestDataEntity;
import com.runtoinfo.httpUtils.utils.HttpUtils;
import com.runtoinfo.youxiao.globalTools.utils.DensityUtil;
import com.runtoinfo.youxiao.globalTools.utils.DialogMessage;
import com.runtoinfo.youxiao.globalTools.utils.Entity;
import com.runtoinfo.youxiao.globalTools.utils.IntentDataType;
import com.runtoinfo.youxiao.globalTools.utils.RecyclerViewDecoration;
import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;
import com.youxiao.comment.R;
import com.youxiao.comment.adapter.CommentPublishAdapter;
import com.youxiao.comment.databinding.CommentPublishBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Route(path = "/comment/publishComment")
public class PublishComment extends BaseActivity {

    public CommentPublishBinding binding;
    public int articleId;
    public int targetType;
    public int positionResult;
    public CommentPublishAdapter mAdapter;
    public List<CommentRequestResultEntity> dataList = new ArrayList<>();
    public List<CommentRequestResultEntity> tempList = new ArrayList<>();
    public HttpUtils httpUtils;
    public int page = 1;
    public ImageView imageView;
    public CommentRequestResultEntity commentRequestResultEntity;

    //评论区点赞事件实现
    public CommentPublishAdapter.onPraiseListener onPraiseListener = new CommentPublishAdapter.onPraiseListener() {
        @Override
        public void onPraiseClick(View v, int position, CommentRequestResultEntity commentPublishItemEntity) {
            imageView = (ImageView) v;
            commentRequestResultEntity = commentPublishItemEntity;
            if (!commentRequestResultEntity.isHasPraise()) {
                CPRCDataEntity entity = new CPRCDataEntity();
                entity.setToken(spUtils.getString(Entity.TOKEN));
                entity.setUserId(spUtils.getInt(Entity.USER_ID));
                entity.setType(CPRCTypeEntity.PRAISE);
                entity.setTarget(commentPublishItemEntity.getId());
                entity.setTargetType(CPRCTypeEntity.TARGET_COMMENT);
                httpUtils.postComment(mHandler, entity);
            } else {
                //isPraise = false;
                //httpUtils.putAllStatue(handler, commentPublishItemEntity.getId(), spUtils.getString(Entity.TOKEN));
                RequestDataEntity requestDataEntity = new RequestDataEntity();
                requestDataEntity.setToken(spUtils.getString(Entity.TOKEN));
                requestDataEntity.setUrl(HttpEntity.MAIN_URL + HttpEntity.DELETE_COMMENT_CREATE);
                requestDataEntity.setId(commentPublishItemEntity.getPraiseId());
                httpUtils.delectColleciton(mHandler, requestDataEntity);
            }
        }
    };
    //评论区回复事件
    public CommentPublishAdapter.onReplayListener onReplayListener = new CommentPublishAdapter.onReplayListener() {
        @Override
        public void onReplyClick(View v, int position, CommentRequestResultEntity commentRequestResultEntity) {
            positionResult = position;
            String json = new Gson().toJson(commentRequestResultEntity);
            if (commentRequestResultEntity.getType() == 0) {
                //回复的评论
                ARouter.getInstance().build("/comment/replyComment").withString(IntentDataType.DATA, json)
                        .navigation(PublishComment.this, 1);
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(PublishComment.this, R.layout.comment_publish);
        StatusBarUtil.setColor(this, 0xffffff, 80);
        httpUtils = new HttpUtils(this);
        initData();
        initEvent();
        getCommentAll(1);
    }

    public void initData() {
        articleId = getIntent().getExtras().getInt(IntentDataType.ARTICLE);
        targetType = getIntent().getExtras().getInt(IntentDataType.TARGET_TYPE);
        Log.e("token", articleId + " :::" + targetType);
    }

    public void initEvent() {
        binding.commentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogMessage.showBottomDialog(mHandler, 0, PublishComment.this, true);
            }
        });
        binding.resetPasswordBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case 0:
                    String result = msg.obj.toString();
                    //DialogMessage.showToast(PublishComment.this, "评论成功");

                    CommentRequestResultEntity resultEntity =
                            new Gson().fromJson(result,
                                    new TypeToken<CommentRequestResultEntity>() {}.getType());
                    mAdapter.addItem(resultEntity, 0);
                    break;
                case 10:
                    String result1 = msg.obj.toString();
                    DialogMessage.showBottomDialog(mHandler, 0, PublishComment.this, false);
//                    CommentRequestResultEntity resultEntity1 =
//                            new Gson().fromJson(result, new TypeToken<CommentRequestResultEntity>(){}.getType());
                    CPRCDataEntity cprcDataEntity = new CPRCDataEntity();
                    cprcDataEntity.setType(CPRCTypeEntity.COMMENT);
                    cprcDataEntity.setTarget(articleId);
                    cprcDataEntity.setTargetType(targetType);
                    cprcDataEntity.setUserId(spUtils.getInt(Entity.USER_ID));
                    cprcDataEntity.setContent(result1);
                    cprcDataEntity.setToken(spUtils.getString(Entity.TOKEN));
                    cprcDataEntity.setLevel(1);
                    httpUtils.postComment(mHandler, cprcDataEntity);
                    break;
                case 20:
                    if (tempList != null) {
                        binding.commentPublishRecycle.setPullLoadMoreCompleted();
                        dataList.addAll(tempList);
                        if (mAdapter != null) {
                            mAdapter.notifyDataSetChanged();
                            return;
                        }
                    }
                    setDataToAdapter();
                    break;
                case 500:
                    //DialogMessage.showToast(PublishComment.this, "请求失败");
                    break;
                case 2:
                    commentRequestResultEntity.setHasPraise(true);
                    imageView.setBackgroundResource(R.drawable.comment_praised);
                    //DialogMessage.showToast(activity, "点赞成功");
                    break;
                case 200:
                    commentRequestResultEntity.setHasPraise(false);
                    imageView.setBackgroundResource(R.drawable.comment_praise);
                    break;
            }
        }
    };

    public void setDataToAdapter() {
        mAdapter = new CommentPublishAdapter(mHandler, PublishComment.this, dataList, R.layout.comment_publish_items);
        binding.commentPublishRecycle.setLinearLayout();
        binding.commentPublishRecycle.setAdapter(mAdapter);
        RecyclerViewDecoration recyclerViewDecoration = new RecyclerViewDecoration(PublishComment.this, RecyclerViewDecoration.VERTICAL_LIST);
        binding.commentPublishRecycle.addItemDecoration(recyclerViewDecoration);
        binding.commentPublishRecycle.setOnPullLoadMoreListener(pullLoadMoreListener);
        mAdapter.setOnPraiseListener(onPraiseListener);
        mAdapter.setOnReplayListener(onReplayListener);
    }

    public PullLoadMoreRecyclerView.PullLoadMoreListener pullLoadMoreListener = new PullLoadMoreRecyclerView.PullLoadMoreListener() {
        @Override
        public void onRefresh() {
            page = 1;
            dataList.clear();
            getCommentAll(page);
        }

        @Override
        public void onLoadMore() {
            page++;
            getCommentAll(page);
        }
    };

    public void getCommentAll(int page) {
        Map<String, Object> map = new HashMap<>();
        map.put("token", spUtils.getString(Entity.TOKEN));
        map.put("Type", CPRCTypeEntity.COMMENT);
        map.put("Target", articleId);
        map.put("TargetType", targetType);
        map.put("MaxResultCount", 10);
        map.put("SkipCount", DensityUtil.getOffSet(page));
        map.put("Sorting", "approvedTime desc");

        tempList = new ArrayList<>();
        httpUtils.getCommentAll(mHandler, map, 0, tempList);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK && data != null){
            String result = data.getStringExtra(IntentDataType.DATA);
            CommentRequestResultEntity entity = new Gson().fromJson(result, new TypeToken<CommentRequestResultEntity>(){}.getType());
            dataList.get(positionResult).setHasPraise(entity.isHasPraise());
            mAdapter.notifyDataSetChanged();
        }
    }
}
