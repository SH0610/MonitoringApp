package com.example.monitoringapp.Scheduler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.monitoringapp.ErrorCatch.ErrorCatchActivity;
import com.example.monitoringapp.Login.LoginActivity;
import com.example.monitoringapp.Main.MainActivity;
import com.example.monitoringapp.R;
import com.example.monitoringapp.ServerDisk.ServerDiskActivity;
import com.example.monitoringapp.ServiceExecution.ServiceExecutionActivity;
import com.example.monitoringapp.databinding.ActivitySchedulerBinding;
import com.google.android.material.navigation.NavigationView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static com.example.monitoringapp.BaseActivity.getTodayDate;

public class SchedulerActivity extends AppCompatActivity {

    MainActivity mainActivity = (MainActivity) MainActivity._MainActivity;

    private Button btn_menu, btn_searchScheduler, btn_logout;
    private LinearLayout layout_btn_filter;
    private Button btn_day, btn_week, btn_month;
    private TextView tv_scheduler_label; // 스케줄러 관리 (제목)
    private TextView tv_start, tv_end, tv_divider;
    private ActivitySchedulerBinding binding;
    private boolean hideBtnClicked = false;

    private String STARTDT = null;
    private String ENDDT = null;
    private String resultCode = null;

    private int mStartYear, mStartMonth, mStartDay, mEndYear, mEndMonth, mEndDay;

    public static ArrayList<SchedulerItem> schedulerList = new ArrayList<>(); // 스케줄러 목록

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySchedulerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        drawerLayout = binding.schedulerDl;

