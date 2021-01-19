package com.example.monitoringapp.ErrorCatch;

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

public class ErrorInfoConnection {

    private static String errorInfoData;

    public static String postErrorInfo(String svccd, String seq, String comfg, String com_msg, String updid) {
        String resultCode = null;

        StringBuffer stringBuffer = new StringBuffer();

        URL url = null;
        HttpURLConnection conn_Url = null;
        String sTarget_url = ""; //호출할 url
        String sJson = ""; //전달할 json data string

        sTarget_url = BASE_URL; //호출할 url

        JSONObject jHObj_send2 = new JSONObject();
        JSONObject jBObj_send2 = new JSONObject();
        JSONObject jTObj_send2 = new JSONObject();
        jHObj_send2 = new JSONObject();
        jBObj_send2 = new JSONObject();

        jTObj_send2 = new JSONObject();

        try {
            // POST

            // header
            jHObj_send2.put("TYPE", "06");

            // body
            jBObj_send2.put("SVCCD", svccd);
            jBObj_send2.put("SEQ", seq);
            jBObj_send2.put("COMFG", comfg);
            jBObj_send2.put("COM_MSG", com_msg);
            jBObj_send2.put("UPDID", updid);

            jTObj_send2.put("header", jHObj_send2); // header
            jTObj_send2.put("body", jBObj_send2); // body

            sJson = jTObj_send2.toString();
            System.out.println("POST 내용 (ErrorCatch) : " + sJson);

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
            errorInfoData = stringBuffer.toString();

            System.out.println("받은 데이터 (에러) 조회) : " + errorInfoData);

            // 닫기
            osw.close();
            br.close();
        } catch(Exception ex) {
            System.out.println(ex.toString());
        }

        try {
            // 응답 JSON 파싱
            JSONObject receiveJSONObject = new JSONObject(errorInfoData);
            // header : Array
            JSONArray jsonArray1 = receiveJSONObject.getJSONArray("header");
//            // body : Array
//            JSONArray jsonArray2 = receiveJSONObject.getJSONArray("body");
            // TYPE, RETURNCD : Object
            JSONObject object1 = jsonArray1.getJSONObject(0); // returncd (header)

            resultCode = object1.getString("RETURNCD");
            System.out.println("result : " + resultCode);
        } catch (JSONException e) {
            System.out.println(e);
            e.printStackTrace();
        }

        return resultCode;
    }
}