package com.example.monitoringapp.ServiceExecution;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.monitoringapp.ErrorCatch.ErrorMain.ErrorCatchActivity;
import com.example.monitoringapp.Login.LoginActivity;
import com.example.monitoringapp.Main.MainActivity;
import com.example.monitoringapp.R;
import com.example.monitoringapp.Scheduler.SchedulerActivity;
import com.example.monitoringapp.ServerDisk.ServerDiskActivity;
import com.example.monitoringapp.databinding.ActivityServiceExecutionBinding;
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

import static com.example.monitoringapp.BaseActivity.BASE_URL;
import static com.example.monitoringapp.ServiceExecution.ServiceSearchConnection.dataList;

public class ServiceExecutionActivity extends AppCompatActivity {

    MainActivity mainActivity = (MainActivity) MainActivity._MainActivity;

    private ActivityServiceExecutionBinding binding;
    public static boolean clicked = false; // ServiceSearchConnection에서 거래처가 선택되었는지 확인하기 위한 변수
    private boolean hideBtnClicked = false; //
    private Spinner spinner1, spinner2;
    private Button btn_search, btn_menu, btn_logout; // 조회하기, 상단바 오른쪽 메뉴 버튼
    private LinearLayout linearLayout; // 필터 버튼
    private TextView tv_accountLabel, tv_serviceLabel;

    private ArrayList<String> item_accountSearch = new ArrayList<String>(); // 거래처 목록
    public static ArrayList<String> item_serviceSearch = new ArrayList<String>(); // 서비스 목록 (ServiceSearchConnection에서 받아옴)
    private ArrayList<String> item_accountSearchCode = new ArrayList<String>(); // 서비스 코드 목록

    StringBuffer stringBuffer = new StringBuffer();
    private String accountSearchData;

    public static String se_AGCD; // 대리점코드
    public static String se_SVCNM = ""; // 서비스명

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityServiceExecutionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = (Toolbar) binding.serviceExecutionToolbar;
        setSupportActionBar(toolbar);

//        ActionBar actionBar = getSupportActionBar(); androidx에서 X
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 뒤로
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.icon_back);

        drawerLayout = binding.serviceExecutionDl;

        btn_menu = binding.serviceExecutionBtnMenu;
        btn_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.END);
            }
        });

        // 오른쪽 메뉴 버튼
        NavigationView navigationView = (NavigationView) binding.serviceExecutionNv;
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                item.setChecked(true);
                drawerLayout.closeDrawers();

                int id = item.getItemId();

                if (id == R.id.menu_service_execution) {
                    item.setChecked(false);
                    Toast.makeText(getApplicationContext(), "현재 페이지입니다.", Toast.LENGTH_SHORT).show();
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
                    Intent intent = new Intent(getApplicationContext(), ErrorCatchActivity.class);
                    startActivity(intent);
                    finish();
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

        item_serviceSearch.add("전체 보기"); // 스피너에 보여주고, 개수를 맞춰주기 위해 추가

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build(); StrictMode.setThreadPolicy(policy); }

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

            item_accountSearch.add("전체 보기");
            for (int i = 0; i < jsonArray2.length(); i++) {
                item_accountSearch.add(jsonArray2.getJSONObject(i).getString("AGNM"));
            }

            item_accountSearchCode.add("전체 보기");
            for (int i = 0; i < jsonArray2.length(); i++) {
                item_accountSearchCode.add(jsonArray2.getJSONObject(i).getString("AGCD"));
            }
        } catch (JSONException e) {
            System.out.println(e);
            e.printStackTrace();
        }

        spinner1 = binding.serviceExecutionSpinner1;

        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, item_accountSearch);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter1);

        ServiceSearchConnection.getServiceSearch(se_AGCD, se_SVCNM);


        final ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, item_serviceSearch);
        //  아이템이 선택되었을 때의 이벤트 처리 리스너 설정
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (i == 0) {
                    se_AGCD = "";
                }
                else {
                    se_AGCD = item_accountSearchCode.get(i); // 선택된 대리점 코드
                }
                System.out.println("선택된 대리점 코드 : " + se_AGCD);

                // 서비스 조회 스피너
                spinner2 = binding.serviceExecutionSpinner2;

                clicked = true;
                ServiceSearchConnection.getServiceSearch(se_AGCD, se_SVCNM);

                adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner2.setAdapter(adapter2);

                spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                        if (item_serviceSearch.get(i) == "전체 보기") {
                            se_SVCNM = "";
                        } else {
                            se_SVCNM = item_serviceSearch.get(i);
                        }
                        System.out.println("선택된 서비스 : " + se_SVCNM);
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

        // 7. 리사이클러뷰 가져오기
        final RecyclerView recyclerView = binding.serviceExecutionRecyclerview;
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        btn_search = binding.serviceExecutionBtn;
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clicked = false;

                System.out.println("버튼 클릭 시의 AGCD : " + se_AGCD);
                ServiceSearchConnection.getServiceSearch(se_AGCD, se_SVCNM);
                ServiceAdapter serviceAdapter = new ServiceAdapter(dataList);
                recyclerView.setAdapter(serviceAdapter);
                recyclerView.getAdapter().notifyDataSetChanged();
            }
        });


        tv_accountLabel = binding.serviceExecutionTvLabel1;
        tv_serviceLabel = binding.serviceExecutionTvLabel2;

        linearLayout = binding.serviceExecutionBtnHide;
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!hideBtnClicked) { // 한번 클릭
                    hideBtnClicked = true;
                    spinner1.setVisibility(view.GONE);
                    spinner2.setVisibility(view.GONE);
                    tv_accountLabel.setVisibility(view.GONE);
                    tv_serviceLabel.setVisibility(view.GONE);
                    btn_search.setVisibility(view.GONE);
                } else { // 두번 클릭
                    hideBtnClicked = false;
                    spinner1.setVisibility(view.VISIBLE);
                    spinner2.setVisibility(view.VISIBLE);
                    tv_accountLabel.setVisibility(view.VISIBLE);
                    tv_serviceLabel.setVisibility(view.VISIBLE);
                    btn_search.setVisibility(view.VISIBLE);
                }
            }
        });
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