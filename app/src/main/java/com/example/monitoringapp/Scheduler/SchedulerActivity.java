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
import java.util.Date;
import java.util.GregorianCalendar;

public class SchedulerActivity extends AppCompatActivity {

    private Button btn_back, btn_hide, btn_searchScheduler;
    private Button btn_day, btn_week, btn_month;
    private TextView tv_scheduler_label; // 스케줄러 관리 (제목)
    private TextView tv_start, tv_end, tv_divider;
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

        tv_divider = binding.schedulerDateTvDivider;

        tv_start = binding.schedulerDateTvStart;
        // 초기 날짜 (미리 지정되어있음)
        long now = System.currentTimeMillis();
        Date mDate = new Date(now);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String init_dt = simpleDateFormat.format(mDate);
        tv_start.setText(init_dt);
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
                        // i1에서 1월이면 0, 2월이면 1, 3월이면 2가 나오므로 1을 더해줘야함
                        int forMonth = i1 + 1;
                        String startdt;
                        System.out.println("i : " + i + "  i1 : " + forMonth + "  i2 : " + i2);
                        if (forMonth < 10) {
                            startdt = i + "-0" + forMonth + "-";
                        } else {
//                            String tmp = Integer.toString(forMonth);
                            startdt = i + "-" + forMonth + "-";
                        }

                        if (i2 < 10) {
                            startdt = startdt + "0" + i2;
                        }
                        else {
                            startdt = startdt + i2;
                        }
                        System.out.println(startdt);
                        tv_start.setText(startdt);
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
                        int forMonth = i1 + 1;
                        String enddt;
                        System.out.println("i : " + i + "  i1 : " + forMonth + "  i2 : " + i2);
                        if (forMonth < 10) {
                            enddt = i + "-0" + forMonth + "-";
                        } else {
                            enddt = i + "-" + forMonth + "-";
                        }

                        if (i2 < 10) {
                            enddt = enddt + "0" + i2;
                        }
                        else {
                            enddt = enddt + i2;
                        }
                        System.out.println(enddt);
                        tv_end.setText(enddt);
                    }
                }, mYear, mMonth, mDay);
                mDatePickerDialog.show();
            }
        });

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


                STARTDT = tv_start.getText().toString();
                ENDDT = tv_end.getText().toString();

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
                    tv_divider.setVisibility(view.GONE);
                    btn_searchScheduler.setVisibility(view.GONE);
                    btn_day.setVisibility(view.GONE);
                    btn_week.setVisibility(view.GONE);
                    btn_month.setVisibility(view.GONE);
                } else { // 두번 클릭
                    hideBtnClicked = false;
//                    spinner.setVisibility(view.VISIBLE);
                    tv_scheduler_label.setVisibility(view.VISIBLE);
                    tv_divider.setVisibility(view.VISIBLE);
                    btn_searchScheduler.setVisibility(view.VISIBLE);
                    btn_day.setVisibility(view.VISIBLE);
                    btn_week.setVisibility(view.VISIBLE);
                    btn_month.setVisibility(view.VISIBLE);
                }
            }
        });
    }
}