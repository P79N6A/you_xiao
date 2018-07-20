package com.runto.cources.bean;

import java.io.Serializable;

/**
 * 一个简单的bean
 * Created by huanghaibin on 2017/12/4.
 */
@SuppressWarnings("all")
public class Article implements Serializable {
    private int id;
    private String tv_time;
    private String course_time;
    private String tv_teacher;
    private String course_teacher;
    private String tv_progress;
    private int course_progress;
    private String tv_address;
    private String course_address;
    private String tv_home_work;
    private String course_home_work;
    private String course_progress_num;

    public String getCourse_progress_num() {
        return course_progress_num;
    }

    public void setCourse_progress_num(String course_progress_num) {
        this.course_progress_num = course_progress_num;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTv_time() {
        return tv_time;
    }

    public void setTv_time(String tv_time) {
        this.tv_time = tv_time;
    }

    public String getCourse_time() {
        return course_time;
    }

    public void setCourse_time(String course_time) {
        this.course_time = course_time;
    }

    public String getTv_teacher() {
        return tv_teacher;
    }

    public void setTv_teacher(String tv_teacher) {
        this.tv_teacher = tv_teacher;
    }

    public String getCourse_teacher() {
        return course_teacher;
    }

    public void setCourse_teacher(String course_teacher) {
        this.course_teacher = course_teacher;
    }

    public String getTv_progress() {
        return tv_progress;
    }

    public void setTv_progress(String tv_progress) {
        this.tv_progress = tv_progress;
    }

    public int getCourse_progress() {
        return course_progress;
    }

    public void setCourse_progress(int course_progress) {
        this.course_progress = course_progress;
    }

    public String getTv_address() {
        return tv_address;
    }

    public void setTv_address(String tv_address) {
        this.tv_address = tv_address;
    }

    public String getCourse_address() {
        return course_address;
    }

    public void setCourse_address(String course_address) {
        this.course_address = course_address;
    }

    public String getTv_home_work() {
        return tv_home_work;
    }

    public void setTv_home_work(String tv_home_work) {
        this.tv_home_work = tv_home_work;
    }

    public String getCourse_home_work() {
        return course_home_work;
    }

    public void setCourse_home_work(String course_home_work) {
        this.course_home_work = course_home_work;
    }
}
