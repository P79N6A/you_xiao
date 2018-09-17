package com.runtoinfo.httpUtils.bean;

/**
 * Created by QiaoJunChao on 2018/8/24.
 */
@SuppressWarnings("all")
public class HomeCourseEntity {
    public int courseInstId;
    public int classId;
    public String className;
    public int courseId;
    public String courseName;
    public String beginTime;
    public String coverPhoto;
    public boolean isSignIn;
    public String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getCourseInstId() {
        return courseInstId;
    }

    public void setCourseInstId(int courseInstId) {
        this.courseInstId = courseInstId;
    }

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getCoverPhoto() {
        return coverPhoto;
    }

    public void setCoverPhoto(String coverPhoto) {
        this.coverPhoto = coverPhoto;
    }

    public boolean isSignIn() {
        return isSignIn;
    }

    public void setSignIn(boolean signIn) {
        isSignIn = signIn;
    }
}
