package com.runtoinfo.youxiao.activities;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.runtoinfo.httpUtils.CPRCBean.CPRCDataEntity;
import com.runtoinfo.httpUtils.CPRCBean.CPRCTypeEntity;
import com.runtoinfo.httpUtils.HttpEntity;
import com.runtoinfo.httpUtils.bean.RequestDataEntity;
import com.runtoinfo.httpUtils.bean.SchoolDynamicsNewEntity;
import com.runtoinfo.httpUtils.utils.HttpUtils;
import com.runtoinfo.youxiao.R;
import com.runtoinfo.youxiao.adapter.SchoolDynamicsRecyclerAdapter;
import com.runtoinfo.youxiao.databinding.SchoolMovmentBinding;
import com.runtoinfo.youxiao.globalTools.utils.DensityUtil;
import com.runtoinfo.youxiao.globalTools.utils.DialogMessage;
import com.runtoinfo.youxiao.globalTools.utils.Entity;
import com.runtoinfo.youxiao.globalTools.utils.IntentDataType;
import com.runtoinfo.youxiao.globalTools.utils.RecyclerViewDecoration;
import com.runtoinfo.youxiao.utils.ScrollCalculatorHelper;
import com.runtoinfo.youxiao.utils.Utils;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.utils.CommonUtil;
import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

import java.util.ArrayList;
import java.util.List;

@Route(path = "/main/schoolDynamics")
public class SchoolDynamics extends BaseActivity {

    public SchoolMovmentBinding binding;
    public SchoolDynamicsRecyclerAdapter adapter;
    public String dataType;
    public int times = 0;
    public SchoolDynamicsNewEntity schoolDynamicsEntity;//传递的数据
    public SchoolDynamicsNewEntity dynamicsNewEntity;//请求接口后返回的数据
    public int type;
    public int targetType;
    public int returnType;
    public HttpUtils httpUtils;
    public List<SchoolDynamicsNewEntity> newDataList = new ArrayList<>();
    public List<SchoolDynamicsNewEntity> tempDataList;

    boolean mFull = false;
    ScrollCalculatorHelper scrollCalculatorHelper;
    public LinearLayoutManager linearLayoutManager;

    public int page;

    @Override
    protected void initView() {
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        binding = DataBindingUtil.setContentView(SchoolDynamics.this, R.layout.school_movment);
        httpUtils = new HttpUtils(this);

        //限定范围为屏幕一半的上下偏移180
        int playTop = CommonUtil.getScreenHeight(this) / 2 - CommonUtil.dip2px(this, 180);
        int playBottom = CommonUtil.getScreenHeight(this) / 2 + CommonUtil.dip2px(this, 180);
        scrollCalculatorHelper = new ScrollCalculatorHelper(R.id.school_dynamics_video, playTop, playBottom);
        changeView();
        initEvent();
    }

    @Override
    protected void setStatusBar() {
        super.setStatusBar();
    }

    public void initEvent() {
        binding.activityImgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (times == 0) {
                    onBackPressed();
                } else {
                    hideView(false);
                    times = 0;
                }
            }
        });

        binding.detailsComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build("/comment/publishComment")
                        .withInt(IntentDataType.ARTICLE, schoolDynamicsEntity.getId())
                        .withInt(IntentDataType.TARGET_TYPE, targetType).navigation();
            }
        });
        /**
         * 收藏点击事件
         */
        binding.detailsCollection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dynamicsNewEntity.isHasFavorited()) {
                    RequestDataEntity requestDataEntity = new RequestDataEntity();
                    requestDataEntity.setToken(spUtils.getString(Entity.TOKEN));
                    requestDataEntity.setUrl(HttpEntity.MAIN_URL + HttpEntity.DELETE_COMMENT_CREATE);
                    requestDataEntity.setId(dynamicsNewEntity.getUserFavoriteId());
                    Log.e("WenzId", "school: " + schoolDynamicsEntity.getId());
                    httpUtils.delectColleciton(handler, requestDataEntity);
                } else {
                    CPRCDataEntity dataEntity = new CPRCDataEntity();
                    dataEntity.setToken(spUtils.getString(Entity.TOKEN));
                    dataEntity.setType(CPRCTypeEntity.COLLECTION);
                    dataEntity.setTarget(schoolDynamicsEntity.getId());
                    Log.e("WenzId", "schools: " + schoolDynamicsEntity.getId());
                    dataEntity.setTargetType(targetType);
                    dataEntity.setUserId(spUtils.getInt(Entity.USER_ID));
                    httpUtils.postComment(handler, dataEntity);
                }
            }
        });

        /**
         * 文章下面点赞
         */
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
                    dataEntity.setTarget(schoolDynamicsEntity.getId());
                    dataEntity.setTargetType(targetType);
                    dataEntity.setUserId(spUtils.getInt(Entity.USER_ID));
                    httpUtils.postComment(handler, dataEntity);
                }
            }
        });

