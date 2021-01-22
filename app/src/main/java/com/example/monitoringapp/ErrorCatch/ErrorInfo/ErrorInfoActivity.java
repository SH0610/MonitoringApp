package com.example.monitoringapp.ErrorCatch.ErrorInfo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.monitoringapp.R;
import com.example.monitoringapp.databinding.ActivityErrorInfoBinding;

import static com.example.monitoringapp.ErrorCatch.ErrorMain.ErrorCatchActivity.errorInfoModified;

public class ErrorInfoActivity extends AppCompatActivity {

    private Button btn_ok;

    // ErrorCatchActivity 에서 받은 데이터
    private String account, service, errdt, errtm, errmsg, status, seq, svccd, commsg;

    // 서버 통신할 데이터
    private String post_svccd, post_seq, post_comfg, post_com_msg, post_updid;

    private String resultCode = null;
    private TextView tv_accountName, tv_serviceName, tv_errDate, tv_errTime, tv_errMsg;
    private EditText et_msg;
    private RadioGroup radioGroup;
    private RadioButton radioButton_yes, radioButton_no;
    ActivityErrorInfoBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityErrorInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SharedPreferences sharedPreferences = getSharedPreferences("LoginInfo", MODE_PRIVATE);
        post_updid = sharedPreferences.getString("id", "NO ID");

        System.out.println("ID :  " + post_updid);

        // 라디오 버튼 설정
        radioButton_yes = binding.errorInfoRadioYes;
        radioButton_no = binding.errorInfoRadioNo;
        radioButton_yes.setOnClickListener(radioButtonClickListener);
        radioButton_no.setOnClickListener(radioButtonClickListener);

        // 라디오 그룹 설정
        radioGroup = binding.errorInfoRadioGroup;
        radioGroup.setOnCheckedChangeListener(radioGroupButtonChangeListener);

        // Intent에서 받아온 값 저장
        account = getIntent().getStringExtra("account");
        service = getIntent().getStringExtra("service");
        errdt = getIntent().getStringExtra("errdt");
        errtm = getIntent().getStringExtra("errtm");
        errmsg = getIntent().getStringExtra("errmsg");
        status = getIntent().getStringExtra("status");
        svccd = getIntent().getStringExtra("svccd");
        seq = getIntent().getStringExtra("seq");
        commsg = getIntent().getStringExtra("commsg");

        // 라디오 버튼의 초기 상태
        if (status.equals("1")) {
            radioButton_yes.setChecked(true);
        } else {
            radioButton_no.setChecked(true);
        }

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

        et_msg = binding.errorInfoEtMemo;
        et_msg.setText(commsg);

        btn_ok = binding.errorInfoBtnOk;
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                post_svccd = svccd;
                post_seq = seq;
                post_com_msg = et_msg.getText().toString();

                resultCode = ErrorInfoConnection.postErrorInfo(post_svccd, post_seq, post_comfg, post_com_msg, post_updid);
                if (resultCode.equals("00")) {
                    Toast.makeText(getApplicationContext(), "정상 처리되었습니다.", Toast.LENGTH_SHORT).show();
                    errorInfoModified = true;
                    System.out.println("modify" + errorInfoModified);
                    finish();
                } else if (resultCode.equals("01")) {
                    Toast.makeText(getApplicationContext(), "업데이트 실패", Toast.LENGTH_SHORT).show();
                } else if (resultCode.equals("02")) {
                    Toast.makeText(getApplicationContext(), "키값 업데이트 오류", Toast.LENGTH_SHORT).show();
                } else if (resultCode.equals("03")) {
                    Toast.makeText(getApplicationContext(), "기타 입력값 오류", Toast.LENGTH_SHORT).show();
                } else if (resultCode.equals("99")) {
                    Toast.makeText(getApplicationContext(), "시스템 에러", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // 라디오 버튼 클릭 리스너
    RadioButton.OnClickListener radioButtonClickListener = new RadioButton.OnClickListener() {
        @Override
        public void onClick(View view) {

        }
    };

    RadioGroup.OnCheckedChangeListener radioGroupButtonChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int i) {
            if (i == R.id.error_info_radio_yes) { // 처리 완료
                post_comfg = "1";
                System.out.println(post_comfg);
            } else if (i == R.id.error_info_radio_no) { // 미처리
                post_comfg = "0";
                System.out.println(post_comfg);
            }
        }
    };
}