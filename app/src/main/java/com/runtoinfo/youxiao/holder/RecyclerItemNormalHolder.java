package com.runtoinfo.youxiao.holder;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.runtoinfo.httpUtils.bean.SchoolDynamicsNewEntity;
import com.runtoinfo.youxiao.databinding.SchoolDynamicsOnePicLayoutBinding;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder;
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by QiaoJunChao on 2018/11/23.
 */

public class RecyclerItemNormalHolder extends RecyclerItemBaseHolder {

    public final static String TAG = "RecyclerView2List";
    protected Context context = null;
    private ImageView imageView;
    private GSYVideoOptionBuilder gsyVideoOptionBuilder;

    public SchoolDynamicsOnePicLayoutBinding binding;
    public RecyclerItemNormalHolder(Context context, View v) {
        super(v);
        this.context = context;
        imageView = new ImageView(context);
        binding = DataBindingUtil.getBinding(v);
        gsyVideoOptionBuilder = new GSYVideoOptionBuilder();
    }

    public void onBind(final int position, SchoolDynamicsNewEntity videoModel) {

        //增加封面
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        binding.schoolOneReader.setText(videoModel.getPageView());
        binding.schoolOneTitle.setText(videoModel.getTitle());
        {
            /**
             * 增加获取封面代码逻辑
             */
            List<String> images = videoModel.getCoverImgs();
            if (images != null && images.size() > 0) {
                Glide.with(context).load(videoModel.getCoverImgs().get(0)).into(imageView);
            }
        }
        if (imageView.getParent() != null) {
            ViewGroup viewGroup = (ViewGroup) imageView.getParent();
            viewGroup.removeView(imageView);
        }
        /**
         * 视频url
         */
        String url = videoModel.getVideoPath();

        Map<String, String> header = new HashMap<>();
        header.put("ee", "33");

        //防止错位，离开释放
        //gsyVideoPlayer.initUIState();
        gsyVideoOptionBuilder
                .setIsTouchWiget(false)
                .setThumbImageView(imageView)
                .setUrl(url)
                //.setVideoTitle(title)
                .setCacheWithPlay(false)
                .setRotateViewAuto(true)
                .setLockLand(true)
                .setPlayTag(String.valueOf(position))
                .setMapHeadData(header)
                .setShowFullAnimation(true)
                .setNeedLockFull(true)
                .setPlayPosition(position)
                .setVideoAllCallBack(new GSYSampleCallBack() {
                    @Override
                    public void onPrepared(String url, Object... objects) {
                        super.onPrepared(url, objects);
                        if (!binding.schoolDynamicsVideo.isIfCurrentIsFullscreen()) {
                            //静音
                            GSYVideoManager.instance().setNeedMute(true);
                        }
                    }

                    @Override
                    public void onQuitFullscreen(String url, Object... objects) {
                        super.onQuitFullscreen(url, objects);
                        //全屏不静音
                        GSYVideoManager.instance().setNeedMute(true);
                    }

                    @Override
                    public void onEnterFullscreen(String url, Object... objects) {
                        super.onEnterFullscreen(url, objects);
                        GSYVideoManager.instance().setNeedMute(false);
                        binding.schoolDynamicsVideo.getCurrentPlayer().getTitleTextView().setText((String)objects[0]);
                    }
                }).build(binding.schoolDynamicsVideo);


        //增加title
        binding.schoolDynamicsVideo.getTitleTextView().setVisibility(View.GONE);

        //设置返回键
        binding.schoolDynamicsVideo.getBackButton().setVisibility(View.GONE);

        //设置全屏按键功能
        binding.schoolDynamicsVideo.getFullscreenButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resolveFullBtn(binding.schoolDynamicsVideo);
            }
        });
    }

    /**
     * 全屏幕按键处理
     */
    private void resolveFullBtn(final StandardGSYVideoPlayer standardGSYVideoPlayer) {
        standardGSYVideoPlayer.startWindowFullscreen(context, true, true);
    }
}
