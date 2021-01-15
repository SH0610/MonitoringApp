package com.example.monitoringapp.Scheduler;

import android.util.Log;

import com.example.monitoringapp.ServerDisk.ServerDiskItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.example.monitoringapp.BaseActivity.BASE_URL;
import static com.example.monitoringapp.Scheduler.SchedulerActivity.schedulerList;

public class SchedulerConnection {

    private static String schedulerData;

    public static String getSchedulerData(String STARTDT, String ENDDT) {
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

        try {
            jHObj_send.put("TYPE", "04");
            jBObj_send.put("AGCD", "");
            jBObj_send.put("STARTDT", STARTDT);
            jBObj_send.put("ENDDT", ENDDT);

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
            // {"header":{"TYPE":"02"},"body":{"AGCD":"","SVCCD":""}}
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
            schedulerData = stringBuffer_sc.toString();

            System.out.println("받은 데이터 (스케쥴러 조회) : " + schedulerData);

            // 닫기
            osw.close();
            br.close();
        } catch(Exception ex) {
            System.out.println(ex.toString());
        }

        try {
            // 응답 JSON 파싱
            JSONObject receiveJSONObject = new JSONObject(schedulerData);
            // header : Array
            JSONArray jsonArray1 = receiveJSONObject.getJSONArray("header");
            // body : Array
            JSONArray jsonArray2 = receiveJSONObject.getJSONArray("body");
            // TYPE, RETURNCD : Object
            JSONObject object1 = jsonArray1.getJSONObject(0); // TYpe (header)

            resultCode = object1.getString("RETURNCD"); // 응답코드

            schedulerList.clear();
            for (int i = 0; i < jsonArray2.length(); i++) {
                String forParsingDate, forParsingTime, parse_year, parse_month, parse_day, parse_hour, parse_min, parse_sec; // 실제
                String forParsingDate2, forParsingTime2, parse_year2, parse_month2, parse_day2, parse_hour2, parse_min2, parse_sec2; // 예상

                String date, time; // 실제
                String date2, time2; // 예상
                if (jsonArray2.getJSONObject(i).getString("UPDDT").equals("") || jsonArray2.getJSONObject(i).getString("UPDTM").equals("")) {
                    date = "업데이트 정보 없음";
                    time = "";
                } else {
                    forParsingDate = jsonArray2.getJSONObject(i).getString("UPDDT");
                    forParsingTime = jsonArray2.getJSONObject(i).getString("UPDTM");

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
                }

                forParsingDate2 = jsonArray2.getJSONObject(i).getString("EXEDT");
                forParsingTime2 = jsonArray2.getJSONObject(i).getString("EXETM");

                parse_year2 = forParsingDate2.substring(0, 4);
                parse_month2 = forParsingDate2.substring(4, 6);
                parse_day2 = forParsingDate2.substring(6, 8);

                parse_hour2 = forParsingTime2.substring(0, 2);
                parse_min2 = forParsingTime2.substring(2, 4);
                parse_sec2 = forParsingTime2.substring(4, 6);

                date2 = parse_year2 + "-" + parse_month2 + "-" + parse_day2;
                time2 = parse_hour2 + ":" + parse_min2 + ":" + parse_sec2;

                schedulerList.add(new SchedulerItem(date, time, date2, time2, jsonArray2.getJSONObject(i).getString("AGNM"), jsonArray2.getJSONObject(i).getString("SVCNM"), jsonArray2.getJSONObject(i).getString("EXEFG")));
            }
        } catch (JSONException e) {
            System.out.println(e);
            e.printStackTrace();
        }
        return resultCode;
    }
}
