package com.runto.cources.bean;

import android.widget.ImageView;
import android.widget.VideoView;

/**
 * Created by QiaoJunChao on 2018/8/27.
 */
@SuppressWarnings("all")
public class HomeWorkEntity {
    public ImageView imageView;
    public VideoView videoView;

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public VideoView getVideoView() {
        return videoView;
    }

    public void setVideoView(VideoView videoView) {
        this.videoView = videoView;
    }
}
