package com.runtoinfo.httpUtils.bean;

import java.util.List;

/**
 * Created by QiaoJunChao on 2018/8/23.
 */
@SuppressWarnings("all")
public class CourseDataEntity {

    public String name;
    public String description;
    public String price;
    public String purchasedNumber;
    public String cover;
    public String startTime;
    public String videoPath;
    public String introduction;
    public List<ChildContent> courseContents;
    public int mediaType;

    public int getMediaType() {
        return mediaType;
    }

    public void setMediaType(int mediaType) {
        this.mediaType = mediaType;
    }

    public List<ChildContent> getCourseContents() {
        return courseContents;
    }

    public void setCourseContents(List<ChildContent> courseContents) {
        this.courseContents = courseContents;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPurchasedNumber() {
        return purchasedNumber;
    }

    public void setPurchasedNumber(String purchasedNumber) {
        this.purchasedNumber = purchasedNumber;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getVideoPath() {
        return videoPath;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }
}
