package com.runtoinfo.httpUtils.CPRCBean;

/**
 * Created by QiaoJunChao on 2018/9/5.
 */

public class PraiseEntity {

    public String praiser;
    public String praiseTime;
    public String praiseAvatar;
    public int praisedType;
    public String praisedContent;
    public int targetType;
    public int targetId;
    public String targetCover;
    public String targetTitle;
    public String targetPublisher;

    public String getPraiser() {
        return praiser;
    }

    public void setPraiser(String praiser) {
        this.praiser = praiser;
    }

    public String getPraiseTime() {
        return praiseTime;
    }

    public void setPraiseTime(String praiseTime) {
        this.praiseTime = praiseTime;
    }

    public String getPraiseAvatar() {
        return praiseAvatar;
    }

    public void setPraiseAvatar(String praiseAvatar) {
        this.praiseAvatar = praiseAvatar;
    }

    public int getPraisedType() {
        return praisedType;
    }

    public void setPraisedType(int praisedType) {
        this.praisedType = praisedType;
    }

    public String getPraisedContent() {
        return praisedContent;
    }

    public void setPraisedContent(String praisedContent) {
        this.praisedContent = praisedContent;
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
}
