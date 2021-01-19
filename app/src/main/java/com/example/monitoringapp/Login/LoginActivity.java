package com.example.monitoringapp.Login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.monitoringapp.Main.MainActivity;
import com.example.monitoringapp.databinding.ActivityLoginBinding;

import static com.example.monitoringapp.Login.LoginConnection.login_id;
import static com.example.monitoringapp.Login.LoginConnection.login_name;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;
    private Button btn_login;
    private EditText et_id, et_pw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        et_id = binding.loginEtId;
        et_pw = binding.loginEtPassword;

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build(); StrictMode.setThreadPolicy(policy); }

        btn_login = binding.loginBtn;
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println(et_id.getText().toString() + "비번" + et_pw.getText().toString());

                String resultCode = LoginConnection.getLogin(et_id.getText().toString(), et_pw.getText().toString());
                if (resultCode.equals("00")) { // 정상
                    SharedPreferences sharedPreferences = getSharedPreferences("LoginInfo", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("id", login_id);
                    editor.putString("name", login_name);
                    editor.apply(); // 아이디 저장

                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                else if (resultCode.equals("01")) {
                    Toast.makeText(getApplicationContext(), "입력값 오류", Toast.LENGTH_SHORT).show();
                }
                else if (resultCode.equals("02")) {
                    Toast.makeText(getApplicationContext(), "사용불가 아이디", Toast.LENGTH_SHORT).show();
                    et_id.getText().clear();
                    et_pw.getText().clear();
                }
                else if (resultCode.equals("03")) {
                    Toast.makeText(getApplicationContext(), "로그인 실패", Toast.LENGTH_SHORT).show();
                    et_id.getText().clear();
                    et_pw.getText().clear();
                }
                else if (resultCode.equals("99")) {
                    Toast.makeText(getApplicationContext(), "시스템 에러", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}