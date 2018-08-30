package com.runtoinfo.teacher.bean;

/**
 * Created by QiaoJunChao on 2018/8/30.
 */

public class TopicsEntity {

    public String imagePath;
    public String title;
    public String msg;
    public int read;
    public int replay;
    public int comment;
    public int praise;

    public int getPraise() {
        return praise;
    }

    public void setPraise(int praise) {
        this.praise = praise;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getRead() {
        return read;
    }

    public void setRead(int read) {
        this.read = read;
    }

    public int getReplay() {
        return replay;
    }

    public void setReplay(int replay) {
        this.replay = replay;
    }

    public int getComment() {
        return comment;
    }

    public void setComment(int comment) {
        this.comment = comment;
    }
}
