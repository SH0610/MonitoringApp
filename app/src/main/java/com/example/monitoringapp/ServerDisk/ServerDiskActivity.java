package com.example.monitoringapp.ServerDisk;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.monitoringapp.R;
import com.example.monitoringapp.databinding.ActivityServerDiskBinding;

import java.util.ArrayList;

public class ServerDiskActivity extends AppCompatActivity {

    private Spinner spinner;
    private ActivityServerDiskBinding binding;
    public static ArrayList<String> item_serverDisk = new ArrayList<String>();
    public static ArrayList<String> item_serverDiskCode = new ArrayList<String>();

    private Button btn_filter, btn_back;
    private boolean hideBtnClicked = false;
    private TextView server_disk_label;

    public static ArrayList<ServerDiskItem> serverDiskList = new ArrayList<>(); // 서버 디스크 목록 가져오기 (ip, remark)
    private Button btn_searchDisk;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityServerDiskBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

//        item_serverDisk.add("전체 보기");
        item_serverDisk.add("에넥스텔레콤");
        item_serverDisk.add("ACN코리아");
        item_serverDisk.add("한국케이블텔레콤");
        item_serverDisk.add("KT파워텔");
        item_serverDisk.add("모은넷");
        item_serverDisk.add("웰네트웍스");

//        item_serverDiskCode.add("전체 보기");
        item_serverDiskCode.add("ANX");
        item_serverDiskCode.add("ACN");
        item_serverDiskCode.add("KCT");
        item_serverDiskCode.add("KTP");
        item_serverDiskCode.add("MNT");
        item_serverDiskCode.add("WEL");

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build(); StrictMode.setThreadPolicy(policy); }


        btn_back = binding.serverDiskBtnBack;
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        spinner = binding.serverDiskSpinner;

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, item_serverDisk);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

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

                ServerDiskConnection.getServerDisk(sd_AGCD);
                ServerDiskAdapter serverDiskAdapter = new ServerDiskAdapter(serverDiskList);
                recyclerView.setAdapter(serverDiskAdapter);
                recyclerView.getAdapter().notifyDataSetChanged();
            }
        });

        server_disk_label = findViewById(R.id.server_disk_tv_label);
        btn_filter = findViewById(R.id.server_disk_btn_hide);
        btn_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (hideBtnClicked == false) { // 한번 클릭
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
}