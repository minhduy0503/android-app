package com.dev.fitface.models;

import com.google.gson.annotations.SerializedName;

public class Attendance {
    @SerializedName("studentid")
    private int studentid;
    @SerializedName("count")
    private int count;
    @SerializedName("c")
    private int c;
    @SerializedName("b")
    private int b;
    @SerializedName("t")
    private int t;
    @SerializedName("v")
    private int v;

    @SerializedName("name")
    private String name;
    @SerializedName("email")
    private String email;
    @SerializedName("reports")
    private Report[] reports;

    public Attendance(int studentid, int count, int c, int b, int t, int v, String name, String email, Report[] reports) {
        this.studentid = studentid;
        this.count = count;
        this.c = c;
        this.b = b;
        this.t = t;
        this.v = v;
        this.name = name;
        this.email = email;
        this.reports = reports;
    }

    public int getStudentid() {
        return studentid;
    }

    public void setStudentid(int studentid) {
        this.studentid = studentid;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getC() {
        return c;
    }

    public void setC(int c) {
        this.c = c;
    }

    public int getB() {
        return b;
    }

    public void setB(int b) {
        this.b = b;
    }

    public int getT() {
        return t;
    }

    public void setT(int t) {
        this.t = t;
    }

    public int getV() {
        return v;
    }

    public void setV(int v) {
        this.v = v;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Report[] getReports() {
        return reports;
    }

    public void setReports(Report[] reports) {
        this.reports = reports;
    }
}
