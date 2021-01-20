package com.example.monitoringapp.Main;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.monitoringapp.ErrorCatch.ErrorCatchActivity;
import com.example.monitoringapp.Login.LoginActivity;
import com.example.monitoringapp.R;
import com.example.monitoringapp.Scheduler.SchedulerActivity;
import com.example.monitoringapp.ServerDisk.ServerDiskActivity;
import com.example.monitoringapp.ServiceExecution.ServiceExecutionActivity;
import com.example.monitoringapp.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationView;

import static com.example.monitoringapp.BaseActivity.getTodayDate;
import static com.example.monitoringapp.Main.MainConnection.errorCnt;
import static com.example.monitoringapp.Main.MainConnection.scheduleCnt;


public class MainActivity extends AppCompatActivity {

    private Button btn_service, btn_disk, btn_scheduler, btn_error;
    private Button btn_menu, btn_logout;
    private TextView tv_name, tv_id, tv_scheduler, tv_error;
    ActivityMainBinding binding;
    private String date, resultCode = null;

    private DrawerLayout drawerLayout;

    public static Activity _MainActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        _MainActivity = MainActivity.this;

        if (android.os.Build.VERSION.SDK_INT > 9) { StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build(); StrictMode.setThreadPolicy(policy); }

        Toolbar toolbar = (Toolbar) binding.mainToolbar;
        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayShowCustomEnabled(true);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 뒤로
//        getSupportActionBar().setHomeAsUpIndicator(R.drawable.icon_back);

        drawerLayout = binding.mainDl;

        btn_menu = binding.mainBtnMenu;
        btn_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.END);
            }
        });

        // 오른쪽 메뉴 버튼
        NavigationView navigationView = (NavigationView) binding.mainNv;
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                item.setChecked(true);
                drawerLayout.closeDrawers();

                int id = item.getItemId();

                if (id == R.id.menu_service_execution) {
                    item.setChecked(false);
                    Intent intent = new Intent(getApplicationContext(), ServiceExecutionActivity.class);
                    startActivity(intent);
                    finish();
                }
                else if (id == R.id.menu_server_disk) {
                    item.setChecked(false);
                    Intent intent = new Intent(getApplicationContext(), ServerDiskActivity.class);
                    startActivity(intent);
                    finish();
                }
                else if (id == R.id.menu_scheduler) {
                    item.setChecked(false);
                    Intent intent = new Intent(getApplicationContext(), SchedulerActivity.class);
                    startActivity(intent);
                    finish();
                }
                else if (id == R.id.menu_error) {
                    item.setChecked(false);
                    Intent intent = new Intent(getApplicationContext(), ErrorCatchActivity.class);
                    startActivity(intent);
                    finish();
                }
                return true;
            }
        });

        btn_logout = (Button) navigationView.getHeaderView(0).findViewById(R.id.btn_logout);
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "로그아웃되었습니다.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

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
