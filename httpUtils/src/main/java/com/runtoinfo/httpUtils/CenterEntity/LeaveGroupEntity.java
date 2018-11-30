package com.runtoinfo.httpUtils.CenterEntity;

import java.util.List;

/**
 * Created by QiaoJunChao on 2018/11/30.
 */

public class LeaveGroupEntity {
    public String key;
    public List<LeaveRecordEntity> items;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public List<LeaveRecordEntity> getItems() {
        return items;
    }

    public void setItems(List<LeaveRecordEntity> items) {
        this.items = items;
    }
}
