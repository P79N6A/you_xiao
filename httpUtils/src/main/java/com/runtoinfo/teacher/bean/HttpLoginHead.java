package com.runtoinfo.teacher.bean;

/**
 * Created by QiaoJunChao on 2018/8/10.
 */

public class HttpLoginHead {

    private int code;
    private String token;
    private String tenancyName;
    private String client;
    private int campusId;
    private String userName;
    private String passWord;
    private String tenantId;
    private String newPassWord;

    public String getNewPassWord() {
        return newPassWord;
    }

    public void setNewPassWord(String newPassWord) {
        this.newPassWord = newPassWord;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }
}
