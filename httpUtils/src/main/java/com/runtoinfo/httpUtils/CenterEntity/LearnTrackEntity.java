package com.runtoinfo.httpUtils.CenterEntity;

/**
 * Created by QiaoJunChao on 2018/9/5.
 */
@SuppressWarnings("all")
public class LearnTrackEntity {
    public int tenantId;
    public int campusId;
    public int userId;
    public int courseId;
    public int lastSectionId;
    public int lastLearnningTime;
    public int progress;
    public int id;

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

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public int getLastSectionId() {
        return lastSectionId;
    }

    public void setLastSectionId(int lastSectionId) {
        this.lastSectionId = lastSectionId;
    }

    public int getLastLearnningTime() {
        return lastLearnningTime;
    }

    public void setLastLearnningTime(int lastLearnningTime) {
        this.lastLearnningTime = lastLearnningTime;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
