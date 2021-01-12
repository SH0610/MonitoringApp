package com.example.monitoringapp.Scheduler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.example.monitoringapp.R;
import com.example.monitoringapp.databinding.ActivitySchedulerBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class SchedulerActivity extends AppCompatActivity {

    private Button btn_back, btn_hide, btn_searchScheduler;
    private Button btn_day, btn_week, btn_month;
    private TextView tv_scheduler_label; // 스케줄러 관리 (제목)
    private TextView tv_start, tv_end;
    private ActivitySchedulerBinding binding;
    private boolean hideBtnClicked = false;

    public static String sc_AGCD; // 대리점코드
    public static String sc_AGNM; // 대리점이름
    public static String sc_SVCCD; // 서비스코드
    public static String sc_SVCNM; // 서비스명
    public static String sc_EXEFG; // 처리여부
    public static String sc_EXEDT; // 예상실행일자
    public static String sc_EXETM; // 예상실행시간
    public static String sc_UPDDT; // 실제실행일자
    public static String sc_UPDTM; // 실제실행시간
    private String STARTDT;
    private String ENDDT;

    public static ArrayList<SchedulerItem> schedulerList = new ArrayList<>(); // 스케줄러 목록

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySchedulerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        btn_back = binding.schedulerBtnBack;
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        tv_start = binding.schedulerDateTvStart;

        tv_end = binding.schedulerDateTvEnd;

        btn_day = binding.schedulerBtnDay;

        btn_week = binding.schedulerBtnWeek;

        btn_month = binding.schedulerBtnDateMonth;

        btn_searchScheduler = binding.schedulerBtnSearch;
        btn_searchScheduler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final RecyclerView recyclerView = binding.schedulerRecyclerview;
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                recyclerView.setLayoutManager(layoutManager);


                STARTDT = "20210101";
                ENDDT = "20210117";

                SchedulerConnection.getSchedulerData(STARTDT, ENDDT);
                SchedulerAdapter schedulerAdapter = new SchedulerAdapter(schedulerList);
                recyclerView.setAdapter(schedulerAdapter);
                recyclerView.getAdapter().notifyDataSetChanged();
            }
        });

        tv_scheduler_label = binding.schedulerTvLabel;
        btn_hide = binding.schedulerBtnHide;
        btn_hide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (hideBtnClicked == false) { // 한번 클릭
                    hideBtnClicked = true;
//                    spinner.setVisibility(view.GONE);
                    tv_scheduler_label.setVisibility(view.GONE);
                    btn_searchScheduler.setVisibility(view.GONE);
                    btn_day.setVisibility(view.GONE);
                    btn_week.setVisibility(view.GONE);
                    btn_month.setVisibility(view.GONE);
                } else { // 두번 클릭
                    hideBtnClicked = false;
//                    spinner.setVisibility(view.VISIBLE);
                    tv_scheduler_label.setVisibility(view.VISIBLE);
                    btn_searchScheduler.setVisibility(view.VISIBLE);
                    btn_day.setVisibility(view.VISIBLE);
                    btn_week.setVisibility(view.VISIBLE);
                    btn_month.setVisibility(view.VISIBLE);
                }
            }
        });
    }
}