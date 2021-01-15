package com.example.monitoringapp.ServerDisk;

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
import static com.example.monitoringapp.ServerDisk.ServerDiskActivity.serverDiskList;

public class ServerDiskConnection {

    private static String serverDiskData;


    public static String getServerDisk(String AGCD) {
        String resultCode = null;

        StringBuffer stringBuffer_sd = new StringBuffer();

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

        try {
            jHObj_send.put("TYPE", "03");
            jBObj_send.put("AGCD", AGCD);

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
                stringBuffer_sd.append(line);
            }

            Log.d("RESPONSE", sJson_get);
            System.out.println("hi" + sJson_get);
            serverDiskData = stringBuffer_sd.toString();

            System.out.println("받은 데이터 (디스크 조회) : " + serverDiskData);

            // 닫기
            osw.close();
            br.close();
        } catch(Exception ex) {
            System.out.println(ex.toString());
        }

        try {
            // 응답 JSON 파싱
            JSONObject receiveJSONObject = new JSONObject(serverDiskData);
            // header : Array
            JSONArray jsonArray1 = receiveJSONObject.getJSONArray("header");
            // body : Array
            JSONArray jsonArray2 = receiveJSONObject.getJSONArray("body");
            // TYPE, RETURNCD : Object
            JSONObject object1 = jsonArray1.getJSONObject(0); // TYpe (header)

            resultCode = object1.getString("RETURNCD");

            serverDiskList.clear();
            for (int i = 0; i < jsonArray2.length(); i++) {
                String forParsingDate, forParsingTime, parse_year, parse_month, parse_day, parse_hour, parse_min, parse_sec;

                String date, time;
                if (jsonArray2.getJSONObject(i).getString("UPDDT") == "" || jsonArray2.getJSONObject(i).getString("UPDTM") == "") {
                    date = "업데이트 정보 없음";
                    time = "";
                } else {
                    forParsingDate = jsonArray2.getJSONObject(i).getString("UPDDT");
                    forParsingTime = jsonArray2.getJSONObject(i).getString("UPDTM");

                    parse_year = forParsingDate.substring(0, 4);
                    parse_month = forParsingDate.substring(4, 6);
                    parse_day = forParsingDate.substring(6, 8);

                    parse_hour = forParsingTime.substring(0, 2);
                    parse_min = forParsingTime.substring(2, 4);
                    parse_sec = forParsingTime.substring(4, 6);

                    date = parse_year + "-" + parse_month + "-" + parse_day;
                    time = parse_hour + ":" + parse_min + ":" + parse_sec;
                }
                serverDiskList.add(new ServerDiskItem(jsonArray2.getJSONObject(i).getString("IP"), jsonArray2.getJSONObject(i).getString("REMARK"), jsonArray2.getJSONObject(i).getString("DRIVE_NM"), jsonArray2.getJSONObject(i).getString("TOTAL_SIZE"), jsonArray2.getJSONObject(i).getString("FREE_SIZE"), jsonArray2.getJSONObject(i).getString("USAGE_PERCENT"), date, time));
            }
        } catch (JSONException e) {
            System.out.println(e);
            e.printStackTrace();
        }
        return resultCode;
    }
}
