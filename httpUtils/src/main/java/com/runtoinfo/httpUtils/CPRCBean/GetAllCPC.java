package com.runtoinfo.httpUtils.CPRCBean;

/**
 * Created by QiaoJunChao on 2018/9/3.
 */
@SuppressWarnings("all")
public class GetAllCPC {
    public String UserId;
    public String Type;
    public String Target;
    public String TargetType;
    public String ParentType;
    public String ParentId;
    public int MaxResultCount;
    public int SkipCount;
    public String Sorting;
    public boolean IsEmptyContent;
    public String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getTarget() {
        return Target;
    }

    public void setTarget(String target) {
        Target = target;
    }

    public String getTargetType() {
        return TargetType;
    }

    public void setTargetType(String targetType) {
        TargetType = targetType;
    }

    public String getParentType() {
        return ParentType;
    }

    public void setParentType(String parentType) {
        ParentType = parentType;
    }

    public String getParentId() {
        return ParentId;
    }

    public void setParentId(String parentId) {
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

    public String getSorting() {
        return Sorting;
    }

    public void setSorting(String sorting) {
        Sorting = sorting;
    }

    public boolean isEmptyContent() {
        return IsEmptyContent;
    }

    public void setEmptyContent(boolean emptyContent) {
        IsEmptyContent = emptyContent;
    }
}
