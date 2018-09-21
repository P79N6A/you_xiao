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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.runtoinfo.httpUtils.CPRCBean.CPRCDataEntity;
import com.runtoinfo.httpUtils.CPRCBean.CPRCTypeEntity;
import com.runtoinfo.httpUtils.CPRCBean.CommentRequestResultEntity;
import com.runtoinfo.httpUtils.CPRCBean.GetAllCPC;
import com.runtoinfo.httpUtils.HttpEntity;
import com.runtoinfo.httpUtils.utils.HttpUtils;
import com.runtoinfo.youxiao.globalTools.utils.DialogMessage;
import com.runtoinfo.youxiao.globalTools.utils.Entity;
import com.runtoinfo.youxiao.globalTools.utils.IntentDataType;
import com.runtoinfo.youxiao.globalTools.utils.RecyclerViewDecoration;
import com.youxiao.comment.R;
import com.youxiao.comment.adapter.CommentPublishAdapter;
import com.youxiao.comment.databinding.ActivityReplyCommentBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

@Route(path = "/comment/replyComment")
public class ReplyComment extends BaseActivity {
    public ActivityReplyCommentBinding binding;
    public CommentRequestResultEntity resultEntity;
    public CommentPublishAdapter mAdapter;
    public List<CommentRequestResultEntity> dataList = new ArrayList<>();
    public CommentRequestResultEntity dialogEntity;
    public boolean selected = true;
    public HttpUtils httpUtils;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(ReplyComment.this,R.layout.activity_reply_comment);
        httpUtils = new HttpUtils(getBaseContext());
        initView();
        initData();
        requestAll();
        initEvent();
    }

    public void initView(){
        String result = getIntent().getStringExtra(IntentDataType.DATA);
        resultEntity = new Gson().fromJson(result, new TypeToken<CommentRequestResultEntity>(){}.getType());
    }

    public void initData(){
        binding.replyPublishName.setText(resultEntity.getNickName());
        httpUtils.postSrcPhoto(ReplyComment.this, HttpEntity.IMAGE_HEAD + resultEntity.getUserAvatar(), binding.replyCommentAvatar);
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
        if (dataList.size() > 0) {
            mAdapter = new CommentPublishAdapter(handler, ReplyComment.this, dataList, R.layout.comment_publish_items);
            binding.replyRecycler.setLayoutManager(new LinearLayoutManager(ReplyComment.this));
            binding.replyRecycler.setAdapter(mAdapter);
            binding.replyRecycler.addItemDecoration(new RecyclerViewDecoration(ReplyComment.this, RecyclerViewDecoration.HORIZONTAL_LIST));
        }else{
            DialogMessage.showToast(ReplyComment.this, "暂无回复");
        }
    }

    public void requestAll(){
        GetAllCPC cpc = new GetAllCPC();
        cpc.setToken(spUtils.getString(Entity.TOKEN));
        cpc.setType(CPRCTypeEntity.REPLY);
        cpc.setParentId(resultEntity.getId());
        cpc.setParentType(CPRCTypeEntity.PARENT_COMMENT);
        cpc.setTarget(resultEntity.getTarget());
        cpc.setTargetType(resultEntity.getTargetType());
        cpc.setMaxResultCount(10);
        cpc.setSkipCount(0);

        httpUtils.getCommentAll(handler, cpc);
    }

    public Handler handler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 20://获取全部回复 结果
                    try {
                        JSONObject result = (JSONObject) msg.obj;
                        JSONArray items = result.getJSONArray("items");
                        for (int i =0; i < items.length(); i++){
                            JSONObject item = items.getJSONObject(i);
                            CommentRequestResultEntity requestResultEntity = new Gson().fromJson(item.toString(), new TypeToken<CommentRequestResultEntity>() {}.getType());
                            dataList.add(requestResultEntity);
                        }
                        initRecyclerData();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 1://回复的评论 结果
                    String result = msg.obj.toString();
                    CommentRequestResultEntity requestResultEntity = new Gson().fromJson(result,
                            new TypeToken<CommentRequestResultEntity>(){}.getType());
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
                    cprcDataEntity.setParentType(CPRCTypeEntity.PARENT_COMMENT);
                    cprcDataEntity.setUserId(spUtils.getInt(Entity.USER_ID));
                    cprcDataEntity.setContent(json);
                    cprcDataEntity.setToken(spUtils.getString(Entity.TOKEN));
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
                    replyReply.setParentId(dialogEntity.getId());
                    replyReply.setContent(content);
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
