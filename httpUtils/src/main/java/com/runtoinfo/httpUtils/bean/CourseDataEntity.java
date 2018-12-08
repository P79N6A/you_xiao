package com.runtoinfo.httpUtils.bean;

/**
 * Created by QiaoJunChao on 2018/8/23.
 */
@SuppressWarnings("all")
public class CourseDataEntity {

    public int tenantId;
    public int campusId;
    public int courseLevel;
    public int courseType;
    public int courseSubject;
    public int courseCategory;
    public String name;
    public String description;
    public String price;
    public String purchasedNumber;
    public String cover;
    public String startTime;
    public String videoPath;
    public String introduction;
    public CourseChildData courseContents;
    public int mediaType;
    public int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTenantId() {
        return tenantId;
    }

    public void setTenantId(int tenantId) {
        this.tenantId = tenantId;
    }

    public int getCampusId() {
        return campusId;
    }

    public void setCampusId(int campusId) {
        this.campusId = campusId;
    }

    public int getCourseLevel() {
        return courseLevel;
    }

    public void setCourseLevel(int courseLevel) {
        this.courseLevel = courseLevel;
    }

    public int getCourseType() {
        return courseType;
    }

    public void setCourseType(int courseType) {
        this.courseType = courseType;
    }

    public int getCourseSubject() {
        return courseSubject;
    }

    public void setCourseSubject(int courseSubject) {
        this.courseSubject = courseSubject;
    }

    public int getCourseCategory() {
        return courseCategory;
    }

    public void setCourseCategory(int courseCategory) {
        this.courseCategory = courseCategory;
    }

    public int getMediaType() {
        return mediaType;
    }

    public void setMediaType(int mediaType) {
        this.mediaType = mediaType;
    }

    public CourseChildData getCourseContents() {
        return courseContents;
    }

    public void setCourseContents(CourseChildData courseContents) {
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
