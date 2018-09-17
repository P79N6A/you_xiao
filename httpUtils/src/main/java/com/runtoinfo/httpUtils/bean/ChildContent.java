package com.runtoinfo.httpUtils.bean;

/**
 * Created by QiaoJunChao on 2018/8/23.
 */

@SuppressWarnings("all")
public class ChildContent {

    public int mediaType;
    public String name;
    public boolean isLeaf;
    public String target;

    public int getMediaType() {
        return mediaType;
    }

    public void setMediaType(int mediaType) {
        this.mediaType = mediaType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isLeaf() {
        return isLeaf;
    }

    public void setLeaf(boolean leaf) {
        isLeaf = leaf;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }
}
