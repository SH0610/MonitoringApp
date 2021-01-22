package com.example.monitoringapp.ErrorCatch.ErrorMain;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.monitoringapp.Login.LoginActivity;
import com.example.monitoringapp.Main.MainActivity;
import com.example.monitoringapp.R;
import com.example.monitoringapp.Scheduler.SchedulerActivity;
import com.example.monitoringapp.ServerDisk.ServerDiskActivity;
import com.example.monitoringapp.ServiceExecution.ServiceExecutionActivity;
import com.example.monitoringapp.databinding.ActivityErrorCatchBinding;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;

import static com.example.monitoringapp.BaseActivity.BASE_URL;
import static com.example.monitoringapp.BaseActivity.getTodayDate;

public class ErrorCatchActivity extends AppCompatActivity {

    MainActivity mainActivity = (MainActivity) MainActivity._MainActivity;

    private Spinner spinner1, spinner2;

    public static boolean error_clicked = false;
    private ArrayList<String> item_accountSearch2 = new ArrayList<String>();
    private ArrayList<String> item_accountSearchCode2 = new ArrayList<String>();
    public static ArrayList<String> item_serviceSearch2 = new ArrayList<String>();
    public static ArrayList<String> item_ServiceSearchCode2 = new ArrayList<String>();

    StringBuffer stringBuffer = new StringBuffer();
    private String accountSearchData;

    public static String ec_AGCD; // 대리점코드
    public static String ec_SVCNM = ""; // 서비스명

    private Button btn_search, btn_menu, btn_logout;
    private LinearLayout layout_btn_filter;
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

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityErrorCatchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        item_serviceSearch2.add("전체 보기");
        item_ServiceSearchCode2.add("전체 보기");

        if (android.os.Build.VERSION.SDK_INT > 9) { StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build(); StrictMode.setThreadPolicy(policy); }

        // 거래처 조회

        URL url = null;
        HttpURLConnection conn_Url = null;
        String sTarget_url = ""; //호출할 url
        String sJson = ""; //전달할 json data string

        sTarget_url = BASE_URL; //호출할 url

        Log.d("TARGET URL : ", sTarget_url);

        //전달 데이터 (json)
        // {"header":{"TYPE":"01"},"body":[{}]}
        JSONArray jHArr_send = new JSONArray();
        JSONArray jBArr_send = new JSONArray();
        JSONObject jHObj_send = new JSONObject();
        JSONObject jBObj_send = new JSONObject();
        JSONObject jTObj_send = new JSONObject();

        try {
            jHObj_send.put("TYPE", "01");
            jBArr_send.put(jBObj_send);

            jTObj_send.put("header", jHObj_send);
            jTObj_send.put("body", jBArr_send);

            sJson = jTObj_send.toString();
            System.out.println(sJson);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            url = new URL(sTarget_url);
            conn_Url = (HttpURLConnection) url.openConnection();
            conn_Url.setDoInput(true);
            conn_Url.setDoOutput(true);
            conn_Url.setRequestMethod("POST");
            conn_Url.setRequestProperty("Accept", "application/json");
            conn_Url.connect();

            OutputStreamWriter osw = new OutputStreamWriter(conn_Url.getOutputStream());
            osw.write(sJson);
            osw.flush();
            System.out.println("osw.write(sJson); : ");

            BufferedReader br = null;
            br = new BufferedReader(new InputStreamReader(conn_Url.getInputStream(), "UTF-8"));
            System.out.println("InputStreamReader ");
            String line = null;
            String sJson_get = "";
            while ((line = br.readLine()) != null) {
                sJson_get = line;
                System.out.println(line);
                stringBuffer.append(line);
            }

            Log.d("RESPONSE", sJson_get);
            System.out.println("hi" + sJson_get);
            accountSearchData = stringBuffer.toString();

            System.out.println("받은 데이터 (거래처 조회) : " + accountSearchData);

            // 닫기
            osw.close();
            br.close();
        } catch(Exception ex) {
            System.out.println(ex.toString());
        }

