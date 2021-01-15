package com.example.monitoringapp.ErrorCatch;

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

import com.example.monitoringapp.databinding.ActivityErrorCatchBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ErrorCatchActivity extends AppCompatActivity {

    private Button btn_back, btn_search, btn_hide;
    private TextView tv_error_catch_label; // 오류 처리 결과 (제목)
    private TextView tv_start, tv_end, tv_divider;
    private boolean hideBtnClicked = false;
    ActivityErrorCatchBinding binding;

    private String STARTDT = null;
    private String ENDDT = null;
    private String resultCode = null;

    private int mStartYear, mStartMonth, mStartDay, mEndYear, mEndMonth, mEndDay;

    public static ArrayList<ErrorItem> errorList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityErrorCatchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        btn_back = binding.errorCatchBtnBack;
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        tv_error_catch_label = binding.errorCatchTvLabel;
        tv_divider = binding.errorCatchTvDivider;

        tv_start = binding.errorCatchTvStart;
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
                mDatePickerDialog = new DatePickerDialog(ErrorCatchActivity.this, new DatePickerDialog.OnDateSetListener() {
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

        tv_end = binding.errorCatchTvEnd;
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
                mDatePickerDialog = new DatePickerDialog(ErrorCatchActivity.this, new DatePickerDialog.OnDateSetListener() {
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

        btn_search = binding.errorCatchBtn;
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resultCode = ErrorCatchConnection.getErrorInfo(STARTDT, ENDDT);

                final RecyclerView recyclerView = binding.errorRecyclerview;
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                recyclerView.setLayoutManager(layoutManager);

                if (resultCode.equals("00")) {

                    ErrorAdapter errorAdapter = new ErrorAdapter(errorList);
                    recyclerView.setAdapter(errorAdapter);
                    recyclerView.getAdapter().notifyDataSetChanged();

                    if (errorList.isEmpty()) { // 데이터가 없을 경우
                        Toast.makeText(getApplicationContext(), "데이터가 없습니다.", Toast.LENGTH_SHORT).show();
                    }
                }
                else if (resultCode.equals("01")) {
                    Toast.makeText(getApplicationContext(), "입력값 오류", Toast.LENGTH_SHORT).show();
                }
                else if (resultCode.equals("99")) {
                    Toast.makeText(getApplicationContext(), "시스템 에러", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_hide = binding.errorCatchBtnHide;
        btn_hide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!hideBtnClicked) { // 한번 클릭
                    hideBtnClicked = true;
                    tv_error_catch_label.setVisibility(view.GONE);
                    tv_start.setVisibility(view.GONE);
                    tv_divider.setVisibility(view.GONE);
                    tv_end.setVisibility(view.GONE);
                    btn_search.setVisibility(view.GONE);
                } else { // 두번 클릭
                    hideBtnClicked = false;
                    tv_error_catch_label.setVisibility(view.VISIBLE);
                    tv_start.setVisibility(view.VISIBLE);
                    tv_divider.setVisibility(view.VISIBLE);
                    tv_end.setVisibility(view.VISIBLE);
                    btn_search.setVisibility(view.VISIBLE);
                }
            }
        });


    }
}