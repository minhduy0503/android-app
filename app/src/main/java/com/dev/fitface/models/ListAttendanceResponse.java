package com.dev.fitface.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ListAttendanceResponse extends BaseResponse {
    @SerializedName("data")
    ArrayList<Attendance> data;
//    @SerializedName("message")
//    String message;
//    @SerializedName("status")
//    int status;

    public ListAttendanceResponse(String message, int status) {
        super(message, status);
    }


    public ArrayList<Attendance> getData() {
        return data;
    }

}
