package com.example.monitoringapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.monitoringapp.ErrorCatch.ErrorCatchActivity;
import com.example.monitoringapp.Scheduler.SchedulerActivity;
import com.example.monitoringapp.ServerDisk.ServerDiskActivity;
import com.example.monitoringapp.ServiceExecution.ServiceExecutionActivity;
import com.example.monitoringapp.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {

    private Button btn_service, btn_disk, btn_scheduler, btn_error;
    private TextView tv_id;
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (android.os.Build.VERSION.SDK_INT > 9) { StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build(); StrictMode.setThreadPolicy(policy); }

        btn_service = findViewById(R.id.main_btn_service);
        btn_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ServiceExecutionActivity.class);
                startActivity(intent);
            }
        });

        btn_disk = findViewById(R.id.main_btn_disk);
        btn_disk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ServerDiskActivity.class);
                startActivity(intent);
            }
        });

        btn_scheduler = findViewById(R.id.main_btn_scheduler);
        btn_scheduler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SchedulerActivity.class);
                startActivity(intent);
            }
        });

        btn_error = findViewById(R.id.main_btn_error);
        btn_error.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ErrorCatchActivity.class);
                startActivity(intent);
            }
        });

        tv_id = binding.mainTvId;
        SharedPreferences sharedPreferences = getSharedPreferences("LoginInfo", MODE_PRIVATE);
        tv_id.setText(sharedPreferences.getString("id", "NO ID"));
    }
}
