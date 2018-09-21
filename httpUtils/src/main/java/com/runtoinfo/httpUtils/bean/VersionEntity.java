package com.runtoinfo.httpUtils.bean;

/**
 * Created by QiaoJunChao on 2018/9/19.
 */

public class VersionEntity {
    public String name;
    public String version;
    public String packages;
    public String description;
    public int platform;
    public String publishDate;
    public String iOSUpgradePath;
    public int id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getPackages() {
        return packages;
    }

    public void setPackages(String packages) {
        this.packages = packages;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPlatform() {
        return platform;
    }

    public void setPlatform(int platform) {
        this.platform = platform;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }

    public String getiOSUpgradePath() {
        return iOSUpgradePath;
    }

    public void setiOSUpgradePath(String iOSUpgradePath) {
        this.iOSUpgradePath = iOSUpgradePath;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
