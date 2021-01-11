package com.example.monitoringapp.ErrorCatch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.monitoringapp.R;

public class ErrorCatchActivity extends AppCompatActivity {

    private Button btn_back, btn_tmp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error_catch);

        btn_back = findViewById(R.id.error_catch_btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btn_tmp = findViewById(R.id.error_catch_btn);
        btn_tmp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ErrorInfoActivity.class);
                startActivity(intent);
            }
        });
    }
}