package com.runtoinfo.youxiao.entity;

import android.graphics.Bitmap;
import android.widget.ImageView;

/**
 * Created by Administrator on 2018/7/5 0005.
 */

public class CourseEntity {

    public String courseName;
    public String courseTime;
    public Bitmap bitmap;

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseTime() {
        return courseTime;
    }

    public void setCourseTime(String courseTime) {
        this.courseTime = courseTime;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
