package com.runtoinfo.httpUtils.bean;

import java.util.List;

/**
 * Created by QiaoJunChao on 2018/12/3.
 */

public class SystemMessageGroupEntity {
    public String key;
    public List<SystemMessageEntity> items;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public List<SystemMessageEntity> getItems() {
        return items;
    }

    public void setItems(List<SystemMessageEntity> items) {
        this.items = items;
    }
}
