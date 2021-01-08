package com.example.monitoringapp.ServiceExecution;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.monitoringapp.ServiceExecution.Adapter.ServiceAdapter;
import com.example.monitoringapp.ServiceExecution.Connection.ServiceSearchConnection;
import com.example.monitoringapp.databinding.ActivityServiceExecutionBinding;

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

public class ServiceExecutionActivity extends AppCompatActivity {

    private Spinner spinner1, spinner2;
    private ActivityServiceExecutionBinding binding;
    private Button btn_search;

    private ArrayList<String> item_accountSearch = new ArrayList<String>();
    public static ArrayList<String> item_serviceSearch = new ArrayList<String>();

    private ArrayList<String> item_accountSearchCode = new ArrayList<String>();

    private String selectedAccount; // 선택된 대리점 이름
    private String selectedService; // 선택된 서비스 이름

    StringBuffer stringBuffer = new StringBuffer();
//    StringBuffer stringBuffer2 = new StringBuffer();
    private String accountSearchData;

    public static String forTest;

    public static String se_AGCD; // 대리점코드
    public static String se_AGNM; // 대리점이름

    public static String se_SVCCD; // 서비스코드
    public static String se_SVCNM; // 서비스명
    public static String se_RESULT; // 결과
    public static String se_UPDDT; // 수정일자
    public static String se_UPDTM; // 수정시간


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityServiceExecutionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

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
//            jHArr_send.put(jHObj_send); // 이거 하면 {"header":[{"TYPE":"01"}],"body":[{}]}
//            jBObj_send.put("mdn", sMdn); 일단 무조건 빼기
            jBArr_send.put(jBObj_send);

//            jTObj_send.put("header", jHArr_send); // 이거 하면 {"header":[{"TYPE":"01"}],"body":[{}]}
            jTObj_send.put("header", jHObj_send); // 이거 하면 {"header":{"TYPE":"01"},"body":[{}]}
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
            // {"header":{"TYPE":"02"},"body":{"AGCD":"","SVCCD":""}}
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
            JSONObject object2 = jsonArray2.getJSONObject(0); // body

            item_accountSearch.add("전체 보기");
            for (int i = 0; i < jsonArray2.length(); i++) {
                item_accountSearch.add(jsonArray2.getJSONObject(i).getString("AGNM"));
            }
            item_accountSearch.add("거래처 선택"); // last item

            item_accountSearchCode.add("전체 보기");
            for (int i = 0; i < jsonArray2.length(); i++) {
                item_accountSearchCode.add(jsonArray2.getJSONObject(i).getString("AGCD"));
            }
            item_accountSearchCode.add("거래처 선택"); // last item

            System.out.println("jsonarray1 길이 " + jsonArray1.length());
            System.out.println("jsonarray2 길이 " + jsonArray2.length());

            System.out.println("jsonobject1 길이 " + object1.length());
            System.out.println("jsonobject2 길이 " + object2.length());

            String test1 = object1.getString("TYPE");
            String test2 = object1.getString("RETURNCD");

            String test3 = object2.getString("AGCD"); // 필요한 데이터

            String test4 = object2.getString("AGNM"); // 필요한 데이터
            forTest = test3;

            System.out.println(test1); // type : 01
            System.out.println(test2); // returncd : 00
            System.out.println(test3); // agcd : anx
            System.out.println(test4); // agnm : 에넥스텔레콤
        } catch (JSONException e) {
            System.out.println(e);
            e.printStackTrace();
        }

        spinner1 = binding.serviceExecutionSpinner1;

        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, item_accountSearch);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter1);

        ServiceSearchConnection.getServiceSearch();


        final ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, item_serviceSearch);
        //  아이템이 선택되었을 때의 이벤트 처리 리스너 설정
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getApplicationContext(), item_accountSearch.get(i) + " 선택됨", Toast.LENGTH_SHORT).show();

                se_AGCD = item_accountSearchCode.get(i); // 선택된 대리점 코드
                System.out.println("코드" + se_AGCD);

                selectedAccount = item_accountSearch.get(i);
                System.out.println("선택된 대리점 : " + selectedAccount);

                // 서비스 조회 스피너
                spinner2 = binding.serviceExecutionSpinner2;

//                ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, item_serviceSearch);
                adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner2.setAdapter(adapter2);


                spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        Toast.makeText(getApplicationContext(), item_serviceSearch.get(i) + " 선택됨", Toast.LENGTH_SHORT).show();
                        selectedService = item_serviceSearch.get(i);
                        System.out.println("선택된 서비스 : " + selectedService);
                        System.out.println("호출 + 선택된서비스");
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
                ServiceSearchConnection.getServiceSearch();
                ArrayList<ServiceItem> dataList = new ArrayList<>();
                for (int i = 0; i < 5; i++) {
                    dataList.add(new ServiceItem("2021-01-05", "14:31:37", "에넥스텔레콤", "MVNO 자동충전 서비스", "정상"));
                }
                ServiceAdapter serviceAdapter = new ServiceAdapter(dataList);
                recyclerView.setAdapter(serviceAdapter);
                recyclerView.getAdapter().notifyDataSetChanged();
            }
        });
    }
}