package com.example.monitoringapp.Main;

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

public class MainConnection {
    private static String mainData;
    public static String errorCnt, scheduleCnt;

    public static String getMainData(String STARTDT, String ENDDT) {
        StringBuffer stringBuffer_sc = new StringBuffer();
        String resultCode = null;

        URL url = null;
        HttpURLConnection conn_Url = null;
        String sTarget_url = ""; //호출할 url
        String sJson = ""; //전달할 json data string

        sTarget_url = BASE_URL; //호출할 url

        Log.d("TARGET URL : ", sTarget_url);

        //전달 데이터 (json)
        JSONArray jHArr_send = new JSONArray();
        JSONArray jBArr_send = new JSONArray();
        JSONObject jHObj_send = new JSONObject();
        JSONObject jBObj_send = new JSONObject();
        JSONObject jTObj_send = new JSONObject();

        String parseSTARTDT = STARTDT.replace("-", "");
        String parseENDDT = ENDDT.replace("-", "");

        try {
            jHObj_send.put("TYPE", "08");
            jBObj_send.put("STARTDT", parseSTARTDT);
            jBObj_send.put("ENDDT", parseENDDT);

            jTObj_send.put("header", jHObj_send);
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

            BufferedReader br = null;
            br = new BufferedReader(new InputStreamReader(conn_Url.getInputStream(), "UTF-8"));
            System.out.println("InputStreamReader ");
            String line = null;
            String sJson_get = "";
            while ((line = br.readLine()) != null) {
                sJson_get = line;
                System.out.println(line);
                stringBuffer_sc.append(line);
            }

            Log.d("RESPONSE", sJson_get);
            System.out.println("hi" + sJson_get);
            mainData = stringBuffer_sc.toString();

            System.out.println("받은 데이터 (메인 조회) : " + mainData);

            // 닫기
            osw.close();
            br.close();
        } catch(Exception ex) {
            System.out.println(ex.toString());
        }

        try {
            // 응답 JSON 파싱
            JSONObject receiveJSONObject = new JSONObject(mainData);
            // header : Array
            JSONArray jsonArray1 = receiveJSONObject.getJSONArray("header");
            // body : Array
            JSONArray jsonArray2 = receiveJSONObject.getJSONArray("body");
            // TYPE, RETURNCD : Object
            JSONObject object1 = jsonArray1.getJSONObject(0); // TYpe (header)

            resultCode = object1.getString("RETURNCD"); // 응답코드


            errorCnt = jsonArray2.getJSONObject(0).getString("HISTORY_CNT");
            scheduleCnt = jsonArray2.getJSONObject(0).getString("SCHEDULE_CNT");

        } catch (JSONException e) {
            System.out.println(e);
            e.printStackTrace();
        }
        return resultCode;
    }
}
