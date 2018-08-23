package com.runtoinfo.youxiao.entity;

import android.graphics.Bitmap;

import java.util.List;

/**
 * Created by QiaoJunChao on 2018/8/15.
 */

public class SchoolDynamicsEntity {
    public int type;
    public String tile;
    public String message;
    public int readNumber;
    public List<String> imagList;
    public int id;

    public String videoPath;
    //是否是视频
    public int coverType;
    //新闻或者头条
    public int dataType;
    //发布状态
    public String status;
    public String content;
    public String publishTime;

    public String getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getVideoPath() {
        return videoPath;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }

    public int getCoverType() {
        return coverType;
    }

    public void setCoverType(int coverType) {
        this.coverType = coverType;
    }

    public int getDataType() {
        return dataType;
    }

    public void setDataType(int dataType) {
        this.dataType = dataType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTile() {
        return tile;
    }

    public void setTile(String tile) {
        this.tile = tile;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getReadNumber() {
        return readNumber;
    }

    public void setReadNumber(int readNumber) {
        this.readNumber = readNumber;
    }

    public List<String> getImagList() {
        return imagList;
    }

    public void setImagList(List<String> imagList) {
        this.imagList = imagList;
    }
}
