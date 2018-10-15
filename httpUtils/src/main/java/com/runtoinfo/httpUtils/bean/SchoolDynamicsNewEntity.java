package com.runtoinfo.httpUtils.bean;

import java.util.List;

/**
 * Created by QiaoJunChao on 2018/10/8.
 */
@SuppressWarnings("all")
public class SchoolDynamicsNewEntity {
    public int tenantId;
    public int campusId;
    public String title;
    public String subtitle;
    public List<String> coverImgs;
    public int coverType;
    public String videoPath;
    public String content;
    public int tag;
    public int type;
    public int status;
    public int publisher;
    public String publishTime;
    public int pageView;
    public int praiseNumber;
    public int commentNumber;
    public int replyNumber;
    public boolean hasPraised;
    public int userPraiseId;
    public boolean hasFavorited;
    public int userFavoriteId;
    public int id;
    public int dataType;//用于区分布局类型

    public int getDataType() {
        return dataType;
    }

    public void setDataType(int dataType) {
        this.dataType = dataType;
    }

    public int getTenantId() {
        return tenantId;
    }

    public void setTenantId(int tenantId) {
        this.tenantId = tenantId;
    }

    public int getCampusId() {
        return campusId;
    }

    public void setCampusId(int campusId) {
        this.campusId = campusId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public List<String> getCoverImgs() {
        return coverImgs;
    }

    public void setCoverImgs(List<String> coverImgs) {
        this.coverImgs = coverImgs;
    }

    public int getCoverType() {
        return coverType;
    }

    public void setCoverType(int coverType) {
        this.coverType = coverType;
    }

    public String getVideoPath() {
        return videoPath;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getPublisher() {
        return publisher;
    }

    public void setPublisher(int publisher) {
        this.publisher = publisher;
    }

    public String getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }

    public int getPageView() {
        return pageView;
    }

    public void setPageView(int pageView) {
        this.pageView = pageView;
    }

    public int getPraiseNumber() {
        return praiseNumber;
    }

    public void setPraiseNumber(int praiseNumber) {
        this.praiseNumber = praiseNumber;
    }

    public int getCommentNumber() {
        return commentNumber;
    }

    public void setCommentNumber(int commentNumber) {
        this.commentNumber = commentNumber;
    }

    public int getReplyNumber() {
        return replyNumber;
    }

    public void setReplyNumber(int replyNumber) {
        this.replyNumber = replyNumber;
    }

    public boolean isHasPraised() {
        return hasPraised;
    }

    public void setHasPraised(boolean hasPraised) {
        this.hasPraised = hasPraised;
    }

    public int getUserPraiseId() {
        return userPraiseId;
    }

    public void setUserPraiseId(int userPraiseId) {
        this.userPraiseId = userPraiseId;
    }

    public boolean isHasFavorited() {
        return hasFavorited;
    }

    public void setHasFavorited(boolean hasFavorited) {
        this.hasFavorited = hasFavorited;
    }

    public int getUserFavoriteId() {
        return userFavoriteId;
    }

    public void setUserFavoriteId(int userFavoriteId) {
        this.userFavoriteId = userFavoriteId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
