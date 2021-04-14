package com.dev.fitface;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.dev.fitface.adapter.CustomListAttendanceAdapter;
import com.dev.fitface.api.MoodleService;
import com.dev.fitface.api.RetrofitClient;
import com.dev.fitface.models.Attendance;
import com.dev.fitface.models.ListAttendanceResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListAttendanceActivity extends AppCompatActivity {

    private MoodleService service;
    private ArrayList<Attendance> attendances;
    private CustomListAttendanceAdapter customListAttendanceAdapter;
    private ListView customListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        service = RetrofitClient.initService();
        getListAttendances("2","secret_key",this);

        setContentView(R.layout.activity_list_attendances);
        customListView = (ListView) findViewById(R.id.custom_list_view);
        attendances = new ArrayList<>();

    for (int count = 0; count <3; count++) {
        attendances.add(new Attendance(count,2,3,1235674567,1667543456,1745456432,"name ne", "statusne",null));

        }

        customListAttendanceAdapter = new CustomListAttendanceAdapter(attendances, this);
        customListView.setAdapter(customListAttendanceAdapter);
        customListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(ListAttendanceActivity.this, "Name : "  + attendances.get(i).getName(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getBaseContext(), AttendanceDetailActivity.class);
//                intent.putExtra("a", attendances.get(i).getName());
                startActivity(intent);
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
                    attendances = response.body().getData();
                    customListAttendanceAdapter = new CustomListAttendanceAdapter(attendances, context);
                    customListView.setAdapter(customListAttendanceAdapter);
//                    userInfos = response.body().getData();
//                    attendanceAdapter = new AttendanceAdapter(attendanceArrayList,context);
//                    customListAttendance.setAdapter(attendanceAdapter);
                    Log.i("DEBUG", attendances.get(0).getEmail());
                }
            }

            @Override
            public void onFailure(Call<ListAttendanceResponse> call, Throwable t) {

            }
        });
    }

}
//        getMenuInflater().inflate(R.menu.search_option,menu);
//        MenuItem menuItem=menu.findItem(R.id.search);
//        SearchView searchView=(SearchView)menuItem.getActionView();
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                return true;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                newText=newText.toString();
//                ArrayList<UserInfo> newUserInfos=new ArrayList<>();
//                for(UserInfo userInfo:userInfos){
//                    String name=userInfo.getName().toLowerCase();
//                    String profession=userInfo.getProfession().toLowerCase();
//                    if(name.contains(newText) || profession.contains(newText)){
//                        newUserInfos.add(userInfo);
//                    }
//                }
//                customListAdapter.filterResult(newUserInfos);
//                customListAdapter.notifyDataSetChanged();
//                return false;
//            }
//        });
//        return super.onCreateOptionsMenu(menu);
//    }
//    private EditText ed_username;
//    private EditText ed_password;
//    private Button btn_login;
//    private MoodleService service;
//    private SharedPreferences sharedPreferences;
//    private ArrayList<Attendance> attendanceArrayList;
//    private AttendanceAdapter attendanceAdapter;
//    private ListView customListAttendance;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_list_attendances);
//        customListAttendance = (ListView)findViewById(R.id.custom_list_view) ;
//        attendanceArrayList= new ArrayList<>();
//        service = RetrofitClient.initService();
//        getListAttendances("2","secret_key",this);
//
//
//
//
//    }
//
//    private void getListAttendances(String id, String token, Context context) {
//        service.getListAttendances(id,token).enqueue(new Callback<ListAttendanceResponse>() {
//            @Override
//            public void onResponse(Call<ListAttendanceResponse> call, Response<ListAttendanceResponse> response) {
//                if (response.isSuccessful()){
//                    attendanceArrayList = response.body().getData();
//                    attendanceAdapter = new AttendanceAdapter(attendanceArrayList,context);
//                    customListAttendance.setAdapter(attendanceAdapter);
//                    Log.i("DEBUG", attendanceArrayList.get(0).getEmail());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ListAttendanceResponse> call, Throwable t) {
//
//            }
//        });
//    }

//}