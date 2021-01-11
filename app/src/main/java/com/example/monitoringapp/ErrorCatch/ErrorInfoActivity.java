package com.example.monitoringapp.ErrorCatch;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.monitoringapp.R;

public class ErrorInfoActivity extends AppCompatActivity {

    private Button btn_temp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error_info);

        btn_temp = findViewById(R.id.error_info_btn_ok);
        btn_temp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}