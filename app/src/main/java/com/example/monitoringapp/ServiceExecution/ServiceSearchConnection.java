package com.example.monitoringapp.ServiceExecution;

import android.util.Log;

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
import static com.example.monitoringapp.ErrorCatch.ErrorMain.ErrorCatchActivity.item_ServiceSearchCode2;
import static com.example.monitoringapp.ErrorCatch.ErrorMain.ErrorCatchActivity.item_serviceSearch2;
import static com.example.monitoringapp.ServiceExecution.ServiceExecutionActivity.item_serviceSearch;
import static com.example.monitoringapp.ServiceExecution.ServiceExecutionActivity.item_serviceSearchCode;

public class ServiceSearchConnection {

    private static String accountSearchData;
    public static ArrayList<ServiceItem> dataList = new ArrayList<>();
    private static String serviceSearchData;

    public static void getService(String AGCD) {
        System.out.println("ONLY GET SERVICE");
        URL url = null;
        HttpURLConnection conn_Url = null;
        String sTarget_url = ""; //호출할 url
        String sJson = ""; //전달할 json data string

        sTarget_url = BASE_URL; //호출할 url

        StringBuffer stringBuffer1 = new StringBuffer();

        // 전달 데이터 (json)
        JSONObject jHObj_send2 = new JSONObject();
        JSONObject jBObj_send2 = new JSONObject();
        JSONObject jTObj_send2 = new JSONObject();
        jHObj_send2 = new JSONObject();
        jBObj_send2 = new JSONObject();

        jTObj_send2 = new JSONObject();

        try {
            jHObj_send2.put("TYPE", "02");

            jBObj_send2.put("AGCD", AGCD);
            jBObj_send2.put("SVCCD", "");

            jTObj_send2.put("header", jHObj_send2); // {"header":{"TYPE":"01"},"body":[{}]}
            jTObj_send2.put("body", jBObj_send2);

            sJson = jTObj_send2.toString();
            System.out.println("서비스 요청 메시지 (ONLY SERVICE) : " + sJson);

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
                stringBuffer1.append(line);
            }

            Log.d("RESPONSE", sJson_get);
            System.out.println("hi" + sJson_get);
            accountSearchData = stringBuffer1.toString();

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

            if (jsonArray2.length() == 0) { // 서비스가 없을 경우
                dataList.clear();
            }
            else {
                dataList.clear();
                for (int i = 0; i < jsonArray2.length(); i++) {
                    String forParsingDate, forParsingTime, parse_year, parse_month, parse_day, parse_hour, parse_min, parse_sec;

                    String date, time;
                    if (jsonArray2.getJSONObject(i).getString("UPDDT").equals("") || jsonArray2.getJSONObject(i).getString("UPDTM").equals("")) {
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
                    dataList.add(new ServiceItem(date, time, jsonArray2.getJSONObject(i).getString("AGNM"), jsonArray2.getJSONObject(i).getString("SVCNM"), jsonArray2.getJSONObject(i).getString("RESULT")));
                }
            }

            item_serviceSearch.clear();
            item_serviceSearch.add("전체 보기");
            for (int i = 0; i < jsonArray2.length(); i++) {
                item_serviceSearch.add(jsonArray2.getJSONObject(i).getString("SVCNM"));
            }

            item_serviceSearchCode.clear();
            item_serviceSearchCode.add("전체 보기");
            for (int i = 0; i < jsonArray2.length(); i++) {
                item_serviceSearchCode.add(jsonArray2.getJSONObject(i).getString("SVCCD"));
            }

            // ErrorCatch에서의 데이터를 위함
            item_serviceSearch2.clear();
            item_serviceSearch2.add("전체 보기");
            for (int i = 0; i < jsonArray2.length(); i++) {
                item_serviceSearch2.add(jsonArray2.getJSONObject(i).getString("SVCNM"));
            }

            // ErrorCatch에서의 데이터를 위함
            item_ServiceSearchCode2.clear();
            item_ServiceSearchCode2.add("전체 보기");
            for (int i = 0; i < jsonArray2.length(); i++) {
                item_ServiceSearchCode2.add(jsonArray2.getJSONObject(i).getString("SVCCD"));
            }

        } catch (JSONException e) {
            System.out.println(e);
            e.printStackTrace();
        }
    }

    public static void getServiceSearch(String AGCD, String SVCCD) {
        System.out.println("GET EVERYTHING");
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

            jBObj_send2.put("AGCD", AGCD);
            jBObj_send2.put("SVCCD", SVCCD);

            jTObj_send2.put("header", jHObj_send2); // {"header":{"TYPE":"01"},"body":[{}]}
            jTObj_send2.put("body", jBObj_send2);

            sJson = jTObj_send2.toString();
            System.out.println("서비스 요청 메시지 (SEARCH EVERYTHING) : " + sJson);

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
            serviceSearchData = stringBuffer2.toString();

            System.out.println("받은 데이터 (서비스) 조회) : " + serviceSearchData);

            // 닫기
            osw.close();
            br.close();
        } catch(Exception ex) {
            System.out.println(ex.toString());
        }

        try {
            // 응답 JSON 파싱
            JSONObject receiveJSONObject = new JSONObject(serviceSearchData);
            // header : Array
            JSONArray jsonArray1 = receiveJSONObject.getJSONArray("header");
            // body : Array
            JSONArray jsonArray2 = receiveJSONObject.getJSONArray("body");

            if (jsonArray2.length() == 0) { // 서비스가 없을 경우
                dataList.clear();
            }
            else {
                dataList.clear();
                for (int i = 0; i < jsonArray2.length(); i++) {
                    if (jsonArray2.getJSONObject(i).getString("SVCCD").equals(SVCCD)) { // 서비스 필터링
                        String forParsingDate, forParsingTime, parse_year, parse_month, parse_day, parse_hour, parse_min, parse_sec;

                        String date, time;
                        if (jsonArray2.getJSONObject(i).getString("UPDDT").equals("") || jsonArray2.getJSONObject(i).getString("UPDTM").equals("")) {
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
                        dataList.add(new ServiceItem(date, time, jsonArray2.getJSONObject(i).getString("AGNM"), jsonArray2.getJSONObject(i).getString("SVCNM"), jsonArray2.getJSONObject(i).getString("RESULT")));
                    }
                    else if (SVCCD.equals("")){ // 전체 보기
                        String forParsingDate, forParsingTime, parse_year, parse_month, parse_day, parse_hour, parse_min, parse_sec;

                        String date, time;
                        if (jsonArray2.getJSONObject(i).getString("UPDDT").equals("") || jsonArray2.getJSONObject(i).getString("UPDTM") == "") {
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
                        if (jsonArray2.getJSONObject(i).getString("UPDDT").equals("") || jsonArray2.getJSONObject(i).getString("UPDTM").equals("")) {
                            date = "정보 없음";
                            time = "정보 없음";
                        }
                        dataList.add(new ServiceItem(date, time, jsonArray2.getJSONObject(i).getString("AGNM"), jsonArray2.getJSONObject(i).getString("SVCNM"), jsonArray2.getJSONObject(i).getString("RESULT")));
                    }
                }
            }
        } catch (JSONException e) {
            System.out.println(e);
            e.printStackTrace();
        }
    }
}