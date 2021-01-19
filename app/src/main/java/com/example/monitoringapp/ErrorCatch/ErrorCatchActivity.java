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

import java.util.ArrayList;
import java.util.Calendar;

import static com.example.monitoringapp.BaseActivity.getTodayDate;

public class ErrorCatchActivity extends AppCompatActivity {

    private Button btn_back, btn_search, btn_hide;
    private TextView tv_error_catch_label; // 오류 처리 결과 (제목)
    private TextView tv_start, tv_end, tv_divider;
    private boolean hideBtnClicked = false;
    public static boolean errorInfoModified = false;
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

                    ErrorAdapter errorAdapter = new ErrorAdapter(getApplicationContext(), errorList);
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

    @Override
    protected void onResume() {
        super.onResume();

        System.out.println("modify1" + errorInfoModified);

        if (errorInfoModified) {
            System.out.println("modify2" + errorInfoModified);

            errorInfoModified = false;
            resultCode = ErrorCatchConnection.getErrorInfo(STARTDT, ENDDT);

            final RecyclerView recyclerView = binding.errorRecyclerview;
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(layoutManager);

            if (resultCode.equals("00")) {

                ErrorAdapter errorAdapter = new ErrorAdapter(getApplicationContext(), errorList);
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
    }
}
