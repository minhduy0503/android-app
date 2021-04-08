package com.dev.fitface.models;

import com.google.gson.annotations.SerializedName;

public class UserInfo {
    @SerializedName("id")
    int id;
    @SerializedName("usename")
    String username;
    @SerializedName("firstname")
    String firstname;
    @SerializedName("lastname")
    String lastname;
    @SerializedName("roleid")
    int roleid;
    @SerializedName("role")
    String role;
    @SerializedName("shortname")
    String shoername;
    @SerializedName("token")
    String token;

    public UserInfo(int id, String username, String firstname, String lastname, int roleid, String role, String shoername, String token) {
        this.id = id;
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.roleid = roleid;
        this.role = role;
        this.shoername = shoername;
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public int getRoleid() {
        return roleid;
    }

    public void setRoleid(int roleid) {
        this.roleid = roleid;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getShoername() {
        return shoername;
    }

    public void setShoername(String shoername) {
        this.shoername = shoername;
    }
}