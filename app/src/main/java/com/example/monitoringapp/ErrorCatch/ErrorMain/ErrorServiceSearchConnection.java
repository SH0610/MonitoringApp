package com.example.monitoringapp.ErrorCatch.ErrorMain;

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
import static com.example.monitoringapp.ErrorCatch.ErrorMain.ErrorCatchActivity.ec_SVCNM;
import static com.example.monitoringapp.ErrorCatch.ErrorMain.ErrorCatchActivity.error_clicked;
import static com.example.monitoringapp.ErrorCatch.ErrorMain.ErrorCatchActivity.item_ServiceSearchCode2;
import static com.example.monitoringapp.ErrorCatch.ErrorMain.ErrorCatchActivity.item_serviceSearch2;
import static com.example.monitoringapp.ServiceExecution.ServiceExecutionActivity.clicked;

public class ErrorServiceSearchConnection {

    private static String accountSearchData;

    public static void getServiceSearch(String AGCD, String SVCNM) {
        System.out.println("호출 선택");
        URL url = null;
        HttpURLConnection conn_Url = null;
        String sTarget_url = ""; //호출할 url
        String sJson = ""; //전달할 json data string

        sTarget_url = BASE_URL; //호출할 url

        StringBuffer stringBuffer2 = new StringBuffer();

        // 전달 데이터 (json)
        JSONObject jHObj_send2 = new JSONObject();
        JSONObject jBObj_send2 = new JSONObject();
        JSONObject jTObj_send2 = new JSONObject();
        jHObj_send2 = new JSONObject();
        jBObj_send2 = new JSONObject();

        jTObj_send2 = new JSONObject();

        try {
            jHObj_send2.put("TYPE", "02");

            if (error_clicked) {
                // 거래처 선택되면
                clicked = false;
                error_clicked = false;
                System.out.println("거래처 선택완료");
                jBObj_send2.put("AGCD", "");
                jBObj_send2.put("SVCCD", "");

            } else {
                System.out.println("거래처 선택ㄴ");
                jBObj_send2.put("AGCD", AGCD);
                jBObj_send2.put("SVCCD", "");
            }

            jTObj_send2.put("header", jHObj_send2); // {"header":{"TYPE":"01"},"body":[{}]}
            jTObj_send2.put("body", jBObj_send2);

            sJson = jTObj_send2.toString();
            System.out.println("서비스 요청 메시지 : " + sJson);

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
            JSONObject object2 = null; // body

            item_serviceSearch2.clear();
            item_serviceSearch2.add("전체 보기");
            item_ServiceSearchCode2.clear();
            item_ServiceSearchCode2.add("전체 보기");
            for (int i = 0; i < jsonArray2.length(); i++) {
                if (jsonArray2.getJSONObject(i).getString("AGCD").equals(AGCD)) {
                    System.out.println("testok  " + jsonArray2.getJSONObject(i).getString("SVCNM"));
                    item_serviceSearch2.add(jsonArray2.getJSONObject(i).getString("SVCNM"));
                    System.out.println("testok svccd " + jsonArray2.getJSONObject(i).getString("SVCCD"));
                    item_ServiceSearchCode2.add(jsonArray2.getJSONObject(i).getString("SVCCD"));
                }
            }

//            item_ServiceSearchCode2.clear();
//            item_ServiceSearchCode2.add("전체 보기");
//            for (int i = 0; i < jsonArray2.length(); i++) {
//                if (jsonArray2.getJSONObject(i).getString("SVCCD").equals(ec_SVCNM)) {
//                    System.out.println("testok  " + jsonArray2.getJSONObject(i).getString("SVCCD"));
//                    item_ServiceSearchCode2.add(jsonArray2.getJSONObject(i).getString("SVCCD"));
//                }
//            }
        } catch (JSONException e) {
            System.out.println(e);
            e.printStackTrace();
        }
    }
}
