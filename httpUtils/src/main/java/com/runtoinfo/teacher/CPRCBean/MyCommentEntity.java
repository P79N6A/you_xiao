package com.runtoinfo.teacher.CPRCBean;

/**
 * Created by QiaoJunChao on 2018/9/4.
 */

public class MyCommentEntity {

    public String replyer;
    public String replyTime;
    public String replyerAvatar;
    public String replyContent;
    public int replyedType;
    public int targetType;
    public int targetId;
    public String targetCover;
    public String targetTitle;
    public String targetPublisher;

    public String getReplyer() {
        return replyer;
    }

    public void setReplyer(String replyer) {
        this.replyer = replyer;
    }

    public String getReplyTime() {
        return replyTime;
    }

    public void setReplyTime(String replyTime) {
        this.replyTime = replyTime;
    }

    public String getReplyerAvatar() {
        return replyerAvatar;
    }

    public void setReplyerAvatar(String replyerAvatar) {
        this.replyerAvatar = replyerAvatar;
    }

    public String getReplyContent() {
        return replyContent;
    }

    public void setReplyContent(String replyContent) {
        this.replyContent = replyContent;
    }

    public int getReplyedType() {
        return replyedType;
    }

    public void setReplyedType(int replyedType) {
        this.replyedType = replyedType;
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
