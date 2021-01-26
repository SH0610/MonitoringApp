package com.example.monitoringapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.monitoringapp.Login.LoginActivity;
import com.example.monitoringapp.Main.MainActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            Thread.sleep(1200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        SharedPreferences sharedPreferences = getSharedPreferences("LoginInfo", MODE_PRIVATE);

        if (sharedPreferences.getBoolean("Auto_Login_enabled", false)) { // 자동 로그인 체크되어서 아이디가 저장되어있으면 바로 메인으로
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        } else { // 자동 로그인 체크가 안되어있어 저장안되어있으면 로그인 화면으로
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }
}