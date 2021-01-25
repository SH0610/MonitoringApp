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
import static com.example.monitoringapp.ErrorCatch.ErrorMain.ErrorCatchActivity.errorList;
import static com.example.monitoringapp.ErrorCatch.ErrorMain.ErrorCatchActivity.item_serviceSearch2;

public class ErrorCatchConnection {

    private static String errorData;

    public static String getErrorInfo(String account, String service, String STARTDT, String ENDDT) {
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
            jHObj_send2.put("TYPE", "05"); // header

            // body
            jBObj_send2.put("AGCD", account);
            jBObj_send2.put("SVCCD", service);
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
            JSONObject object2 = null; // body

            resultCode = object1.getString("RETURNCD");
            System.out.println("result : " + resultCode);

            errorList.clear();
            for (int i = 0; i < jsonArray2.length(); i++) {
                String date = null, time = null; // 실제

                String forParsingDate, forParsingTime, parse_year, parse_month, parse_day, parse_hour, parse_min, parse_sec; // 실제

                forParsingDate = jsonArray2.getJSONObject(i).getString("ERRDT");
                forParsingTime = jsonArray2.getJSONObject(i).getString("ERRTM");

                System.out.println("TEST" + forParsingDate);
                System.out.println("TEST" + forParsingTime);

                parse_year = forParsingDate.substring(0, 4);
                parse_month = forParsingDate.substring(4, 6);
                parse_day = forParsingDate.substring(6, 8);

                parse_hour = forParsingTime.substring(0, 2);
                parse_min = forParsingTime.substring(2, 4);
                parse_sec = forParsingTime.substring(4, 6);

                date = parse_year + "-" + parse_month + "-" + parse_day;
                time = parse_hour + ":" + parse_min + ":" + parse_sec;

                errorList.add(new ErrorItem(jsonArray2.getJSONObject(i).getString("AGNM"), jsonArray2.getJSONObject(i).getString("SVCCD"), jsonArray2.getJSONObject(i).getString("SVCNM"), jsonArray2.getJSONObject(i).getString("SEQ"), jsonArray2.getJSONObject(i).getString("ECD"), jsonArray2.getJSONObject(i).getString("ERR_MSG"), date, time, jsonArray2.getJSONObject(i).getString("COMFG"), jsonArray2.getJSONObject(i).getString("COM_MSG"), jsonArray2.getJSONObject(i).getString("ENTDT"), jsonArray2.getJSONObject(i).getString("ENTTM"), jsonArray2.getJSONObject(i).getString("REPORTDT"), jsonArray2.getJSONObject(i).getString("REPORTTM"), jsonArray2.getJSONObject(i).getString("UPDDT"), jsonArray2.getJSONObject(i).getString("UPDTM")));
            }
        } catch (JSONException e) {
            System.out.println(e);
            e.printStackTrace();
        }

        return resultCode;
    }
}