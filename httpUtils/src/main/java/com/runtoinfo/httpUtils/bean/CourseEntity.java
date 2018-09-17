package com.runtoinfo.httpUtils.bean;

/**
 * 课程表
 * Created by QiaoJunChao on 2018/9/11.
 */
@SuppressWarnings("all")
public class CourseEntity {
    public int courseInstId;
    public String date;
    public int dayOfWeek;
    public int courseId;
    public String courseName;
    public String startTime;
    public String endTime;
    public int classroomId;
    public String classroomName;
    public int teacherId;
    public String teacherName;
    public int classOrStudentId;
    public String className;
    public int progress;
    public String homeworkRequirement;
    public String coverPhoto;
    public boolean isSignIn;
    public String courseMessage;
    public int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getCourseMessage() {
        return courseMessage;
    }

    public void setCourseMessage(String courseMessage) {
        this.courseMessage = courseMessage;
    }

    public int getCourseInstId() {
        return courseInstId;
    }

    public void setCourseInstId(int courseInstId) {
        this.courseInstId = courseInstId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
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

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getClassroomId() {
        return classroomId;
    }

    public void setClassroomId(int classroomId) {
        this.classroomId = classroomId;
    }

    public String getClassroomName() {
        return classroomName;
    }

    public void setClassroomName(String classroomName) {
        this.classroomName = classroomName;
    }

    public int getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public int getClassOrStudentId() {
        return classOrStudentId;
    }

    public void setClassOrStudentId(int classOrStudentId) {
        this.classOrStudentId = classOrStudentId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public String getHomeworkRequirement() {
        return homeworkRequirement;
    }

    public void setHomeworkRequirement(String homeworkRequirement) {
        this.homeworkRequirement = homeworkRequirement;
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
