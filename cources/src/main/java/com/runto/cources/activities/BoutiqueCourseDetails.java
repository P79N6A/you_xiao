package com.runto.cources.activities;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.runto.cources.R;
import com.runto.cources.databinding.ActivityBoutiqueCourseDetailsBinding;
import com.runto.cources.fragment.CourseIntroductionFragment;
import com.runto.cources.fragment.CourseListFragment;
import com.runtoinfo.httpUtils.bean.CourseDataEntity;
import com.runtoinfo.httpUtils.utils.HttpUtils;
import com.runtoinfo.youxiao.globalTools.adapter.CommonViewPagerAdapter;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder;
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack;
import com.shuyu.gsyvideoplayer.listener.LockClickListener;
import com.shuyu.gsyvideoplayer.utils.Debuger;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("all")
@Route(path = "/course/boutiqueCourseDetails")
public class BoutiqueCourseDetails extends BaseActivity {

    public ActivityBoutiqueCourseDetailsBinding binding;
    public List<String> titles = new ArrayList<>();
    public List<Fragment> fragmentList = new ArrayList<>();
    public CommonViewPagerAdapter viewPagerAdapter;
    public int clickCount = 0;
    public View view;
    public SensorManager sensorManager;
    public CourseDataEntity courseDataEntity;
    public HttpUtils httpUtils;

    public OrientationUtils orientationUtils;
    public boolean isPlay;
    public boolean isPause;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_boutique_course_details);
        httpUtils = new HttpUtils(this);
        courseDataEntity = new Gson().fromJson(getIntent().getExtras().getString("json"), new TypeToken<CourseDataEntity>(){}.getType());
    }

    @Override
    public void setStatusBar() {
        super.setStatusBar();
    }

    @SuppressLint("SetTextI18n")
    public void initData(){

        //sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        //sensorEventListener = new JZVideoPlayerStandard.JZAutoFullscreenListener();

        if (courseDataEntity != null){
            binding.boutiqueCourseName.setText(courseDataEntity.getName());
            httpUtils.postPhoto(this, courseDataEntity.getCover(), binding.boutiqueCourseDetailsImageView);
            //binding.boutiqueCourseOpenTime.setText(courseDataEntity.getStartTime().split("T")[0] + "开课");
            binding.boutiqueCoursePurchaseNumber.setText(courseDataEntity.getPurchasedNumber() + "人购买");
            binding.boutiqueCoursePrice.setText("¥" + courseDataEntity.getPrice());
        }

        String[] title = new String[]{"介绍","目录"};
        titles.addAll(Arrays.asList(title));

        fragmentList.add(new CourseIntroductionFragment(courseDataEntity.getIntroduction()));
        fragmentList.add(new CourseListFragment(courseDataEntity.getCourseContents()));

        viewPagerAdapter = new CommonViewPagerAdapter(getSupportFragmentManager(), fragmentList, titles);
        binding.boutiqueCourseIntroductionVpager.setAdapter(viewPagerAdapter);
        binding.boutiqueCourseIntroductionTablayout.setupWithViewPager(binding.boutiqueCourseIntroductionVpager);

        binding.collectionLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickCount == 0) {
                    clickCount++;
                    binding.boutiqueCourseCollectionImage.setImageResource(R.drawable.boutique_course_collectioned);
                }else{
                    clickCount = 0;
                    binding.boutiqueCourseCollectionImage.setImageResource(R.drawable.boutique_course_collection);
                }
            }
        });

        binding.courseLeaveBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }
    /**
     * 视频引导页
     * @param url 视频路径
     */
    public void videoView(String url){
//        binding.boutiqueCourseDetailsImageView.setVisibility(View.GONE);
//        binding.boutiqueJzvideoStandard.setVisibility(View.VISIBLE);
//        binding.boutiqueJzvideoStandard.setUp(url, JZVideoPlayerStandard.SCREEN_WINDOW_NORMAL, "视频课程");
//        Glide.with(this).load(courseDataEntity.getCover()).into(binding.boutiqueJzvideoStandard.thumbImageView);
    }

    public void initGSYVideoView(String url){

        binding.boutiqueCourseDetailsImageView.setVisibility(View.GONE);
        binding.boutiqueJzvideoStandard.setVisibility(View.VISIBLE);
        //外部辅助的旋转，帮助全屏
        orientationUtils = new OrientationUtils(this, binding.boutiqueJzvideoStandard);
        //初始化不打开外部的旋转
        orientationUtils.setEnable(false);

        //Glide.with(this).load(courseDataEntity.getCover()).into((ImageView) binding.boutiqueJzvideoStandard.getThumbImageView());

        GSYVideoOptionBuilder gsyVideoOption = new GSYVideoOptionBuilder();
        gsyVideoOption.setIsTouchWiget(true)
                .setRotateViewAuto(false)
                .setLockLand(false)
                .setAutoFullWithSize(true)
                .setShowFullAnimation(false)
                .setNeedLockFull(true)
                .setUrl(url)
                .setCacheWithPlay(false)
                .setVideoTitle("测试视频")
                .setVideoAllCallBack(new GSYSampleCallBack() {
                    @Override
                    public void onPrepared(String url, Object... objects) {
                        super.onPrepared(url, objects);
                        //开始播放了才能旋转和全屏
                        orientationUtils.setEnable(true);
                        isPlay = true;
                    }

                    @Override
                    public void onQuitFullscreen(String url, Object... objects) {
                        super.onQuitFullscreen(url, objects);
                        Debuger.printfError("***** onQuitFullscreen **** " + objects[0]);//title
                        Debuger.printfError("***** onQuitFullscreen **** " + objects[1]);//当前非全屏player
                        if (orientationUtils != null) {
                            orientationUtils.backToProtVideo();
                        }
                    }
                }).setLockClickListener(new LockClickListener() {
            @Override
            public void onClick(View view, boolean lock) {
                if (orientationUtils != null) {
                    //配合下方的onConfigurationChanged
                    orientationUtils.setEnable(!lock);
                }
            }
        }).build(binding.boutiqueJzvideoStandard);

        binding.boutiqueJzvideoStandard.getFullscreenButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //直接横屏
                orientationUtils.resolveByClick();

                //第一个true是否需要隐藏actionbar，第二个true是否需要隐藏statusbar
                binding.boutiqueJzvideoStandard.startWindowFullscreen(BoutiqueCourseDetails.this, true, true);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (orientationUtils != null) {
            orientationUtils.backToProtVideo();
        }
        if (GSYVideoManager.backFromWindowFull(this)) {
            return;
        }
        super.onBackPressed();
    }
    @Override
    protected void onPause() {
        binding.boutiqueJzvideoStandard.getCurrentPlayer().onVideoPause();
        super.onPause();
        isPause = true;
    }

    @Override
    protected void onResume() {
        binding.boutiqueJzvideoStandard.getCurrentPlayer().onVideoResume(false);
        super.onResume();
        isPause = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isPlay) {
            binding.boutiqueJzvideoStandard.getCurrentPlayer().release();
        }
        if (orientationUtils != null)
            orientationUtils.releaseListener();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //如果旋转了就全屏
        if (isPlay && !isPause) {
            binding.boutiqueJzvideoStandard.onConfigurationChanged(this, newConfig, orientationUtils, true, true);
        }
    }


}
