package com.example.monitoringapp.ErrorCatch;

import android.util.Log;

import com.example.monitoringapp.Login.LoginInfoItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.example.monitoringapp.BaseActivity.BASE_URL;
import static com.example.monitoringapp.ErrorCatch.ErrorCatchActivity.errorList;

public class ErrorCatchConnection {

    private static String errorData;

    public static String getErrorInfo(String STARTDT, String ENDDT) {
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
            jHObj_send2.put("TYPE", "05");

            // body
            jBObj_send2.put("AGCD", "");
            jBObj_send2.put("SVCCD", "");
            jBObj_send2.put("STARTDT", STARTDT);
            jBObj_send2.put("ENDDT", ENDDT);

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
            errorData = stringBuffer.toString();

            System.out.println("받은 데이터 (에러) 조회) : " + errorData);

            // 닫기
            osw.close();
            br.close();
        } catch(Exception ex) {
            System.out.println(ex.toString());
        }

        try {
            // 응답 JSON 파싱
            JSONObject receiveJSONObject = new JSONObject(errorData);
            // header : Array
            JSONArray jsonArray1 = receiveJSONObject.getJSONArray("header");
            // body : Array
            JSONArray jsonArray2 = receiveJSONObject.getJSONArray("body");
            // TYPE, RETURNCD : Object
            JSONObject object1 = jsonArray1.getJSONObject(0); // returncd (header)
//            JSONObject object2 = jsonArray2.getJSONObject(0); // body
            JSONObject object2 = null; // body

            resultCode = object1.getString("RETURNCD");
            System.out.println("result : " + resultCode);

            System.out.println("test : " + jsonArray2.getJSONObject(0).getString("AGCD"));

            errorList.clear();
//            errorList.add(new ErrorItem(jsonArray2.getJSONObject(0).getString("BID"), jsonArray2.getJSONObject(0).getString("BNM")));

            if (resultCode.equals("00")) {
//                login_id = jsonArray2.getJSONObject(0).getString("BID");
//                login_name = jsonArray2.getJSONObject(0).getString("BNM");
            }
        } catch (JSONException e) {
            System.out.println(e);
            e.printStackTrace();
        }

        return resultCode;
    }
}
