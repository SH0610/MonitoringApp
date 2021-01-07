package com.example.monitoringapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.monitoringapp.ServiceExecution.ServiceExecutionActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.example.monitoringapp.BaseActivity.BASE_URL;
import static com.example.monitoringapp.ServiceExecution.ServiceExecutionActivity.items1;
import static com.example.monitoringapp.ServiceExecution.ServiceExecutionActivity.items2;

public class MainActivity extends AppCompatActivity {
    StringBuffer stringBuffer = new StringBuffer();
    public static String accountSearchData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (android.os.Build.VERSION.SDK_INT > 9) { StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build(); StrictMode.setThreadPolicy(policy); }

        Button button;
        button = findViewById(R.id.btn_test);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ServiceExecutionActivity.class);
                startActivity(intent);
            }
        });

        URL url = null;
        HttpURLConnection conn_Url = null;
        String sTarget_url = ""; //호출할 url
        String sJson = ""; //전달할 json data string

        sTarget_url = BASE_URL; //호출할 url

        Log.d("TARGcdET URL : ", sTarget_url);

        //전달 데이터 (json)
        // {"header":{"TYPE":"01"},"body":[{}]}
        JSONArray jHArr_send = new JSONArray();
        JSONArray jBArr_send = new JSONArray();
        JSONObject jHObj_send = new JSONObject();
        JSONObject jBObj_send = new JSONObject();
        JSONObject jTObj_send = new JSONObject();