        try {
            // 응답 JSON 파싱
            JSONObject receiveJSONObject = new JSONObject(accountSearchData);
            // header : Array
            JSONArray jsonArray1 = receiveJSONObject.getJSONArray("header");
            // body : Array
            JSONArray jsonArray2 = receiveJSONObject.getJSONArray("body");
            // TYPE, RETURNCD : Object
            JSONObject object1 = jsonArray1.getJSONObject(0); // TYpe (header)
//            JSONObject object2 = jsonArray2.getJSONObject(1); // body

            item_accountSearch2.add("전체 보기");
            for (int i = 0; i < jsonArray2.length(); i++) {
                item_accountSearch2.add(jsonArray2.getJSONObject(i).getString("AGNM"));
            }

            item_accountSearchCode2.add("전체 보기");
            for (int i = 0; i < jsonArray2.length(); i++) {
                item_accountSearchCode2.add(jsonArray2.getJSONObject(i).getString("AGCD"));
            }
        } catch (JSONException e) {
            System.out.println(e);
            e.printStackTrace();
        }

        spinner1 = binding.errorCatchSpinner1;

        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, item_accountSearch2);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter1);

        ErrorServiceSearchConnection.getServiceSearch(ec_AGCD, ec_SVCNM);

        final ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, item_serviceSearch2);
        //  아이템이 선택되었을 때의 이벤트 처리 리스너 설정
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (i == 0) {
                    ec_AGCD = "";
                }
                else {
                    ec_AGCD = item_accountSearchCode2.get(i); // 선택된 대리점 코드
                }
                System.out.println("선택된 대리점 코드 : " + ec_AGCD);

                // 서비스 조회 스피너
                spinner2 = binding.errorCatchSpinner2;

                error_clicked = true;
                ErrorServiceSearchConnection.getServiceSearch(ec_AGCD, ec_SVCNM);

                adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner2.setAdapter(adapter2);

                spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                        if (item_serviceSearch2.get(i).equals("전체 보기")) {
                            ec_SVCNM = "";
                        } else {
                            ec_SVCNM = item_ServiceSearchCode2.get(i);
                        }
                        System.out.println("선택된 서비스 : " + ec_SVCNM);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                        Toast.makeText(getApplicationContext(), "아이템을 선택해주세요.", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(getApplicationContext(), "아이템을 선택해주세요.", Toast.LENGTH_SHORT).show();
            }
        });

        Toolbar toolbar = (Toolbar) binding.errorCatchToolbar;
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 뒤로
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.icon_back);

        drawerLayout = binding.errorCatchDl;

        btn_menu = binding.errorCatchBtnMenu;
        btn_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.END);
            }
        });

        // 오른쪽 메뉴 버튼
        NavigationView navigationView = (NavigationView) binding.errorCatchNv;
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                item.setChecked(true);
                drawerLayout.closeDrawers();

                int id = item.getItemId();

                if (id == R.id.menu_service_execution) {
                    item.setChecked(false);
                    Intent intent = new Intent(getApplicationContext(), ServiceExecutionActivity.class);
                    startActivity(intent);
                    finish();
                }
                else if (id == R.id.menu_server_disk) {
                    item.setChecked(false);
                    Intent intent = new Intent(getApplicationContext(), ServerDiskActivity.class);
                    startActivity(intent);
                    finish();
                }
                else if (id == R.id.menu_scheduler) {
                    item.setChecked(false);
                    Intent intent = new Intent(getApplicationContext(), SchedulerActivity.class);
                    startActivity(intent);
                    finish();
                }
                else if (id == R.id.menu_error) {
                    item.setChecked(false);
                    Toast.makeText(getApplicationContext(), "현재 페이지입니다.", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });

        btn_logout = (Button) navigationView.getHeaderView(0).findViewById(R.id.btn_logout);
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "로그아웃되었습니다.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                mainActivity.finish();
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

                resultCode = ErrorCatchConnection.getErrorInfo(ec_AGCD, ec_SVCNM, STARTDT, ENDDT);

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

        layout_btn_filter = binding.errorCatchBtnHide;
        layout_btn_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!hideBtnClicked) { // 한번 클릭
                    hideBtnClicked = true;
                    spinner1.setVisibility(view.GONE);
                    spinner2.setVisibility(view.GONE);
                    tv_error_catch_label.setVisibility(view.GONE);
                    tv_start.setVisibility(view.GONE);
                    tv_divider.setVisibility(view.GONE);
                    tv_end.setVisibility(view.GONE);
                    btn_search.setVisibility(view.GONE);
                } else { // 두번 클릭
                    hideBtnClicked = false;
                    spinner1.setVisibility(view.VISIBLE);
                    spinner2.setVisibility(view.VISIBLE);
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
            resultCode = ErrorCatchConnection.getErrorInfo(ec_AGCD, ec_SVCNM, STARTDT, ENDDT);

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

    // 뒤로 가기 버튼
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ // 뒤로가기 버튼 눌렀을 때
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
