package com.runtoinfo.httpUtils.bean;

/**
 * Created by QiaoJunChao on 2018/8/29.
 */

public class LeaveEntity {
    public int userId;
    public int askTo;
    public int scheduledCourseId;
    public String reason;
    public String from;
    public String to;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getAskTo() {
        return askTo;
    }

    public void setAskTo(int askTo) {
        this.askTo = askTo;
    }

    public int getScheduledCourseId(int courseId) {
        return scheduledCourseId;
    }

    public void setScheduledCourseId(int scheduledCourseId) {
        this.scheduledCourseId = scheduledCourseId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }
}
