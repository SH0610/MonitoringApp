package com.example.monitoringapp.Main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.monitoringapp.ErrorCatch.ErrorCatchActivity;
import com.example.monitoringapp.Scheduler.SchedulerActivity;
import com.example.monitoringapp.ServerDisk.ServerDiskActivity;
import com.example.monitoringapp.ServiceExecution.ServiceExecutionActivity;
import com.example.monitoringapp.databinding.ActivityMainBinding;

import static com.example.monitoringapp.BaseActivity.getTodayDate;
import static com.example.monitoringapp.Main.MainConnection.errorCnt;
import static com.example.monitoringapp.Main.MainConnection.scheduleCnt;


public class MainActivity extends AppCompatActivity {

    private Button btn_service, btn_disk, btn_scheduler, btn_error;
    private TextView tv_name, tv_id, tv_scheduler, tv_error;
    ActivityMainBinding binding;
    private String date, resultCode = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (android.os.Build.VERSION.SDK_INT > 9) { StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build(); StrictMode.setThreadPolicy(policy); }

        btn_service = binding.mainBtnService;
        btn_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ServiceExecutionActivity.class);
                startActivity(intent);
            }
        });

        btn_disk = binding.mainBtnDisk;
        btn_disk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ServerDiskActivity.class);
                startActivity(intent);
            }
        });

        btn_scheduler = binding.mainBtnScheduler;
        btn_scheduler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SchedulerActivity.class);
                startActivity(intent);
            }
        });

        btn_error = binding.mainBtnError;
        btn_error.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ErrorCatchActivity.class);
                startActivity(intent);
            }
        });

        SharedPreferences sharedPreferences = getSharedPreferences("LoginInfo", MODE_PRIVATE);

        tv_name = binding.mainTvName;
        tv_name.setText(sharedPreferences.getString("name", "NO NAME"));

        tv_scheduler = binding.mainTvScheduler;
        tv_error = binding.mainTvError;

        date = getTodayDate();

        resultCode = MainConnection.getMainData(date,date);

        if (resultCode.equals("00")) {
            tv_scheduler.setText(scheduleCnt);
            tv_error.setText(errorCnt);
        } else if (resultCode.equals("01")) {
            tv_scheduler.setText("에러");
            tv_error.setText("에러");
            Toast.makeText(getApplicationContext(), "건수 조회 / 입력값 오류", Toast.LENGTH_SHORT).show();
        } else if (resultCode.equals("99")){
            tv_scheduler.setText("에러");
            tv_error.setText("에러");
            Toast.makeText(getApplicationContext(), "건수 조회 / 시스템 에러", Toast.LENGTH_SHORT).show();
        }
    }
}
