package com.example.monitoringapp.ServerDisk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.monitoringapp.ErrorCatch.ErrorMain.ErrorCatchActivity;
import com.example.monitoringapp.Login.LoginActivity;
import com.example.monitoringapp.Main.MainActivity;
import com.example.monitoringapp.R;
import com.example.monitoringapp.Scheduler.SchedulerActivity;
import com.example.monitoringapp.ServiceExecution.ServiceExecutionActivity;
import com.example.monitoringapp.databinding.ActivityServerDiskBinding;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class ServerDiskActivity extends AppCompatActivity {

    MainActivity mainActivity = (MainActivity) MainActivity._MainActivity;

    private Spinner spinner;
    private ActivityServerDiskBinding binding;
    public static ArrayList<String> item_serverDisk = new ArrayList<String>();
    public static ArrayList<String> item_serverDiskCode = new ArrayList<String>();

    private Button btn_menu, btn_searchDisk, btn_logout;
    private LinearLayout layout_btn_filter; // 필터 버튼
    private boolean hideBtnClicked = false;
    private TextView server_disk_label;
    private String resultCode = null;

    public static ArrayList<ServerDiskItem> serverDiskList = new ArrayList<>(); // 서버 디스크 목록 가져오기 (ip, remark)

    public static String sd_AGCD; // 대리점코드
    public static String sd_AGNM; // 대리점이름
    public static String sd_IP; // 아이피
    public static String sd_DRIVE_NM; // 드라이브명
    public static String sd_TOTAL_SIZE; // 총 용량
    public static String sd_FREE_SIZE; // 사용가능용량
    public static String sd_USAGE_SIZE; // 사용중용량
    public static String sd_USAGE_PERCENT; // 사용중비율
    public static String sd_REMARK; // 비고
    public static String sd_LIMIT_PERCENT; // 하한점
    public static String sd_UPDDT; // 수정일자
    public static String sd_UPDTM; // 수정시간

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityServerDiskBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        item_serverDisk.clear();
        item_serverDiskCode.clear();

        item_serverDisk.add("에넥스텔레콤");
        item_serverDisk.add("ACN코리아");
        item_serverDisk.add("한국케이블텔레콤");
        item_serverDisk.add("KT파워텔");
        item_serverDisk.add("모은넷");
        item_serverDisk.add("웰네트웍스");
        item_serverDisk.add("거래처를 선택해주세요.");

        item_serverDiskCode.add("ANX");
        item_serverDiskCode.add("ACN");
        item_serverDiskCode.add("KCT");
        item_serverDiskCode.add("KTP");
        item_serverDiskCode.add("MNT");
        item_serverDiskCode.add("WEL");
        item_serverDiskCode.add("거래처를 선택해주세요.");

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Toolbar toolbar = (Toolbar) binding.serverDiskToolbar;
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 뒤로
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.icon_back);

        drawerLayout = binding.serverDiskDl;

        btn_menu = binding.serverDiskBtnMenu;
        btn_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.END);
            }
        });

        // 오른쪽 메뉴 버튼
        NavigationView navigationView = (NavigationView) binding.serverDiskNv;
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
                    Toast.makeText(getApplicationContext(), "현재 페이지입니다.", Toast.LENGTH_SHORT).show();
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

                SharedPreferences sharedPreferences = getSharedPreferences("LoginInfo", MODE_PRIVATE);
                final SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("Auto_Login_enabled", false);
                editor.apply();

                Toast.makeText(getApplicationContext(), "로그아웃되었습니다.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                mainActivity.finish();
                finish();
            }
        });

        spinner = binding.serverDiskSpinner;

        // 스피너에 힌트를 추가하기 위해 따로 ServerDiskSpinnerAdapter를 생성한다.
        ServerDiskSpinnerAdapter serverDiskSpinnerAdapter = new ServerDiskSpinnerAdapter(this, android.R.layout.simple_spinner_dropdown_item, item_serverDisk);
        serverDiskSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(serverDiskSpinnerAdapter);
        spinner.setSelection(spinner.getCount());

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                sd_AGCD = item_serverDiskCode.get(i); // 선택된 대리점 코드
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(getApplicationContext(), "아이템을 선택해주세요.", Toast.LENGTH_SHORT).show();
            }
        });


        btn_searchDisk = findViewById(R.id.server_disk_btn);
        btn_searchDisk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final RecyclerView recyclerView = binding.serverDiskRecyclerview;
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                recyclerView.setLayoutManager(layoutManager);

                resultCode = ServerDiskConnection.getServerDisk(sd_AGCD);

                if (resultCode.equals("00")) {
                    ServerDiskAdapter serverDiskAdapter = new ServerDiskAdapter(serverDiskList);
                    recyclerView.setAdapter(serverDiskAdapter);
                    recyclerView.getAdapter().notifyDataSetChanged();

                    if (serverDiskList.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "데이터가 없습니다.", Toast.LENGTH_SHORT).show();
                    }
                } else if (resultCode.equals("01")) {
                    Toast.makeText(getApplicationContext(), "입력값 오류", Toast.LENGTH_SHORT).show();
                } else if (resultCode.equals("99")) {
                    Toast.makeText(getApplicationContext(), "시스템 에러", Toast.LENGTH_SHORT).show();
                }

            }
        });

        server_disk_label = binding.serverDiskTvLabel;

        layout_btn_filter = binding.serverDiskBtnHide;
        layout_btn_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!hideBtnClicked) { // 한번 클릭
                    hideBtnClicked = true;
                    spinner.setVisibility(view.GONE);
                    server_disk_label.setVisibility(view.GONE);
                    btn_searchDisk.setVisibility(view.GONE);
                } else { // 두번 클릭
                    hideBtnClicked = false;
                    spinner.setVisibility(view.VISIBLE);
                    server_disk_label.setVisibility(view.VISIBLE);
                    btn_searchDisk.setVisibility(view.VISIBLE);
                }
            }
        });
    }

    // 뒤로 가기 버튼
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ // 뒤로가기 버튼 눌렀을 때
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}