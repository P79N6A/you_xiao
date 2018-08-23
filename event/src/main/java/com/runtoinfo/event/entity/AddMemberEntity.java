package com.runtoinfo.event.entity;

/**
 * Created by QiaoJunChao on 2018/8/21.
 */

public class AddMemberEntity {

    public String name;
    public String type;
    public String phoneNumber;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
