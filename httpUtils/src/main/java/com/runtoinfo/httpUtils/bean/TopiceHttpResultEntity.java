package com.runtoinfo.httpUtils.bean;

import java.util.List;

/**
 * Created by QiaoJunChao on 2018/8/30.
 */
@SuppressWarnings("all")
public class TopiceHttpResultEntity {

    public int tenantId;
    public String campusId;
    public String title;
    public String subtitle;
    public List<String> coverImgs;
    public String coverType;
    public String videoPath;
    public String content;
    public String tag;
    public String type;
    public String status;
    public String publisher;
    public String publishTime;
    public String pageView;
    public String praiseNumber;
    public String commentNumber;
    public String replyNumber;
    public int id;

    public int getTenantId() {
        return tenantId;
    }

    public void setTenantId(int tenantId) {
        this.tenantId = tenantId;
    }

    public String getCampusId() {
        return campusId;
    }

    public void setCampusId(String campusId) {
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

    public String getCoverType() {
        return coverType;
    }

    public void setCoverType(String coverType) {
        this.coverType = coverType;
    }

    public String getVideoPath() {
        return videoPath;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }

    public String getContentpublic() {
        return content;
    }

    public void setContentpublic(String contentpublic) {
        this.content = contentpublic;
    }

    public String getTagpublic() {
        return tag;
    }

    public void setTagpublic(String tagpublic) {
        this.tag = tagpublic;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }

    public String getPageView() {
        return pageView;
    }

    public void setPageView(String pageView) {
        this.pageView = pageView;
    }

    public String getPraiseNumber() {
        return praiseNumber;
    }

    public void setPraiseNumber(String praiseNumber) {
        this.praiseNumber = praiseNumber;
    }

    public String getCommentNumber() {
        return commentNumber;
    }

    public void setCommentNumber(String commentNumber) {
        this.commentNumber = commentNumber;
    }

    public String getReplyNumber() {
        return replyNumber;
    }

    public void setReplyNumber(String replyNumber) {
        this.replyNumber = replyNumber;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
