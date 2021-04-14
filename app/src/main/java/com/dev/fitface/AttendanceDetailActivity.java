package com.dev.fitface;

import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import com.dev.fitface.adapter.CustomListReportAdapter;
import com.dev.fitface.api.MoodleService;
import com.dev.fitface.api.RetrofitClient;
import com.dev.fitface.models.ListAttendanceResponse;
import com.dev.fitface.models.Report;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AttendanceDetailActivity extends AppCompatActivity {
//    String name;
//    private MoodleService service;
//    private ArrayList<Report> reports;
//    private CustomAttendanceDetailAdapter customAttendanceDetailAdapter;
//    private ListView customListView;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        Intent intent = getIntent();
////        TextView attendance=(TextView)findViewById(R.id.attendance);
//
//        name = intent.getStringExtra("a");
//        Log.i("DEBUG3232", name);
//
//        service = RetrofitClient.initService();
//        setContentView(R.layout.activity_attendance_detail);
//        customListView = (ListView) findViewById(R.id.custom_list_view);
//        reports = new ArrayList<>();
//        for (int count = 0; count <3; count++) {
//            reports.add(new Report(count,2,3,1235674567,1667543456,1745456432,"name ne", "statusne"));
//
//        }
//        customAttendanceDetailAdapter = new CustomAttendanceDetailAdapter(reports, this);
//
//        customListView.setAdapter(customAttendanceDetailAdapter);
//        customListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
////                Toast.makeText(AttendanceDetailActivity.this, "Name : "  + "\n Profession : " + adapterView, Toast.LENGTH_SHORT).show();
//
//            }
//        });
//
//
//    }
private MoodleService service;
    private ArrayList<Report> reports;
    private CustomListReportAdapter customListAttendanceAdapter;
    private ListView customListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        service = RetrofitClient.initService();
        getListAttendances("2","secret_key",this);

        setContentView(R.layout.activity_attendance_detail);
        customListView = (ListView) findViewById(R.id.listVieww);
        reports = new ArrayList<>();
        for (int count = 0; count <3; count++) {
            reports.add(new Report(count,2,3,1235674567,1667543456,1745456432,"NVC", "E301"));

        }

        customListAttendanceAdapter = new CustomListReportAdapter(reports, this);
        customListView.setAdapter(customListAttendanceAdapter);
        customListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(AttendanceDetailActivity.this, "Name : "  + reports.get(i).getSessdate(), Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(AttendanceDetailActivity.this, AttendanceDetailActivity.class);
//                intent.putExtra("a", reports.get(i).getCampus());
//                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }
    private void getListAttendances(String id, String token, Context context) {
        service.getListAttendances(id,token).enqueue(new Callback<ListAttendanceResponse>() {
            @Override
            public void onResponse(Call<ListAttendanceResponse> call, Response<ListAttendanceResponse> response) {
                if (response.isSuccessful()){
//                    ArrayList<Attendance> attendances = response.body().getData();
//                    for (int count = 0; count <attendances.size(); count++) {
//                        Log.i("DEBUG", attendances.get(count).getEmail());
//
//                        userInfos.add(new Attendance(1, 1, 1, 1, 1, 1, attendances.get(count).getName(),  attendances.get(count).getEmail(), null));
//                    }
//                    attendances = response.body().getData();
//                    customListAttendanceAdapter = new CustomAttendanceDetailAdapter(reports, context);
//                    customListView.setAdapter(customListAttendanceAdapter);
//                    userInfos = response.body().getData();
//                    attendanceAdapter = new AttendanceAdapter(attendanceArrayList,context);
//                    customListAttendance.setAdapter(attendanceAdapter);
//                    Log.i("DEBUG", reports.get(0).getName());
                }
            }

            @Override
            public void onFailure(Call<ListAttendanceResponse> call, Throwable t) {

            }
        });
    }
}
