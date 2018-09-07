package com.runtoinfo.httpUtils.CenterEntity;

/**
 * Created by QiaoJunChao on 2018/9/5.
 */

public class CollectionEntity {

    public int id;
    public int targetType;
    public int targetId;
    public String targetCover;
    public String targetTitle;
    public String targetPublisher;
    public String targetRemark;
    public int targetPrice;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTargetType() {
        return targetType;
    }

    public void setTargetType(int targetType) {
        this.targetType = targetType;
    }

    public int getTargetId() {
        return targetId;
    }

    public void setTargetId(int targetId) {
        this.targetId = targetId;
    }

    public String getTargetCover() {
        return targetCover;
    }

    public void setTargetCover(String targetCover) {
        this.targetCover = targetCover;
    }

    public String getTargetTitle() {
        return targetTitle;
    }

    public void setTargetTitle(String targetTitle) {
        this.targetTitle = targetTitle;
    }

    public String getTargetPublisher() {
        return targetPublisher;
    }

    public void setTargetPublisher(String targetPublisher) {
        this.targetPublisher = targetPublisher;
    }

    public String getTargetRemark() {
        return targetRemark;
    }

    public void setTargetRemark(String targetRemark) {
        this.targetRemark = targetRemark;
    }

    public int getTargetPrice() {
        return targetPrice;
    }

    public void setTargetPrice(int targetPrice) {
        this.targetPrice = targetPrice;
    }
}
