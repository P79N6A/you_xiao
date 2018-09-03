package com.youxiao.comment.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.runtoinfo.teacher.CPRCBean.CPRCDataEntity;
import com.runtoinfo.teacher.CPRCBean.CPRCTypeEntity;
import com.runtoinfo.teacher.utils.HttpUtils;
import com.runtoinfo.youxiao.globalTools.utils.DialogMessage;
import com.runtoinfo.youxiao.globalTools.utils.IntentDataType;
import com.youxiao.comment.R;
import com.youxiao.comment.databinding.CommentPublishBinding;

@Route(path = "/comment/publishComment")
public class PublishComment extends BaseActivity {

    public CommentPublishBinding binding;
    public int articleId;
    public int targetType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(PublishComment.this, R.layout.comment_publish);
        initData();
        initEvent();
    }

    public void initData(){
        articleId = getIntent().getExtras().getInt(IntentDataType.ARTICLE);
        targetType = getIntent().getExtras().getInt(IntentDataType.TARGET_TYPE);
    }

    public void initEvent(){
        binding.commentPublishSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogMessage.showBottomDialog(mHandler, PublishComment.this, true);
            }
        });
    }

    public Handler mHandler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    DialogMessage.showToast(PublishComment.this, "请求成功");
                    break;
                case 1:
                    DialogMessage.showBottomDialog(mHandler, PublishComment.this, false);
                    String content = msg.obj.toString();
                    CPRCDataEntity cprcDataEntity = new CPRCDataEntity();
                    cprcDataEntity.setType(CPRCTypeEntity.COMMENT);
                    cprcDataEntity.setTarget(articleId);
                    cprcDataEntity.setTargetType(targetType);

                    HttpUtils.postComment(mHandler, setEntity(content), cprcDataEntity);
                    break;
                case 500:
                    DialogMessage.showToast(PublishComment.this, "请求失败");
                    break;
            }
        }
    };


}
