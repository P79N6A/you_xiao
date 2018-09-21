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
import com.runtoinfo.youxiao.globalTools.utils.DialogMessage;
import com.runtoinfo.youxiao.globalTools.utils.Entity;
import com.runtoinfo.youxiao.globalTools.utils.IntentDataType;
import com.runtoinfo.youxiao.globalTools.utils.RecyclerViewDecoration;
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
    public HttpUtils httpUtils;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(PublishComment.this, R.layout.comment_publish);
        initData();
        initEvent();
        getCommentAll();
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
            String result = msg.obj.toString();
            switch (msg.what){
                case 0:
                    DialogMessage.showToast(PublishComment.this, "评论成功");

                    CommentRequestResultEntity resultEntity =
                            new Gson().fromJson(result, new TypeToken<CommentRequestResultEntity>(){}.getType());
                    mAdapter.addItem(resultEntity, 0);
                    break;
                case 10:
                    DialogMessage.showBottomDialog(mHandler,0, PublishComment.this, false);
//                    CommentRequestResultEntity resultEntity1 =
//                            new Gson().fromJson(result, new TypeToken<CommentRequestResultEntity>(){}.getType());
                    CPRCDataEntity cprcDataEntity = new CPRCDataEntity();
                    cprcDataEntity.setType(CPRCTypeEntity.COMMENT);
                    cprcDataEntity.setTarget(articleId);
                    cprcDataEntity.setTargetType(targetType);
                    cprcDataEntity.setUserId(spUtils.getInt(Entity.USER_ID));
                    cprcDataEntity.setContent(result);
                    cprcDataEntity.setToken(spUtils.getString(Entity.TOKEN));
                    httpUtils.postComment(mHandler, cprcDataEntity);
                    break;
                case 20:
                    try {
                        JSONObject object = (JSONObject) msg.obj;
                        JSONArray array = object.getJSONArray("items");
                        Type type = new TypeToken<CommentRequestResultEntity>(){}.getType();
                        Gson gson = new Gson();
                        for (int i =0; i < array.length(); i++){
                            String item = array.getJSONObject(i).toString();
                            /*CommentRequestResultEntity requestResultEntity = new Gson().fromJson(item,
                                    new TypeToken<CommentRequestResultEntity>(){}.getType());*/
                            dataList.add((CommentRequestResultEntity) gson.fromJson(item, type));
                        }
                        setDataToAdapter();
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                    break;
                case 500:
                    DialogMessage.showToast(PublishComment.this, "请求失败");
                    break;
            }
        }
    };

    public void setDataToAdapter(){
        mAdapter = new CommentPublishAdapter(mHandler, PublishComment.this, dataList, R.layout.comment_publish_items);
        binding.commentPublishRecycle.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(PublishComment.this, LinearLayoutManager.VERTICAL, true);
        binding.commentPublishRecycle.setLayoutManager(linearLayoutManager);
        binding.commentPublishRecycle.setAdapter(mAdapter);
        binding.commentPublishRecycle.addItemDecoration(new RecyclerViewDecoration(PublishComment.this, RecyclerViewDecoration.VERTICAL_LIST));
    }

    public void getCommentAll(){
        GetAllCPC cpc = new GetAllCPC();
        cpc.setToken(spUtils.getString(Entity.TOKEN));
        cpc.setType(CPRCTypeEntity.COMMENT);
//        cpc.setTarget(articleId);
//        cpc.setTargetType(targetType);
        cpc.setMaxResultCount(10);
        cpc.setSkipCount(0);

        httpUtils.getCommentAll(mHandler, cpc);
    }

}
