package com.youxiao.comment.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jaeger.library.StatusBarUtil;
import com.runtoinfo.httpUtils.CPRCBean.CPRCDataEntity;
import com.runtoinfo.httpUtils.CPRCBean.CPRCTypeEntity;
import com.runtoinfo.httpUtils.CPRCBean.CommentRequestResultEntity;
import com.runtoinfo.httpUtils.CPRCBean.GetAllCPC;
import com.runtoinfo.httpUtils.HttpEntity;
import com.runtoinfo.httpUtils.utils.HttpUtils;
import com.runtoinfo.youxiao.globalTools.utils.DensityUtil;
import com.runtoinfo.youxiao.globalTools.utils.DialogMessage;
import com.runtoinfo.youxiao.globalTools.utils.Entity;
import com.runtoinfo.youxiao.globalTools.utils.IntentDataType;
import com.runtoinfo.youxiao.globalTools.utils.RecyclerViewDecoration;
import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;
import com.youxiao.comment.R;
import com.youxiao.comment.adapter.CommentPublishAdapter;
import com.youxiao.comment.databinding.ActivityReplyCommentBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Route(path = "/comment/replyComment")
public class ReplyComment extends BaseActivity {
    public ActivityReplyCommentBinding binding;
    public CommentRequestResultEntity resultEntity;
    public CommentPublishAdapter mAdapter;
    public List<CommentRequestResultEntity> dataList = new ArrayList<>();
    public CommentRequestResultEntity dialogEntity;
    public boolean selected = true;
    public HttpUtils httpUtils;
    public int page = 1;
    public List<CommentRequestResultEntity> replayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(ReplyComment.this,R.layout.activity_reply_comment);
        StatusBarUtil.setColor(this, 0xffffff, 80);
        httpUtils = new HttpUtils(this);
        initView();
        initData();
        requestAll(1);
        initEvent();
    }

    public void initView(){
        String result = getIntent().getStringExtra(IntentDataType.DATA);
        resultEntity = new Gson().fromJson(result, new TypeToken<CommentRequestResultEntity>(){}.getType());
    }

    public void initData(){
        binding.replyPublishName.setText(resultEntity.getNickName());
        Glide.with(this).load(HttpEntity.IMAGE_HEAD + resultEntity.getUserAvatar()).into(binding.replyCommentAvatar);
        binding.replyPublishDetails.setText(resultEntity.getContent());
        if (resultEntity.hasPraise){
            binding.replyPublishPraise.setImageResource(R.drawable.comment_praised);
        }else{
            binding.replyPublishPraise.setImageResource(R.drawable.comment_praise);
        }
        if (!TextUtils.isEmpty(resultEntity.getApprovedTime()))
        binding.replyPublishTime.setText(resultEntity.getApprovedTime().split("T")[0]);
    }

    public void initEvent(){
        binding.replyPublishLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //回复的评论 targetType = 2; parentType = 0;
                DialogMessage.showBottomDialog(handler, 1, ReplyComment.this, true);
            }
        });
        binding.replyBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        binding.replyPublishPraise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selected) {
                    CPRCDataEntity entity = new CPRCDataEntity();
                    entity.setToken(spUtils.getString(Entity.TOKEN));
                    entity.setUserId(spUtils.getInt(Entity.USER_ID));
                    entity.setType(CPRCTypeEntity.PRAISE);
                    entity.setTarget(resultEntity.getId());
                    entity.setTargetType(CPRCTypeEntity.TARGET_COMMENT);
                    httpUtils.postComment(handler, entity);
                    selected = false;
                    binding.replyPublishPraise.setBackgroundResource(R.drawable.comment_praised);
                }else{
                    httpUtils.putAllStatue(handler, resultEntity.getId(), spUtils.getString(Entity.TOKEN));
                    selected = true;
                    binding.replyPublishPraise.setBackgroundResource(R.drawable.comment_praise);
                }
            }
        });
    }

    public void initRecyclerData(){
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
            return;
        }
        mAdapter = new CommentPublishAdapter(handler, ReplyComment.this, dataList, R.layout.comment_publish_items);
        binding.replyRecycler.setLinearLayout();
        binding.replyRecycler.setAdapter(mAdapter);
        binding.replyRecycler.addItemDecoration(new RecyclerViewDecoration(ReplyComment.this, RecyclerViewDecoration.HORIZONTAL_LIST));
        binding.replyRecycler.setOnPullLoadMoreListener(pullLoadMoreListener);
    }

    public PullLoadMoreRecyclerView.PullLoadMoreListener pullLoadMoreListener = new PullLoadMoreRecyclerView.PullLoadMoreListener() {
        @Override
        public void onRefresh() {
            page = 1;
            dataList.clear();
            requestAll(page);
        }

        @Override
        public void onLoadMore() {
            page++;
            requestAll(page);
        }
    };

    /**
     * 获取评论
     * @param page 用于计算偏移量
     */
    public void requestAll(int page){
//        GetAllCPC cpc = new GetAllCPC();
//        cpc.setToken(spUtils.getString(Entity.TOKEN));
//        //cpc.setType(CPRCTypeEntity.REPLY);
//        cpc.setParentId(resultEntity.getId() + "");
//        //cpc.setParentType(CPRCTypeEntity.PARENT_REPLY);
//        //cpc.setTarget(resultEntity.getTarget());
//        //cpc.setTargetType(CPRCTypeEntity.TARGET_COMMENT);
//        cpc.setMaxResultCount(10);
//        cpc.setSkipCount(DensityUtil.getOffSet(page));

        Map<String, Object> map = new HashMap<>();
        map.put("token", spUtils.getString(Entity.TOKEN));
        map.put("ParentId", resultEntity.getId());
        map.put("MaxResultCount", 10);
        map.put("SkipCount", DensityUtil.getOffSet(page));
        map.put("Sorting", "approvedTime desc");

        httpUtils.getCommentAll(handler, map, 1, dataList);
    }

    /**
     * 用户获取回复的回复
     *
     * 回复的回复 根据评论下的回复而请求
     */
    public void requestReplayAll(CommentRequestResultEntity commentRequestResultEntity){
//        GetAllCPC cpc = new GetAllCPC();
//        //cpc.setUserId(spUtils.getInt(Entity.USER_ID));
//        //cpc.setType(CPRCTypeEntity.REPLY);
//        cpc.setParentId(commentRequestResultEntity.getId() + "");
//        //cpc.setParentType(CPRCTypeEntity.PARENT_REPLY);
//        //cpc.setTargetType(CPRCTypeEntity.TARGET_REPLY);
//        //cpc.setTarget(commentRequestResultEntity.getTarget());
//        cpc.setToken(spUtils.getString(Entity.TOKEN));
//        cpc.setMaxResultCount(10);

        Map<String, Object> map = new HashMap<>();
        map.put("token", spUtils.getString(Entity.TOKEN));
        map.put("ParentId",commentRequestResultEntity.getId());
        map.put("MaxResultCount", 10);
        map.put("SkipCount", DensityUtil.getOffSet(page));
        map.put("Sorting", "approvedTime desc");

        replayList = new ArrayList<>();
        httpUtils.getCommentAll(handler, map, 2, replayList);
    }

    public void requestReplayReplay(){
        for (int i = 0; i < dataList.size(); i++){
            requestReplayAll(dataList.get(i));
        }
    }

    public Handler handler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 20://获取全部回复 结果
                    binding.replyRecycler.setPullLoadMoreCompleted();
                    if (dataList.size() <= 0) {
                        DialogMessage.showToast(ReplyComment.this, "暂无回复");
                        return;
                    }
                    initRecyclerData();
                    //requestReplayReplay();
                    break;
                case 30://获取回复的回复结果
                    if (replayList.size() > 0){
                        for (int i = 0; i < replayList.size(); i++){
                            replayList.get(i).setCr(2);
                        }
                        mAdapter.addAll(replayList);
                    }
                    break;
                case 1://回复的评论 结果
                    String result = msg.obj.toString();
                    CommentRequestResultEntity requestResultEntity = new Gson().fromJson(result,
                            new TypeToken<CommentRequestResultEntity>(){}.getType());
                    if (mAdapter == null){
                        mAdapter = new CommentPublishAdapter(handler, ReplyComment.this, dataList, R.layout.comment_publish_items);
                        binding.replyRecycler.setLinearLayout();
                        binding.replyRecycler.setAdapter(mAdapter);
                        binding.replyRecycler.addItemDecoration(new RecyclerViewDecoration(ReplyComment.this, RecyclerViewDecoration.HORIZONTAL_LIST));
                        binding.replyRecycler.setOnPullLoadMoreListener(pullLoadMoreListener);
                    }
                    mAdapter.addItem(requestResultEntity, 0);
                    break;
                case 2:
                    String json1 = msg.obj.toString();
                    CommentRequestResultEntity requestEntity = new Gson().fromJson(json1,
                            new TypeToken<CommentRequestResultEntity>(){}.getType());
                    requestEntity.setCr(2);
                    mAdapter.addItem(requestEntity, 0);
                    break;
                case 11://回复的评论 请求
                    DialogMessage.showBottomDialog(handler, 1,ReplyComment.this, false);
                    String json = msg.obj.toString();
                    CPRCDataEntity cprcDataEntity = new CPRCDataEntity();
                    cprcDataEntity.setType(CPRCTypeEntity.REPLY);
                    cprcDataEntity.setTarget(resultEntity.getTarget());
                    cprcDataEntity.setTargetType(CPRCTypeEntity.TARGET_COMMENT);
                    cprcDataEntity.setParentId(resultEntity.getId());
                    cprcDataEntity.setParentType(CPRCTypeEntity.PARENT_REPLY);
                    cprcDataEntity.setUserId(spUtils.getInt(Entity.USER_ID));
                    cprcDataEntity.setContent(json);
                    cprcDataEntity.setToken(spUtils.getString(Entity.TOKEN));
                    cprcDataEntity.setLevel(2);
                    httpUtils.postComment(handler, cprcDataEntity);
                    break;
                case 12://来自dialogMessage中发送按钮 回复回复请求
                    DialogMessage.showBottomDialog(handler, 2,ReplyComment.this, false);
                    String content = msg.obj.toString();
                    CPRCDataEntity replyReply = new CPRCDataEntity();
                    replyReply.setType(CPRCTypeEntity.REPLY);
                    replyReply.setTargetType(CPRCTypeEntity.TARGET_REPLY);
                    replyReply.setParentType(CPRCTypeEntity.PARENT_REPLY);
                    replyReply.setTarget(dialogEntity.getTarget());
                    replyReply.setParentId(dialogEntity.parentId);
                    replyReply.setContent(content.concat("//@").concat(dialogEntity.getNickName()).concat(":").concat(dialogEntity.getContent()));
                    replyReply.setUserId(spUtils.getInt(Entity.USER_ID));
                    replyReply.setToken(spUtils.getString(Entity.TOKEN));
                    httpUtils.postComment(handler, replyReply);
                    break;
                case 13://来自mAdapter 回复按钮
                    dialogEntity = new Gson().fromJson(msg.obj.toString(), new TypeToken<CommentRequestResultEntity>(){}.getType());
                    DialogMessage.showBottomDialog(handler, 2, ReplyComment.this, true);
                    break;
            }
        }
    };
}
