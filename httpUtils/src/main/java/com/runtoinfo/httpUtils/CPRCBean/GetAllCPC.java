package com.runtoinfo.httpUtils.CPRCBean;

/**
 * Created by QiaoJunChao on 2018/9/3.
 */

public class GetAllCPC {
    public int UserId;
    public int Type;
    public int Target;
    public int TargetType;
    public int ParentType;
    public int ParentId;
    public int MaxResultCount;
    public int SkipCount;
    public int Sorting;
    public boolean IsEmptyContent;
    public String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getUserId() {
        return UserId;
    }

    public void setUserId(int userId) {
        UserId = userId;
    }

    public int getType() {
        return Type;
    }

    public void setType(int type) {
        Type = type;
    }

    public int getTarget() {
        return Target;
    }

    public void setTarget(int target) {
        Target = target;
    }

    public int getTargetType() {
        return TargetType;
    }

    public void setTargetType(int targetType) {
        TargetType = targetType;
    }

    public int getParentType() {
        return ParentType;
    }

    public void setParentType(int parentType) {
        ParentType = parentType;
    }

    public int getParentId() {
        return ParentId;
    }

    public void setParentId(int parentId) {
        ParentId = parentId;
    }

    public int getMaxResultCount() {
        return MaxResultCount;
    }

    public void setMaxResultCount(int maxResultCount) {
        MaxResultCount = maxResultCount;
    }

    public int getSkipCount() {
        return SkipCount;
    }

    public void setSkipCount(int skipCount) {
        SkipCount = skipCount;
    }

    public int getSorting() {
        return Sorting;
    }

    public void setSorting(int sorting) {
        Sorting = sorting;
    }

    public boolean isEmptyContent() {
        return IsEmptyContent;
    }

    public void setEmptyContent(boolean emptyContent) {
        IsEmptyContent = emptyContent;
    }
}
