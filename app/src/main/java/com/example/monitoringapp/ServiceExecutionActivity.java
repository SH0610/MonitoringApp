package com.example.monitoringapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.monitoringapp.Adapter.ServiceAdapter;
import com.example.monitoringapp.Adapter.SpinnerAdapter;
import com.example.monitoringapp.databinding.ActivityServiceExecutionBinding;

import java.util.ArrayList;

public class ServiceExecutionActivity extends AppCompatActivity {

    private Spinner spinner1, spinner2;
    private ActivityServiceExecutionBinding binding;
//    String[] items = {"모은넷", "에넥스텔레콤", "ACN코리아", "한국케이블텔레콤"};
//    String[] items2 = {"전체 보기", "MVNO 자동충전 서비스", "MVNO 일차감 서버", "MVNO ARS 충전/잔액 연동 서비스", "서비스 선택"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityServiceExecutionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        spinner1 = binding.serviceExecutionSpinner1;
        spinner2 = binding.serviceExecutionSpinner2;

        // 7. 리사이클러뷰 가져오기
        RecyclerView recyclerView = binding.serviceExecutionRecyclerview;
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        final ArrayList<String> items1 = new ArrayList<String>();
        items1.add("전체 보기");
        items1.add("모은넷");
        items1.add("에넥스텔레콤");
        items1.add("ACN코리아");
        items1.add("한국케이블텔레콤");
        items1.add("거래처 선택"); // Last item

        final ArrayList<String> items2 = new ArrayList<String>();
        items2.add("전체 보기");
        items2.add("MVNO 자동충전 서비스");
        items2.add("MVNO 일차감 서버");
        items2.add("MVNO ARS 충전/잔액 연동 서비스");
        items2.add("서비스 선택"); // Last item

        SpinnerAdapter adapter1 = new SpinnerAdapter(this, android.R.layout.simple_spinner_dropdown_item, items1);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter1);
        spinner1.setSelection(adapter1.getCount());

        SpinnerAdapter adapter2 = new SpinnerAdapter(this, android.R.layout.simple_spinner_dropdown_item, items2);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);
        spinner2.setSelection(adapter2.getCount());

        //  아이템이 선택되었을 때의 이벤트 처리 리스너 설정
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getApplicationContext(), items1.get(i) + " 선택됨", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(getApplicationContext(), "아이템을 선택해주세요.", Toast.LENGTH_SHORT).show();
            }
        });

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getApplicationContext(), items2.get(i) + " 선택됨", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(getApplicationContext(), "아이템을 선택해주세요.", Toast.LENGTH_SHORT).show();
            }
        });

        ArrayList<ServiceItem> dataList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            dataList.add(new ServiceItem("2021-01-05", "14:31:37", "에넥스텔레콤", "MVNO 자동충전 서비스", "정상"));
        }
        ServiceAdapter serviceAdapter = new ServiceAdapter(dataList);
        recyclerView.setAdapter(serviceAdapter);
        recyclerView.getAdapter().notifyDataSetChanged();
    }
}