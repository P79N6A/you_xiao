package com.runtoinfo.httpUtils.bean;

import java.util.List;

/**
 * Created by QiaoJunChao on 2018/8/28.
 */
@SuppressWarnings("all")
public class HandWorkEntity {

    public int courseId;
    public int courseInsId;
    public int studentId;
    public int userId;
    public String remark;
    public List<WorkItems> workItems;

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public int getCourseInsId() {
        return courseInsId;
    }

    public void setCourseInsId(int courseInsId) {
        this.courseInsId = courseInsId;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<WorkItems> getWorkItems() {
        return workItems;
    }

    public void setWorkItems(List<WorkItems> workItems) {
        this.workItems = workItems;
    }

    public static class WorkItems{
        public int type;
        public String path;

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }
    }
}
