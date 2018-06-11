package com.runto.cources.bean;

import java.io.Serializable;

/**
 * 一个简单的bean
 * Created by huanghaibin on 2017/12/4.
 */
@SuppressWarnings("all")
public class Article implements Serializable {
    private int id;
    private String tv_teach_name;
    private String course_teacher_name;
    private String tv_progress;
    private String course_class_progress;
    private String tv_class_room;
    private String course_class_room;
    private String tv_home_work;
    private String course_home_work;
//    private String tv_class_name;
//    private String course_class_name;
    private int image_id;
    private String time;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTv_teach_name() {
        return tv_teach_name;
    }

    public void setTv_teach_name(String tv_teach_name) {
        this.tv_teach_name = tv_teach_name;
    }

    public String getCourse_teacher_name() {
        return course_teacher_name;
    }

    public void setCourse_teacher_name(String course_teacher_name) {
        this.course_teacher_name = course_teacher_name;
    }

    public String getTv_progress() {
        return tv_progress;
    }

    public void setTv_progress(String tv_progress) {
        this.tv_progress = tv_progress;
    }

    public String getCourse_class_progress() {
        return course_class_progress;
    }

    public void setCourse_class_progress(String course_class_progress) {
        this.course_class_progress = course_class_progress;
    }

    public String getTv_class_room() {
        return tv_class_room;
    }

    public void setTv_class_room(String tv_class_room) {
        this.tv_class_room = tv_class_room;
    }

    public String getCourse_class_room() {
        return course_class_room;
    }

    public void setCourse_class_room(String course_class_room) {
        this.course_class_room = course_class_room;
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

    public int getImage_id() {
        return image_id;
    }

    public void setImage_id(int image_id) {
        this.image_id = image_id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
