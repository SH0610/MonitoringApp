package com.example.monitoringapp.ServiceExecution;

public class ServiceItem {
    // 4. 아이템 정의
    // 데이터 클래스 생성
    private String date;
    private String time;
    private String account;
    private String service;
    private String status;

    public ServiceItem(String date, String time, String account, String service, String status) {
        this.date = date;
        this.time = time;
        this.account = account;
        this.service = service;
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
