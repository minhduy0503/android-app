package com.dev.fitface;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.dev.fitface.api.MoodleService;
import com.dev.fitface.api.RetrofitClient;
import com.dev.fitface.models.LoginRequest;
import com.dev.fitface.models.LoginResponse;
import com.dev.fitface.models.UserInfo;


import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText ed_username;
    private EditText ed_password;
    private Button btn_login;
    private MoodleService service;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setupUI();
        initListener();
        initSharedPref();
        service = RetrofitClient.initService();


    }

    private void initSharedPref() {
        sharedPreferences = getSharedPreferences("userlog", MODE_PRIVATE);
    }

    private void initListener() {
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = ed_username.getText().toString();
                String password = ed_password.getText().toString();
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", username);
                params.put("password", password);
                login(params);
            }
        });
    }

    private void login(Map<String, String> params) {
        service.postLogin(params).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful()){
                    Log.i("DEBUG", response.toString());
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {

            }
        });
    }

    private void setupUI() {
        ed_username = findViewById(R.id.edt_username);
        ed_password = findViewById(R.id.edt_password);
        btn_login = findViewById(R.id.btn_login);
    }

}