package com.example.monitoringapp.ErrorCatch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.monitoringapp.R;
import com.example.monitoringapp.databinding.ActivityErrorCatchBinding;

import java.util.ArrayList;

public class ErrorCatchActivity extends AppCompatActivity {

    private Button btn_back, btn_search, btn_hide;
    private boolean hideBtnClicked = false;
    ActivityErrorCatchBinding binding;

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

        btn_search = binding.errorCatchBtn;
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code = ErrorCatchConnection.getErrorInfo("20210101", "20210115");
                if (code.equals("00")) {
//                    final RecyclerView recyclerView = binding.errorRecyclerview;
//                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
//                    recyclerView.setLayoutManager(layoutManager);
//
//                    ErrorAdapter errorAdapter = new ErrorAdapter(errorList);
//                    recyclerView.setAdapter(errorAdapter);
//                    recyclerView.getAdapter().notifyDataSetChanged();
                    Intent intent = new Intent(getApplicationContext(), ErrorInfoActivity.class);
                    startActivity(intent);
                }
                else if (code.equals("01")) {
                    Toast.makeText(getApplicationContext(), "입력값 오류", Toast.LENGTH_SHORT).show();
                }
                else if (code.equals("99")) {
                    Toast.makeText(getApplicationContext(), "시스템 에러", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_hide = binding.errorCatchBtnHide;
        btn_hide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (hideBtnClicked == false) { // 한번 클릭
                    hideBtnClicked = true;
//                    spinner.setVisibility(view.GONE);
//                    tv_scheduler_label.setVisibility(view.GONE);
//                    btn_searchScheduler.setVisibility(view.GONE);
                    btn_search.setVisibility(view.GONE);
//                    btn_week.setVisibility(view.GONE);
//                    btn_month.setVisibility(view.GONE);
                } else { // 두번 클릭
                    hideBtnClicked = false;
//                    spinner.setVisibility(view.VISIBLE);
//                    tv_scheduler_label.setVisibility(view.VISIBLE);
//                    btn_searchScheduler.setVisibility(view.VISIBLE);
                    btn_search.setVisibility(view.VISIBLE);
//                    btn_week.setVisibility(view.VISIBLE);
//                    btn_month.setVisibility(view.VISIBLE);
                }
            }
        });


    }
}