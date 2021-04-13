package com.dev.fitface;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import com.dev.fitface.adapter.CustomAttendanceDetailAdapter;
import com.dev.fitface.adapter.CustomListAttendanceAdapter;
import com.dev.fitface.api.MoodleService;
import com.dev.fitface.api.RetrofitClient;
import com.dev.fitface.models.Attendance;
import com.dev.fitface.models.Report;

import java.util.ArrayList;


public class AttendanceDetailActivity extends AppCompatActivity {
    String name;
    private MoodleService service;
    private ArrayList<Report> reports;
    private CustomAttendanceDetailAdapter customAttendanceDetailAdapter;
    private ListView customListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        name = intent.getStringExtra("attendance");
        service = RetrofitClient.initService();
        setContentView(R.layout.activity_attendance_detail);
        customListView = (ListView) findViewById(R.id.list_reports);
        reports = new ArrayList<>();
        reports.add(new Report(1,2,3,1235674567,1667543456,1745456432,"name ne", "statusne"));

        customAttendanceDetailAdapter = new CustomAttendanceDetailAdapter(reports, this);
        customListView.setAdapter(customAttendanceDetailAdapter);
        customListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(AttendanceDetailActivity.this, "Name : "  + "\n Profession : " + adapterView, Toast.LENGTH_SHORT).show();

            }
        });


    }
}