        btn_menu = binding.schedulerBtnMenu;
        btn_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.END);
            }
        });

        Toolbar toolbar = (Toolbar) binding.schedulerToolbar;
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 뒤로
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.icon_back);

        // 오른쪽 메뉴 버튼
        NavigationView navigationView = (NavigationView) binding.schedulerNv;
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
                    Toast.makeText(getApplicationContext(), "현재 페이지입니다.", Toast.LENGTH_SHORT).show();
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

        tv_divider = binding.schedulerDateTvDivider;

        tv_start = binding.schedulerDateTvStart;

        final String init_dt = getTodayDate();

        tv_start.setText(init_dt);
        STARTDT = init_dt;
        ENDDT = init_dt;
        tv_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int mYear = calendar.get(Calendar.YEAR);
                int mMonth = calendar.get(Calendar.MONTH);
                int mDay = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog mDatePickerDialog;
                mDatePickerDialog = new DatePickerDialog(SchedulerActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        // i : year, i1 : month, i2 : day

                        mStartYear = i;
                        mStartMonth = i1 + 1;
                        mStartDay = i2;

                        // i1에서 1월이면 0, 2월이면 1, 3월이면 2가 나오므로 1을 더해줘야함
                        String startdt;
                        System.out.println("i : " + mStartYear + "  i1 : " + mStartMonth + "  i2 : " + mStartDay);

                        // 날짜를 2021-01-01 형식으로 맞춰주기
                        if (mStartMonth < 10) {
                            startdt = i + "-0" + mStartMonth + "-";
                        } else {
                            startdt = i + "-" + mStartMonth + "-";
                        }

                        if (i2 < 10) {
                            startdt = startdt + "0" + mStartDay;
                        }
                        else {
                            startdt = startdt + mStartDay;
                        }
                        System.out.println("선택된 시작 날짜 : " + startdt);
                        tv_start.setText(startdt);

                        STARTDT = startdt;
                    }
                }, mYear, mMonth, mDay);
                mDatePickerDialog.show();
            }
        });

        tv_end = binding.schedulerDateTvEnd;
        // 초기 날짜 (미리 지정되어있음)
        tv_end.setText(init_dt);
        tv_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int mYear = calendar.get(Calendar.YEAR);
                int mMonth = calendar.get(Calendar.MONTH);
                int mDay = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog mDatePickerDialog;
                mDatePickerDialog = new DatePickerDialog(SchedulerActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        // i : year, i1 : month, i2 : day

                        mEndYear = i;
                        mEndMonth = i1 + 1;
                        mEndDay = i2;

                        String enddt;
                        System.out.println("i : " + i + "  i1 : " + mEndMonth + "  i2 : " + i2);
                        if (mEndMonth < 10) {
                            enddt = i + "-0" + mEndMonth + "-";
                        } else {
                            enddt = i + "-" + mEndMonth + "-";
                        }

                        if (i2 < 10) {
                            enddt = enddt + "0" + i2;
                        }
                        else {
                            enddt = enddt + i2;
                        }
                        System.out.println("선택된 끝 날짜 : " + enddt);
                        tv_end.setText(enddt);

                        ENDDT = enddt;
                    }
                }, mYear, mMonth, mDay);
                mDatePickerDialog.show();
            }
        });

        btn_day = binding.schedulerBtnDay;
        btn_day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_start.setText(init_dt);
                tv_end.setText(init_dt);

                STARTDT = init_dt;
                ENDDT = init_dt;
            }
        });

        btn_week = binding.schedulerBtnWeek;
        btn_week.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_start.setText(init_dt);
                STARTDT = init_dt;
                System.out.println("tv_start" + tv_start.getText().toString());
                String enddt;
                enddt = getLaterDay(tv_start.getText().toString(), 7);
                tv_end.setText(enddt);
                ENDDT = enddt;
            }
        });

        btn_month = binding.schedulerBtnDateMonth;
        btn_month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_start.setText(init_dt);
                STARTDT = init_dt;
                System.out.println("tv_start" + tv_start.getText().toString());
                String enddt;
                enddt = getLaterMonth(tv_start.getText().toString(), 1);
                System.out.println(enddt);
                tv_end.setText(enddt);
                ENDDT = enddt;
            }
        });

        // 조회하기 버튼
        btn_searchScheduler = binding.schedulerBtnSearch;
        btn_searchScheduler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final RecyclerView recyclerView = binding.schedulerRecyclerview;
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                recyclerView.setLayoutManager(layoutManager);

                STARTDT = tv_start.getText().toString();
                ENDDT = tv_end.getText().toString();

                resultCode = SchedulerConnection.getSchedulerData(STARTDT, ENDDT);

                System.out.println("resultcOde" + resultCode);
                if (resultCode.equals("00")) {
                    SchedulerAdapter schedulerAdapter = new SchedulerAdapter(schedulerList);
                    recyclerView.setAdapter(schedulerAdapter);
                    recyclerView.getAdapter().notifyDataSetChanged();

                    if (schedulerList.isEmpty()) { // 데이터가 없을 경우
                        Toast.makeText(getApplicationContext(), "데이터가 없습니다.", Toast.LENGTH_SHORT).show();
                    }
                } else if (resultCode.equals("01")) {
                    Toast.makeText(getApplicationContext(), "입력값 오류", Toast.LENGTH_SHORT).show();
                } else if (resultCode.equals("99")) {
                    Toast.makeText(getApplicationContext(), "시스템 에러", Toast.LENGTH_SHORT).show();
                }
            }
        });

        tv_scheduler_label = binding.schedulerTvLabel;
        layout_btn_filter = binding.schedulerBtnHide;
        layout_btn_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!hideBtnClicked) { // 한번 클릭
                    hideBtnClicked = true;
                    tv_scheduler_label.setVisibility(view.GONE);
                    tv_start.setVisibility(view.GONE);
                    tv_end.setVisibility(view.GONE);
                    tv_divider.setVisibility(view.GONE);
                    btn_searchScheduler.setVisibility(view.GONE);
                    btn_day.setVisibility(view.GONE);
                    btn_week.setVisibility(view.GONE);
                    btn_month.setVisibility(view.GONE);
                } else { // 두번 클릭
                    hideBtnClicked = false;
                    tv_scheduler_label.setVisibility(view.VISIBLE);
                    tv_start.setVisibility(view.VISIBLE);
                    tv_end.setVisibility(view.VISIBLE);
                    tv_divider.setVisibility(view.VISIBLE);
                    btn_searchScheduler.setVisibility(view.VISIBLE);
                    btn_day.setVisibility(view.VISIBLE);
                    btn_week.setVisibility(view.VISIBLE);
                    btn_month.setVisibility(view.VISIBLE);
                }
            }
        });
    }

    // 7일 후의 년, 월, 일 받아옴
    private String getLaterDay(String originDay, int laterCnt) {
        System.out.println("originday" + originDay);

        Calendar cal = Calendar.getInstance();
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = formatter.parse(originDay); // 날짜
        } catch (ParseException e) {
            e.printStackTrace();
        }
        cal.setTime(date);
        System.out.println("입력 날짜 : " + formatter.format(cal.getTime()));

        cal.add(Calendar.DATE, laterCnt);
        System.out.println("출력 날짜 : " + formatter.format(cal.getTime()));

        String returnValue = formatter.format(cal.getTime());
        return returnValue;
    }

    // 한 달 후의 년, 월, 일 받아옴
    private String getLaterMonth(String originDay, int laterCnt) {
        System.out.println("originday" + originDay);

        Calendar cal = Calendar.getInstance();
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = formatter.parse(originDay); // 날짜
        } catch (ParseException e) {
            e.printStackTrace();
        }
        cal.setTime(date);
        System.out.println("입력 날짜 : " + formatter.format(cal.getTime()));

        cal.add(Calendar.MONTH, laterCnt);
        System.out.println("출력 날짜 : " + formatter.format(cal.getTime()));

        String returnValue = formatter.format(cal.getTime());
        return returnValue;
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