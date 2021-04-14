package com.dev.fitface.models;

import com.google.gson.annotations.SerializedName;

public class Report {
    @SerializedName("lesson")
    private int lesson;

    @SerializedName("statusid")
    private int statusid;

    @SerializedName("sessionid")
    private int sessionid;

    @SerializedName("timein")
    private long timein;

    @SerializedName("timeout")
    private long timeout;

    @SerializedName("sessdate")
    private long sessdate;

    @SerializedName("campus")
    private String campus;

    @SerializedName("room")
    private String room;

    public Report(int lesson, int statusid, int sessionid, long timein, long timeout, long sessdate, String campus, String room) {
        this.lesson = lesson;
        this.statusid = statusid;
        this.sessionid = sessionid;
        this.timein = timein;
        this.timeout = timeout;
        this.sessdate = sessdate;
        this.campus = campus;
        this.room = room;
    }

    public int getLesson() {
        return lesson;
    }

    public void setLesson(int lesson) {
        this.lesson = lesson;
    }

    public int getStatusid() {
        return statusid;
    }

    public void setStatusid(int statusid) {
        this.statusid = statusid;
    }

    public int getSessionid() {
        return sessionid;
    }

    public void setSessionid(int sessionid) {
        this.sessionid = sessionid;
    }

    public long getTimein() {
        return timein;
    }

    public void setTimein(long timein) {
        this.timein = timein;
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    public long getSessdate() {
        return sessdate;
    }

    public void setSessdate(long sessdate) {
        this.sessdate = sessdate;
    }

    public String getCampus() {
        return campus;
    }

    public void setCampus(String campus) {
        this.campus = campus;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }
}
