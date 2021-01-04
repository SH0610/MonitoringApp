package com.example.monitoringapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.monitoringapp.databinding.ActivityServiceExecutionBinding;

import java.util.ArrayList;

public class ServiceExecutionActivity extends AppCompatActivity {

    private Spinner spinner1, spinner2;
    private ActivityServiceExecutionBinding binding;
//    String[] items = {"모은넷", "에넥스텔레콤", "ACN코리아", "한국케이블텔레콤"};
    String[] items2 = {"MVNO 자동충전 서비스", "MVNO 일차감 서버", "MVNO ARS 충전/잔액 연동 서비스"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityServiceExecutionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        spinner1 = binding.serviceExecutionSpinner1;
        spinner2 = binding.serviceExecutionSpinner2;

        final ArrayList<String> items = new ArrayList<String>();
        items.add("전체 보기");
        items.add("모은넷");
        items.add("에넥스텔레콤");
        items.add("ACN코리아");
        items.add("한국케이블텔레콤");
        items.add("서비스 선택");

        SpinnerAdapter adapter1 = new SpinnerAdapter(this, android.R.layout.simple_spinner_dropdown_item, items);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter1);
        spinner1.setSelection(adapter1.getCount());


        // 스피너를 위한 어댑터 구성
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);



        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, items2);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // 스피너 객체 참조하여 어댑터 설정
        spinner1.setAdapter(adapter);
        spinner1.setPrompt("거래처 선택");
        spinner2.setAdapter(adapter2);
        spinner2.setPrompt("서비스 선택");

        //  아이템이 선택되었을 때의 이벤트 처리 리스너 설정
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getApplicationContext(), items.get(i) + " 선택됨", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(getApplicationContext(), "아이템을 선택해주세요.", Toast.LENGTH_SHORT).show();
            }
        });

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getApplicationContext(), items2[i] + " 선택됨", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(getApplicationContext(), "아이템을 선택해주세요.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}