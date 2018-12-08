package com.runtoinfo.youxiao.activities;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.runtoinfo.httpUtils.CPRCBean.CPRCDataEntity;
import com.runtoinfo.httpUtils.CPRCBean.CPRCTypeEntity;
import com.runtoinfo.httpUtils.HttpEntity;
import com.runtoinfo.httpUtils.bean.RequestDataEntity;
import com.runtoinfo.httpUtils.bean.SchoolDynamicsNewEntity;
import com.runtoinfo.httpUtils.utils.HttpUtils;
import com.runtoinfo.youxiao.R;
import com.runtoinfo.youxiao.databinding.ActivityTopiceDetailsBinding;
import com.runtoinfo.youxiao.globalTools.utils.Entity;
import com.runtoinfo.youxiao.globalTools.utils.IntentDataType;

/**
 * Created by QiaoJunChao on 2018/12/7.
 */

@Route(path = "/main/topicDetails")
public class TopicDetailsActivity extends BaseActivity {

    public ActivityTopiceDetailsBinding binding;
    public HttpUtils httpUtils;
    public SchoolDynamicsNewEntity schoolDynamicsNewEntity ;
    public SchoolDynamicsNewEntity dynamicsNewEntity;
    public int returnType;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        binding = DataBindingUtil.setContentView(TopicDetailsActivity.this, R.layout.activity_topice_details);
        httpUtils = new HttpUtils(this);
        spUtils.setInt(Entity.TARGET_TYPE, 1);
        String data = getIntent().getExtras().getString(IntentDataType.DATA);
        schoolDynamicsNewEntity = new Gson().fromJson(data, new TypeToken<SchoolDynamicsNewEntity>(){}.getType());
        requestTopice();
        initEvent();
    }

    @Override
    protected void initData() {

    }

    public void requestTopice(){
        RequestDataEntity entity = new RequestDataEntity();
        entity.setId(schoolDynamicsNewEntity.getId());
        entity.setToken(spUtils.getString(Entity.TOKEN));
        entity.setUrl(HttpEntity.MAIN_URL + HttpEntity.TOPICS_DETAILS);

        httpUtils.getTipocDetails(handler, entity);
    }

    public void initEvent(){
        binding.detailsCollection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dynamicsNewEntity.isHasFavorited()) {
                    returnType = 0;
                    RequestDataEntity requestDataEntity = new RequestDataEntity();
                    requestDataEntity.setToken(spUtils.getString(Entity.TOKEN));
                    requestDataEntity.setUrl(HttpEntity.MAIN_URL + HttpEntity.DELETE_COMMENT_CREATE);
                    requestDataEntity.setId(dynamicsNewEntity.getUserFavoriteId());
                    httpUtils.delectColleciton(handler, requestDataEntity);
                } else {
                    CPRCDataEntity dataEntity = new CPRCDataEntity();
                    dataEntity.setToken(spUtils.getString(Entity.TOKEN));
                    dataEntity.setType(CPRCTypeEntity.COLLECTION);
                    dataEntity.setTarget(dynamicsNewEntity.getId());
                    dataEntity.setTargetType(1);
                    dataEntity.setUserId(spUtils.getInt(Entity.USER_ID));
                    httpUtils.postComment(handler, dataEntity);
                }
            }
        });

        binding.detailsPraise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnType = 1;
                if (dynamicsNewEntity.isHasPraised()) {
                    RequestDataEntity requestDataEntity = new RequestDataEntity();
                    requestDataEntity.setToken(spUtils.getString(Entity.TOKEN));
                    requestDataEntity.setUrl(HttpEntity.MAIN_URL + HttpEntity.DELETE_COMMENT_CREATE);
                    requestDataEntity.setId(dynamicsNewEntity.getUserPraiseId());
                    httpUtils.delectColleciton(handler, requestDataEntity);
                } else {
                    CPRCDataEntity dataEntity = new CPRCDataEntity();
                    dataEntity.setToken(spUtils.getString(Entity.TOKEN));
                    dataEntity.setType(CPRCTypeEntity.PRAISE);
                    dataEntity.setTarget(dynamicsNewEntity.getId());
                    dataEntity.setTargetType(1);
                    dataEntity.setUserId(spUtils.getInt(Entity.USER_ID));
                    httpUtils.postComment(handler, dataEntity);
                }
            }
        });

        binding.detailsComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build("/comment/publishComment")
                        .withInt(IntentDataType.ARTICLE, dynamicsNewEntity.getId())
                        .withInt(IntentDataType.TARGET_TYPE, 1).navigation();
            }
        });
    }

    public Handler handler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    dynamicsNewEntity = (SchoolDynamicsNewEntity) msg.obj;
                    binding.topicTitle.setText(dynamicsNewEntity.getTitle());
                    binding.topicTime.setText(dynamicsNewEntity.getPublishTime());

                    httpUtils.getNewsReadNumber(handler,
                            HttpEntity.MAIN_URL + HttpEntity.NEWS_READ_NUMBER,
                            spUtils.getString(Entity.TOKEN),
                            dynamicsNewEntity.getId());
                    binding.topicWebview.loadData(dynamicsNewEntity.getContent(), "text/html; charset=UTF-8", null);

                    if (dynamicsNewEntity.isHasFavorited()) {
                        binding.detailsCollectionImagView.setBackgroundResource(R.drawable.boutique_course_collectioned);
                        binding.detailsCollectionText.setText("已收藏");
                    }

                    if (dynamicsNewEntity.isHasPraised()) {
                        binding.detailsPraiseImagView.setBackgroundResource(R.drawable.comment_praised);
                    }
                    break;
                case 2:
                    dynamicsNewEntity.setHasPraised(true);
                    binding.detailsPraiseImagView.setBackgroundResource(R.drawable.comment_praised);
                    break;
                case 4:
                    binding.topicReadNumber.setText(msg.obj.toString());
                    break;
                case 200:
                    if (returnType == 1) {
                        binding.detailsPraiseImagView.setBackgroundResource(R.drawable.dynamics_z);
                        dynamicsNewEntity.setHasPraised(false);
                    } else {
                        dynamicsNewEntity.setHasFavorited(false);
                        binding.detailsCollectionImagView.setBackgroundResource(R.drawable.dynamics_collection);
                        binding.detailsCollectionText.setText("收藏");
                    }
                    break;
            }
        }
    };
}