//
//        try {
//            jHObj_send.put("TYPE", "01");
////            jHArr_send.put(jHObj_send); // 이거 하면 {"header":[{"TYPE":"01"}],"body":[{}]}
////            jBObj_send.put("mdn", sMdn); 일단 무조건 빼기
//            jBArr_send.put(jBObj_send);
//
////            jTObj_send.put("header", jHArr_send); // 이거 하면 {"header":[{"TYPE":"01"}],"body":[{}]}
//            jTObj_send.put("header", jHObj_send); // 이거 하면 {"header":{"TYPE":"01"},"body":[{}]}
//            jTObj_send.put("body", jBArr_send);
//
//            sJson = jTObj_send.toString();
//            System.out.println(sJson);
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        try {
//            url = new URL(sTarget_url);
//            conn_Url = (HttpURLConnection) url.openConnection();
//            conn_Url.setDoInput(true);
//            conn_Url.setDoOutput(true);
//            conn_Url.setRequestMethod("POST");
//            conn_Url.setRequestProperty("Accept", "application/json");
//            conn_Url.connect();
//
//            OutputStreamWriter osw = new OutputStreamWriter(conn_Url.getOutputStream());
//            osw.write(sJson);
//            osw.flush();
//            System.out.println("osw.write(sJson); : ");
//            // {"header":{"TYPE":"02"},"body":{"AGCD":"","SVCCD":""}}
//            BufferedReader br = null;
//            br = new BufferedReader(new InputStreamReader(conn_Url.getInputStream(), "UTF-8"));
//            System.out.println("InputStreamReader ");
//            String line = null;
//            String sJson_get = "";
//            while ((line = br.readLine()) != null) {
//                sJson_get = line;
//                System.out.println(line);
//                stringBuffer.append(line);
//            }
//
//            Log.d("RESPONSE", sJson_get);
//            System.out.println("hi" + sJson_get);
//            accountSearchData = stringBuffer.toString();
//
//            System.out.println("gg" + accountSearchData);
//
//            // 닫기
//            osw.close();
//            br.close();
//        } catch(Exception ex) {
//            System.out.println(ex.toString());
//        }
//
//        try {
//            // 응답 JSON 파싱
//            JSONObject receiveJSONObject = new JSONObject(accountSearchData);
//            // header : Array
//            JSONArray jsonArray1 = receiveJSONObject.getJSONArray("header");
//            // body : Array
//            JSONArray jsonArray2 = receiveJSONObject.getJSONArray("body");
//            // TYPE, RETURNCD : Object
//            JSONObject object1 = jsonArray1.getJSONObject(0); // TYpe (header)
//            JSONObject object2 = jsonArray2.getJSONObject(1); // body
//
//            items1.add("전체 보기");
//            for (int i = 0; i < jsonArray2.length(); i++) {
//                items1.add(jsonArray2.getJSONObject(i).getString("AGNM"));
//            }
//            items1.add("거래처 선택"); // last item
//
//            System.out.println("jsonarray1 길이 " + jsonArray1.length());
//            System.out.println("jsonarray2 길이 " + jsonArray2.length());
//
//            System.out.println("jsonobject1 길이 " + object1.length());
//            System.out.println("jsonobject2 길이 " + object2.length());
//
//            String test1 = object1.getString("TYPE");
//            String test2 = object1.getString("RETURNCD");
//
//            String test3 = object2.getString("AGCD");
//
//            String test4 = object2.getString("AGNM"); // 필요한 데이터
//
//            System.out.println(test1); // type : 01
//            System.out.println(test2); // returncd : 00
//            System.out.println(test3); // agcd : anx
//            System.out.println(test4); // agnm : 에넥스텔레콤
//        } catch (JSONException e) {
//            System.out.println(e);
//            e.printStackTrace();
//        }





        // 서비스 목록 조회ㅣㅣㅣ
        Log.d("서비스목록조호 TARGcdET URL : ", sTarget_url);

        //전달 데이터 (json)
        // {"header":{"TYPE":"01"},"body":[{}]}
        // {"header":{"TYPE":"02"},"body":{"AGCD":"", "SVCCD":""}}
//        JSONArray jHArr_send = new JSONArray();
//        JSONArray jBArr_send = new JSONArray();
//        JSONObject jHObj_send = new JSONObject();
//        JSONObject jBObj_send = new JSONObject();
//        JSONObject jTObj_send = new JSONObject();
        jHObj_send = new JSONObject();
        jBObj_send = new JSONObject();

        jTObj_send = new JSONObject();

        try {
            jHObj_send.put("TYPE", "02");
//            jHArr_send.put(jHObj_send); // 이거 하면 {"header":[{"TYPE":"01"}],"body":[{}]}
//            jBObj_send.put("mdn", sMdn); 일단 무조건 빼기

//            jBArr_send.put(jBObj_send);
            jBObj_send.put("AGCD", "");
            jBObj_send.put("SVCCD", "");


//            jTObj_send.put("header", jHArr_send); // 이거 하면 {"header":[{"TYPE":"01"}],"body":[{}]}
            jTObj_send.put("header", jHObj_send); // 이거 하면 {"header":{"TYPE":"01"},"body":[{}]}
            jTObj_send.put("body", jBObj_send);

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

            System.out.println("gg" + accountSearchData);

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
            JSONObject object2 = jsonArray2.getJSONObject(1); // body

            items2.add("전체 보기");
            for (int i = 0; i < jsonArray2.length(); i++) {
                System.out.println("HOHOHOH" + jsonArray2.getJSONObject(i).getString("SVCNM"));
                items2.add(jsonArray2.getJSONObject(i).getString("SVCNM"));
            }
            items2.add("서비스 선택"); // last item

            System.out.println("jsonarray1 길이 " + jsonArray1.length());
            System.out.println("jsonarray2 길이 " + jsonArray2.length());

            System.out.println("jsonobject1 길이 " + object1.length());
            System.out.println("jsonobject2 길이 " + object2.length());

            String test1 = object1.getString("TYPE");
            String test2 = object1.getString("RETURNCD");

            String test3 = object2.getString("AGCD");

            String test4 = object2.getString("AGNM"); // 필요한 데이터

            System.out.println(test1); // type : 01
            System.out.println(test2); // returncd : 00
            System.out.println(test3); // agcd : anx
            System.out.println(test4); // agnm : 에넥스텔레콤
        } catch (JSONException e) {
            System.out.println(e);
            e.printStackTrace();
        }
    }
}
