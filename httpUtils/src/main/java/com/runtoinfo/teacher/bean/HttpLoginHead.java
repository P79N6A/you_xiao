package com.runtoinfo.teacher.bean;

/**
 * Created by QiaoJunChao on 2018/8/10.
 */

public class HttpLoginHead {

    public String token;
    public String tenancyName;
    public String client;
    public int campusId;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTenancyName() {
        return tenancyName;
    }

    public void setTenancyName(String tenancyName) {
        this.tenancyName = tenancyName;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public int getCampusId() {
        return campusId;
    }

    public void setCampusId(int campusId) {
        this.campusId = campusId;
    }
}
