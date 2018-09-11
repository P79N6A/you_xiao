package com.runtoinfo.httpUtils.bean;

/**
 * Created by QiaoJunChao on 2018/9/10.
 */

public class SystemMessageEntity {

    public String time;
    public String title;
    public String pay_price;
    public String pay_time;
    public String pay_type;
    public String pay_details;
    public String pay_number;
    public String go_where;
    public String message;
    public int itemType;
    public String picPath;

    public String getPicPath() {
        return picPath;
    }

    public void setPicPath(String picPath) {
        this.picPath = picPath;
    }

    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPay_price() {
        return pay_price;
    }

    public void setPay_price(String pay_price) {
        this.pay_price = pay_price;
    }

    public String getPay_time() {
        return pay_time;
    }

    public void setPay_time(String pay_time) {
        this.pay_time = pay_time;
    }

    public String getPay_type() {
        return pay_type;
    }

    public void setPay_type(String pay_type) {
        this.pay_type = pay_type;
    }

    public String getPay_details() {
        return pay_details;
    }

    public void setPay_details(String pay_details) {
        this.pay_details = pay_details;
    }

    public String getPay_number() {
        return pay_number;
    }

    public void setPay_number(String pay_number) {
        this.pay_number = pay_number;
    }

    public String getGo_where() {
        return go_where;
    }

    public void setGo_where(String go_where) {
        this.go_where = go_where;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
