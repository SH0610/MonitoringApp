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
import android.widget.Toast;

import com.example.monitoringapp.R;
import com.example.monitoringapp.databinding.ActivitySchedulerBinding;

import java.text.ParseException;
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
    private String STARTDT = null;
    private String ENDDT = null;

    private int mStartYear, mStartMonth, mStartDay, mEndYear, mEndMonth, mEndDay;

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
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("y");
        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("MM");
        SimpleDateFormat simpleDateFormat3 = new SimpleDateFormat("dd");

        int init_year = Integer.parseInt(simpleDateFormat1.format(mDate));
        int init_month = Integer.parseInt(simpleDateFormat2.format(mDate));
        int init_day = Integer.parseInt(simpleDateFormat3.format(mDate));

        System.out.println("년도 : " + init_year + " 월 : " + init_month + " 일 : " + init_day);

        String st_init_month = null;
        String st_init_day = null;

        if (init_month < 10) {
            st_init_month = "0" + Integer.toString(init_month);
        } else {
            st_init_month = Integer.toString(init_month);
        }
        if (init_day < 10) {
            st_init_day = "0" + Integer.toString(init_day);
        }
        else {
            st_init_day = Integer.toString(init_day);
        }

        final String init_dt = Integer.toString(init_year) + "-" + st_init_month + "-" + st_init_day;

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

//        // init_dt 에서 parse 해오기
//        final int parseYear, parseMonth, parseDay;
//        System.out.println("init_dt : " + init_dt);
//
//        parseYear = Integer.parseInt(init_dt.substring(0, 4));
//        System.out.println("init_dt : " + parseYear);
//        parseMonth = Integer.parseInt(init_dt.substring(4, 6));
//        System.out.println("init_dt : " + parseMonth);
//        parseDay = Integer.parseInt(init_dt.substring(6, 8));
//        System.out.println("init_dt : " + parseDay);

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
                tv_end.setText(enddt);
                ENDDT = enddt;
            }
        });

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
                    tv_start.setVisibility(view.GONE);
                    tv_end.setVisibility(view.GONE);
                    tv_divider.setVisibility(view.GONE);
                    btn_searchScheduler.setVisibility(view.GONE);
                    btn_day.setVisibility(view.GONE);
                    btn_week.setVisibility(view.GONE);
                    btn_month.setVisibility(view.GONE);
                } else { // 두번 클릭
                    hideBtnClicked = false;
//                    spinner.setVisibility(view.VISIBLE);
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

    private String getLaterDay(String originDay, int laterCnt) {
        System.out.println("originday" + originDay);
        String laterDay = null;

        try {
            Calendar calendar = Calendar.getInstance();
            Date originDate = new Date();
            Date laterDate = new Date();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-DD");
//            SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyyMMdd");
//
//            System.out.println("test");
//            System.out.println(originDay); // 선택시작날짜
            originDate = simpleDateFormat.parse(originDay);
            calendar.setTime(laterDate);
//
//            System.out.println(originDate); // 이상한값이나와

            calendar.add(Calendar.DATE, laterCnt); // laterCnt일 후의 날짜를 구한다.
            laterDate = calendar.getTime();
            laterDay = simpleDateFormat.format(laterDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        System.out.println("laterDay" + laterDay);
        return laterDay;
    }

    private String getLaterMonth(String originDay, int laterCnt) {
        System.out.println("originday" + originDay);
        String originDay2 = originDay.replaceAll("-" ,"");

        String tmp_year = originDay.substring(0, 4);
        String tmp_month = originDay.substring(5, 7);
        String tmp_day =  originDay.substring(8, 10);

        System.out.println("tmp_year : " + tmp_year + " tmp_month: " + tmp_month + "  tmp_day : " + tmp_day);
        String tmp_date = null;

        String laterDay = null;

        try {
            Calendar calendar = Calendar.getInstance();
            Date originDate = new Date();
            Date laterDate = new Date();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");

            System.out.println("test");
            System.out.println(originDay); // 선택시작날짜
            originDate = simpleDateFormat.parse(originDay);
            calendar.setTime(laterDate);
//
//            System.out.println(originDate); // 이상한값이나와

            calendar.add(Calendar.MONTH, laterCnt); // laterCnt 개월 후의 월을 구한다.
            laterDate = calendar.getTime();
            laterDay = simpleDateFormat.format(laterDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        tmp_year = laterDay.substring(0, 4);
        tmp_month = laterDay.substring(4, 6);
        tmp_day =  laterDay.substring(6, 8);

        System.out.println("tmp_year : " + tmp_year + " tmp_month: " + tmp_month + "  tmp_day : " + tmp_day);

        // 날짜를 2021-01-01 형식으로 맞춰주기
        tmp_date = tmp_year + "-" + tmp_month + "-";

        if (Integer.parseInt(tmp_day) < 10) {
            tmp_date = tmp_date + "0" + tmp_day;
        }
        else {
            tmp_date = tmp_date + tmp_day;
        }

        System.out.println("laterMonth" + laterDay);
        return tmp_date;
    }
}