package com.handsome.xrecyclerview.bean;

/**
 * =====作者=====
 * 许英俊
 * =====时间=====
 * 2018/1/6.
 */

public class Student {

    private String username;
    private String phone;
    private int avater;

    public Student(String username, String phone, int avater) {
        this.username = username;
        this.phone = phone;
        this.avater = avater;
    }

    public int getAvater() {
        return avater;
    }

    public void setAvater(int avater) {
        this.avater = avater;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
