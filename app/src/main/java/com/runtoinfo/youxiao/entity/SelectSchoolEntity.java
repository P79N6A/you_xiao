package com.runtoinfo.youxiao.entity;

import android.graphics.drawable.Drawable;

import java.util.List;

/**
 * Created by QiaoJunChao on 2018/8/6.
 */

public class SelectSchoolEntity {

    public String orgName;
    public List<String> schoolName;
    public String tenancyName;
    public int id;
    public String tenantId;
    public String imgPath;

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getTenancyName() {
        return tenancyName;
    }

    public void setTenancyName(String tenancyName) {
        this.tenancyName = tenancyName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public List<String> getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(List<String> schoolName) {
        this.schoolName = schoolName;
    }
}
