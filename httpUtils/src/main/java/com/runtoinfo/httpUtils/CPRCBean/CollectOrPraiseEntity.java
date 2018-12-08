package com.runtoinfo.httpUtils.CPRCBean;

/**
 * Created by QiaoJunChao on 2018/12/6.
 */

public class CollectOrPraiseEntity {

    public int target;
    public int targetType;
    public int userId;
    public boolean hasPraise;
    public boolean hasFavorite;
    public int favoriteId;
    public int praiseId;

    public int getPraiseId() {
        return praiseId;
    }

    public void setPraiseId(int praiseId) {
        this.praiseId = praiseId;
    }

    public int getFavoriteId() {
        return favoriteId;
    }

    public void setFavoriteId(int favoriteId) {
        this.favoriteId = favoriteId;
    }

    public int getTarget() {
        return target;
    }

    public void setTarget(int target) {
        this.target = target;
    }

    public int getTargetType() {
        return targetType;
    }

    public void setTargetType(int targetType) {
        this.targetType = targetType;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public boolean getHasPraise() {
        return hasPraise;
    }

    public void setHasPraise(boolean hasPraise) {
        this.hasPraise = hasPraise;
    }

    public boolean getHasFavorite() {
        return hasFavorite;
    }

    public void setHasFavorite(boolean hasFavorite) {
        this.hasFavorite = hasFavorite;
    }
}
