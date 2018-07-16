package com.runtoinfo.youxiao.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.VideoView;

import com.runtoinfo.youxiao.R;
import com.runtoinfo.youxiao.adapter.ViewPageAdapter;
import com.runtoinfo.youxiao.ui.DepthPageTransformer;

import java.util.ArrayList;
import java.util.List;

import myapplication.MyApplication;

/**
 * Created by Administrator on 2018/5/14 0014.
 * 视频引导页/图片引导页
 */

@SuppressLint("Registered")
public class WelcomeActivity extends BaseActivity {

    //public MyVideoView myVideoView;
    public String SdPath;
    public Button player;
    public VideoView myVideoView;
    public List<View> myViewList = new ArrayList<>();
    public ViewPager mViewPager;
    public ImageView mImageView;
    public LinearLayout mLinear;
    private int mDistance;
    private ImageView mOne_dot;
    private ImageView mTwo_dot;
    private ImageView mThree_dot;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_activity);
        compatPermission();//申请权限

        isNextStep();
        //initViews();
        //videoView("http://pic.ibaotu.com/00/20/08/96e888piCHck.mp4");
        //viewPages();
        //setOnClickListener();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.welcome_activity;
    }

    @Override
    protected void initView() {

    }

    /**
     * 申请权限
     */
    public void compatPermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {//检查是否有了权限
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            } else {
                //没有权限即动态申请
                String[] str = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
                ActivityCompat.requestPermissions(this, str, 1);
            }
        }
    }

    public void initViews(){
        myVideoView = findViewById(R.id.welcome_video);
        player = findViewById(R.id.btn_start);
        player.setText(R.string.welcome_experience_now);
        mViewPager = findViewById(R.id.welcome_viewpage);
        mImageView = findViewById(R.id.iv_light_dots);
        mLinear =(LinearLayout) findViewById(R.id.linear_layout);
    }

    /**
     * 视频引导页
     * @param url 视频路径
     */
    public void videoView(String url){
        Uri uri = Uri.parse(url);
        myVideoView.setVideoURI(uri);
        myVideoView.requestFocus();
        myVideoView.start();
    }

    public void setOnClickListener(){
        //为VideoView添加完成事件监听器
        myVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {

            }
        });

        player.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //进入主页
                Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        mOne_dot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewPager.setCurrentItem(0);
            }
        });
        mTwo_dot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewPager.setCurrentItem(1);
            }
        });
        mThree_dot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewPager.setCurrentItem(2);
            }
        });
    }

    /**
     * 图片左右滑动引导页
     */
    public void viewPages(){
        initData();
        mViewPager.setAdapter(new ViewPageAdapter(myViewList));
        addDots();
        moveDots();
        mViewPager.setPageTransformer(true, new DepthPageTransformer());
    }

    public void initData(){
        LayoutInflater inflater = LayoutInflater.from(WelcomeActivity.this);
        myViewList.add(inflater.inflate(R.layout.layout_page1, null));
        myViewList.add(inflater.inflate(R.layout.layout_page2, null));
        myViewList.add(inflater.inflate(R.layout.layout_page3, null));
    }

    private void moveDots() {
        mImageView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                //获得两个圆点之间的距离
                mDistance = mLinear.getChildAt(1).getLeft() - mLinear.getChildAt(0).getLeft();
                mImageView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //页面滚动时小白点移动的距离，并通过setLayoutParams(params)不断更新其位置
                float leftMargin = mDistance * (position + positionOffset);
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mImageView.getLayoutParams();
                params.leftMargin = (int) leftMargin;
                mImageView.setLayoutParams(params);
                if(position==2){
                    player.setVisibility(View.VISIBLE);
                }
                if(position!=2&&player.getVisibility()==View.VISIBLE){
                    player.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageSelected(int position) {
                //页面跳转时，设置小圆点的margin
                float leftMargin = mDistance * position;
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mImageView.getLayoutParams();
                params.leftMargin = (int) leftMargin;
                mImageView.setLayoutParams(params);
                if(position==2){
                    player.setVisibility(View.VISIBLE);
                }
                if(position!=2&&player.getVisibility()==View.VISIBLE){
                    player.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void addDots() {
        mOne_dot = new ImageView(this);
        mOne_dot.setImageResource(R.drawable.gray_dot);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 0, 40, 0);
        mLinear.addView(mOne_dot, layoutParams);
        mTwo_dot = new ImageView(this);
        mTwo_dot.setImageResource(R.drawable.gray_dot);
        mLinear.addView(mTwo_dot, layoutParams);
        mThree_dot = new ImageView(this);
        mThree_dot.setImageResource(R.drawable.gray_dot);
        mLinear.addView(mThree_dot, layoutParams);
        //setClickListener();
    }

    /**
     * 查看是否是第一次打开软件，
     * 如果不是，跳过欢迎页面。
     */
    public void isNextStep()
    {
        MyApplication application = (MyApplication) getApplication();
        boolean isfirst = application.isFirstLogin();
        if (!isfirst)
        {
            startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
            this.finish();
        }
        else
        {
            initViews();
            viewPages();
            setOnClickListener();
        }
    }
}
