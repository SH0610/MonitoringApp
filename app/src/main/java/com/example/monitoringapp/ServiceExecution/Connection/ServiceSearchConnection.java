package com.example.monitoringapp.ServiceExecution.Connection;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.example.monitoringapp.BaseActivity.BASE_URL;
import static com.example.monitoringapp.ServiceExecution.ServiceExecutionActivity.forTest;
import static com.example.monitoringapp.ServiceExecution.ServiceExecutionActivity.item_serviceSearch;

public class ServiceSearchConnection {

    private static String accountSearchData;

    public static void getServiceSearch() {
        System.out.println("호출 선택");
        URL url = null;
        HttpURLConnection conn_Url = null;
        String sTarget_url = ""; //호출할 url
        String sJson = ""; //전달할 json data string

        sTarget_url = BASE_URL; //호출할 url

        StringBuffer stringBuffer2 = new StringBuffer();

        // 서비스 목록 조회
//        Log.d("서비스목록조회 TARGET URL : ", sTarget_url);

        //전달 데이터 (json)
        // {"header":{"TYPE":"01"},"body":[{}]}
        // {"header":{"TYPE":"02"},"body":{"AGCD":"", "SVCCD":""}}
//        JSONArray jHArr_send = new JSONArray();
//        JSONArray jBArr_send = new JSONArray();
        JSONObject jHObj_send2 = new JSONObject();
        JSONObject jBObj_send2 = new JSONObject();
        JSONObject jTObj_send2 = new JSONObject();
        jHObj_send2 = new JSONObject();
        jBObj_send2 = new JSONObject();

        jTObj_send2 = new JSONObject();

        try {
            jHObj_send2.put("TYPE", "02");
//            jHArr_send.put(jHObj_send); // 이거 하면 {"header":[{"TYPE":"01"}],"body":[{}]}
//            jBObj_send.put("mdn", sMdn); 일단 무조건 빼기

//            jBArr_send.put(jBObj_send);
            jBObj_send2.put("AGCD", "");
            jBObj_send2.put("SVCCD", "");


//            jTObj_send.put("header", jHArr_send); // 이거 하면 {"header":[{"TYPE":"01"}],"body":[{}]}
            jTObj_send2.put("header", jHObj_send2); // 이거 하면 {"header":{"TYPE":"01"},"body":[{}]}
            jTObj_send2.put("body", jBObj_send2);

            sJson = jTObj_send2.toString();
            System.out.println(sJson);
            System.out.println("hihi" + sJson);

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
                stringBuffer2.append(line);
            }

            Log.d("RESPONSE", sJson_get);
            System.out.println("hi" + sJson_get);
            accountSearchData = stringBuffer2.toString();

            System.out.println("받은 데이터 (서비스) 조회) : " + accountSearchData);

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

            item_serviceSearch.add("전체 보기");
            System.out.println("fortest" + forTest);
            for (int i = 0; i < jsonArray2.length(); i++) {
                if (jsonArray2.getJSONObject(i).getString("AGCD").equals(forTest)) {
                    System.out.println("testok  " + jsonArray2.getJSONObject(i).getString("SVCNM"));
                    item_serviceSearch.add(jsonArray2.getJSONObject(i).getString("SVCNM"));
                }

            }

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