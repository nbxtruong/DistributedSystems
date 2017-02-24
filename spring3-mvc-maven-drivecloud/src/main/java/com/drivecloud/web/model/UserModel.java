package com.drivecloud.web.model;

public class UserModel {
    private int userid;
    private String username;
    private String password;

    public UserModel(String username, String Password) {
        this.username = username;
        this.password = password;
    }

    public UserModel() {

    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
