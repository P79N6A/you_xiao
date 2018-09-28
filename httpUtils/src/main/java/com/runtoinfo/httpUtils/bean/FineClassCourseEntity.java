package com.runtoinfo.httpUtils.bean;

/**
 * Created by QiaoJunChao on 2018/9/27.
 */

public class FineClassCourseEntity {

    public String code;
    public String name;
    public String description;
    public int parentId;
    public int categoryId;
    public String categoryName;
    public int tenantId;
    public boolean isHostData;
    public boolean isTenantData;
    public boolean isTemplateData;
    public int id;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getTenantId() {
        return tenantId;
    }

    public void setTenantId(int tenantId) {
        this.tenantId = tenantId;
    }

    public boolean isHostData() {
        return isHostData;
    }

    public void setHostData(boolean hostData) {
        isHostData = hostData;
    }

    public boolean isTenantData() {
        return isTenantData;
    }

    public void setTenantData(boolean tenantData) {
        isTenantData = tenantData;
    }

    public boolean isTemplateData() {
        return isTemplateData;
    }

    public void setTemplateData(boolean templateData) {
        isTemplateData = templateData;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
