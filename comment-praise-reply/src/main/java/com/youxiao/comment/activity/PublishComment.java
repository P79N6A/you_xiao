package com.youxiao.comment.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.runtoinfo.httpUtils.CPRCBean.CPRCDataEntity;
import com.runtoinfo.httpUtils.CPRCBean.CPRCTypeEntity;
import com.runtoinfo.httpUtils.CPRCBean.CommentRequestResultEntity;
import com.runtoinfo.httpUtils.CPRCBean.GetAllCPC;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@Route(path = "/comment/publishComment")
public class PublishComment extends BaseActivity {

    public CommentPublishBinding binding;
    public int articleId;
    public int targetType;
    public CommentPublishAdapter mAdapter;
    public List<CommentRequestResultEntity> dataList = new ArrayList<>();
    public List<CommentRequestResultEntity> tempList = new ArrayList<>();
    public HttpUtils httpUtils;
    public int page = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(PublishComment.this, R.layout.comment_publish);
        httpUtils = new HttpUtils(this);
        initData();
        initEvent();
        getCommentAll(1);
    }

    public void initData(){
        articleId = getIntent().getExtras().getInt(IntentDataType.ARTICLE);
        targetType = getIntent().getExtras().getInt(IntentDataType.TARGET_TYPE);
    }

    public void initEvent(){
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

    public Handler mHandler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what){
                case 0:
                    String result = msg.obj.toString();
                    DialogMessage.showToast(PublishComment.this, "评论成功");

                    CommentRequestResultEntity resultEntity =
                            new Gson().fromJson(result, new TypeToken<CommentRequestResultEntity>(){}.getType());
                    mAdapter.addItem(resultEntity, 0);
                    break;
                case 10:
                    String result1 = msg.obj.toString();
                    DialogMessage.showBottomDialog(mHandler,0, PublishComment.this, false);
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
                    if (tempList != null){
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
                    DialogMessage.showToast(PublishComment.this, "请求失败");
                    break;
            }
        }
    };

    public void setDataToAdapter(){
        mAdapter = new CommentPublishAdapter(mHandler, PublishComment.this, dataList, R.layout.comment_publish_items);
        binding.commentPublishRecycle.setLinearLayout();
        binding.commentPublishRecycle.setAdapter(mAdapter);
        RecyclerViewDecoration recyclerViewDecoration = new RecyclerViewDecoration(PublishComment.this, RecyclerViewDecoration.VERTICAL_LIST);
        binding.commentPublishRecycle.addItemDecoration(recyclerViewDecoration);
        binding.commentPublishRecycle.setOnPullLoadMoreListener(pullLoadMoreListener);
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

    public void getCommentAll(int page){
        GetAllCPC cpc = new GetAllCPC();
        cpc.setToken(spUtils.getString(Entity.TOKEN));
        cpc.setType(CPRCTypeEntity.COMMENT);
        cpc.setTarget(articleId);
        cpc.setTargetType(targetType);
        cpc.setMaxResultCount(10);
        cpc.setSkipCount(DensityUtil.getOffSet(page));
        tempList = new ArrayList<>();
        httpUtils.getCommentAll(mHandler, cpc, 0, tempList);
    }

}
