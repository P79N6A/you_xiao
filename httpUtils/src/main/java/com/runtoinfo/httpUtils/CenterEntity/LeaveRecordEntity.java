package com.runtoinfo.httpUtils.CenterEntity;

/**
 * Created by QiaoJunChao on 2018/9/5.
 */
@SuppressWarnings("all")
public class LeaveRecordEntity {
    public int tenantId;
    public int campusId;
    public int userId;
    public String userName;
    public int askTo;
    public int scheduledCourseId;
    public String reason;
    public String from;
    public String to;
    public int status;
    public String courseName;
    public String className;
    public String date;
    public int id;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
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

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getAskTo() {
        return askTo;
    }

    public void setAskTo(int askTo) {
        this.askTo = askTo;
    }

    public int getScheduledCourseId() {
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