//        binding.schoolRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//
//            int firstVisibleItem, lastVisibleItem;
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//                scrollCalculatorHelper.onScrollStateChanged(recyclerView, newState);
//            }
//
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                firstVisibleItem = linearLayoutManager.findFirstVisibleItemPosition();
//                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
//
//                //这是滑动自动播放的代码
//                if (!mFull) {
//                    scrollCalculatorHelper.onScroll(recyclerView, firstVisibleItem, lastVisibleItem, lastVisibleItem - firstVisibleItem);
//                }
//            }
//        });
    }

    public void changeView() {
        dataType = getIntent().getExtras().getString(IntentDataType.INTENT_KEY);
        if (!TextUtils.isEmpty(dataType)) {
            switch (dataType) {
                case IntentDataType.SCHOOL_DYNAMICS:
                    binding.schoolDynamicsActivityTitle.setText("学校动态");
                    spUtils.setInt(Entity.TARGET_TYPE, 0);
                    type = 0;
                    requestData(type, 1);
                    break;
                case IntentDataType.HEAD_NEWS:
                    binding.schoolDynamicsActivityTitle.setText("新闻头条");
                    spUtils.setInt(Entity.TARGET_TYPE, 0);
                    type = 1;
                    requestData(type, 1);
                    break;
                case IntentDataType.TOPICS:
                    binding.schoolDynamicsActivityTitle.setText("专题详情");
                    spUtils.setInt(Entity.TARGET_TYPE, 1);
                    String result = getIntent().getStringExtra(IntentDataType.DATA);
                    try {
                        schoolDynamicsEntity = new Gson().fromJson(result, new TypeToken<SchoolDynamicsNewEntity>() {}.getType());
                    } catch (JsonSyntaxException e) {
                        Log.e("SchoolDynamic", e.toString());
                    }
                    hideView(true);
                    initTopice();
                    break;
            }
        }
    }

    public void initTopice() {
        binding.dynamicsTitle.setText(schoolDynamicsEntity.getTitle());
        binding.dynamicsTime.setText(schoolDynamicsEntity.getPublishTime());
        //binding.dynamicsReadNumber.setText(schoolDynamicsEntity.getPageView());
        httpUtils.getNewsReadNumber(handler,
                HttpEntity.MAIN_URL + HttpEntity.NEWS_READ_NUMBER,
                spUtils.getString(Entity.TOKEN),
                schoolDynamicsEntity.getId());
        binding.dynamicsWebview.loadData(schoolDynamicsEntity.getContent(), "text/html; charset=UTF-8", null);
    }

    public void initData() {

    }

    public void initRecyclerData() {
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        } else {
            adapter = new SchoolDynamicsRecyclerAdapter(SchoolDynamics.this, newDataList, handler);
            linearLayoutManager = new LinearLayoutManager(this);
            binding.schoolRecyclerView.setLinearLayout();
            binding.schoolRecyclerView.setAdapter(adapter);
            binding.schoolRecyclerView.addItemDecoration(new RecyclerViewDecoration(this, RecyclerViewDecoration.HORIZONTAL_LIST));
            binding.schoolRecyclerView.setOnPullLoadMoreListener(pullLoadMoreListener);
        }
    }

    public PullLoadMoreRecyclerView.PullLoadMoreListener pullLoadMoreListener = new PullLoadMoreRecyclerView.PullLoadMoreListener() {
        @Override
        public void onRefresh() {
            page = 1;
            newDataList.clear();
            requestData(type, page);
        }

        @Override
        public void onLoadMore() {
            page++;
            requestData(type, page);
        }
    };

    public void requestData(int type, int page) {
        RequestDataEntity dataEntity = new RequestDataEntity();
        dataEntity.setUrl(HttpEntity.MAIN_URL + HttpEntity.SCHOOL_NEWS_ALL);
        dataEntity.setType(type);
        dataEntity.setId(DensityUtil.getOffSet(page));
        dataEntity.setToken(spUtils.getString(Entity.TOKEN));
        tempDataList = new ArrayList<>();
        httpUtils.getSchoolNewsAll(handler, dataEntity, tempDataList);
    }

    @Override
    public void onResume() {
        super.onResume();
        GSYVideoManager.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @SuppressLint("HandlerLeak")
    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                case 1:
                case 2://赞，根据请求type决定
                    if (returnType == 1) {
                        dynamicsNewEntity.setHasPraised(true);
                        binding.detailsPraiseImagView.setBackgroundResource(R.drawable.comment_praised);
                        returnType = 0;
                    }
                    break;
                case 6:
                    schoolDynamicsEntity = (SchoolDynamicsNewEntity) msg.obj;
                    times++;
                    hideView(true);

                    RequestDataEntity dataEntity = new RequestDataEntity();
                    dataEntity.setUserId(spUtils.getInt(Entity.USER_ID));
                    dataEntity.setType(targetType);
                    dataEntity.setId(schoolDynamicsEntity.getId());
                    dataEntity.setUrl(HttpEntity.MAIN_URL + HttpEntity.SCHOOL_NEWS_CONTENT);
                    dataEntity.setToken(spUtils.getString(Entity.TOKEN));

                    binding.dynamicsTitle.setText(schoolDynamicsEntity.getTitle());
                    binding.dynamicsTime.setText(schoolDynamicsEntity.getPublishTime());
                    //binding.dynamicsReadNumber.setText(schoolDynamicsEntity.getPageView());
                    httpUtils.getNewsReadNumber(handler,
                            HttpEntity.MAIN_URL + HttpEntity.NEWS_READ_NUMBER,
                            spUtils.getString(Entity.TOKEN),
                            schoolDynamicsEntity.getId());
                    binding.dynamicsWebview.loadData(schoolDynamicsEntity.getContent(), "text/html; charset=UTF-8", null);
                    //binding.dynamicsWebview.loadUrl("http://soft.imtt.qq.com/browser/tes/feedback.html");
                    httpUtils.getNewsDetails(handler, dataEntity);
                    break;
                case 7:
                    dynamicsNewEntity = (SchoolDynamicsNewEntity) msg.obj;
                    if (dynamicsNewEntity.isHasFavorited()) {
                        binding.detailsCollectionImagView.setBackgroundResource(R.drawable.boutique_course_collectioned);
                        binding.detailsCollectionText.setText("已收藏");
                    }

                    if (dynamicsNewEntity.isHasPraised()) {
                        binding.detailsPraiseImagView.setBackgroundResource(R.drawable.comment_praised);
                    }
                    break;
                case 3://收藏，根据请求参数type值获取
                    try {
                        dynamicsNewEntity.setHasFavorited(true);
                        binding.detailsCollectionImagView.setBackgroundResource(R.drawable.boutique_course_collectioned);
                        binding.detailsCollectionText.setText("已收藏");
                    } catch (JsonSyntaxException e) {
                        Log.e("SchoolDynamics", e.toString());
                    }
                    DialogMessage.showToast(SchoolDynamics.this, "收藏成功");
                    break;

                case 5:
                    binding.schoolRecyclerView.setPullLoadMoreCompleted();
                    fromJson();
                    //initRecyclerData();
                    break;
                case 4:
                    binding.dynamicsReadNumber.setText(msg.obj.toString());
                    schoolDynamicsEntity.setPageView(Integer.parseInt(msg.obj.toString()));
                    if (adapter != null) adapter.notifyDataSetChanged();
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
                case 500:
                    Utils.showToast(SchoolDynamics.this, "请检查您的网络");
                    break;
            }
        }
    };

    public void fromJson() {
        int size = tempDataList.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                setDataList(tempDataList.get(i));
            }
            newDataList.addAll(tempDataList);
        }
        if (newDataList.size() > 0) {
            initRecyclerData();
        } else {
            Utils.showToast(SchoolDynamics.this, "没有数据");
        }
    }

    public void setDataList(SchoolDynamicsNewEntity newEntity) {
        int coverType = newEntity.getCoverType();//childItem.getInt("coverType");
        List<String> imageList = newEntity.getCoverImgs();
        if (imageList == null) {
            imageList = new ArrayList<>();
        }
        int imageListSize = imageList.size();
        switch (coverType) {
            case 0://图片
                switch (imageListSize) {
                    case 1:
                    case 2:
                        newEntity.setDataType(1);
                        break;
                    case 3:
                        newEntity.setDataType(2);
                        break;
                    default:
                        newEntity.setDataType(1);
                        break;
                }
                break;
            case 1://视频
                newEntity.setDataType(0);
                break;
        }
    }

    public void hideView(boolean flag) {
        if (flag) {
            binding.schoolRecyclerView.setVisibility(View.GONE);
            binding.dynamicsWebViewLayout.setVisibility(View.VISIBLE);
        } else {
            binding.schoolRecyclerView.setVisibility(View.VISIBLE);
            binding.dynamicsWebViewLayout.setVisibility(View.GONE);
        }

        if (dataType.equals(IntentDataType.SCHOOL_DYNAMICS)) {
            binding.schoolDynamicsActivityTitle.setText(flag ? "动态详情" : "学校动态");
        } else if (dataType.equals(IntentDataType.HEAD_NEWS)) {
            binding.schoolDynamicsActivityTitle.setText(flag ? "头条详情" : "新闻头条");
        }
    }

    @Override
    public void onBackPressed() {

        if (GSYVideoManager.backFromWindowFull(this)) {
            return;
        }
        if (times == 0)
            super.onBackPressed();
        else {
            hideView(false);
            times = 0;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        GSYVideoManager.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        GSYVideoManager.releaseAllVideos();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //如果旋转了就全屏
        if (newConfig.orientation != ActivityInfo.SCREEN_ORIENTATION_USER) {
            mFull = false;
        } else {
            mFull = true;
        }

    }
}
