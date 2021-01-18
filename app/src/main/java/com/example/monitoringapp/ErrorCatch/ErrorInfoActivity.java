package com.example.monitoringapp.ErrorCatch;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.monitoringapp.R;
import com.example.monitoringapp.databinding.ActivityErrorInfoBinding;

public class ErrorInfoActivity extends AppCompatActivity {

    private Button btn_ok;
    private String account, service, errdt, errtm, errmsg;
    private TextView tv_accountName, tv_serviceName, tv_errDate, tv_errTime, tv_errMsg;
    ActivityErrorInfoBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityErrorInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        btn_ok = binding.errorInfoBtnOk;
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });

        // Intent에서 받아온 값 저장
        account = getIntent().getStringExtra("account");
        service = getIntent().getStringExtra("service");
        errdt = getIntent().getStringExtra("errdt");
        errtm = getIntent().getStringExtra("errtm");
        errmsg = getIntent().getStringExtra("errmsg");

        tv_accountName = binding.errorInfoTvAccountName;
        tv_accountName.setText(account);

        tv_serviceName = binding.errorInfoTvServiceName;
        tv_serviceName.setText(service);

        tv_errDate = binding.errorInfoTvDate;
        tv_errDate.setText(errdt);

        tv_errTime = binding.errorInfoTvTime;
        tv_errTime.setText(errtm);

        tv_errMsg = binding.errorInfoTvMsg;
        tv_errMsg.setText(errmsg);
    }
}