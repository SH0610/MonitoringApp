package com.example.monitoringapp.Scheduler;

public class SchedulerItem {
    private String real_date; // 실제실행일자
    private String real_time; // 실제실행시간
    private String pre_date; // 예상실행일자
    private String pre_time; // 예상실행시간
    private String account; // 대리점이름
    private String service; // 서비스명
    private String status; // 처리여부

    public SchedulerItem(String real_date, String real_time, String pre_date, String pre_time, String account, String service, String status) {
        this.real_date = real_date;
        this.real_time = real_time;
        this.pre_date = pre_date;
        this.pre_time = pre_time;
        this.account = account;
        this.service = service;
        this.status = status;
    }

    public String getReal_date() {
        return real_date;
    }

    public void setReal_date(String real_date) {
        this.real_date = real_date;
    }

    public String getReal_time() {
        return real_time;
    }

    public void setReal_time(String real_time) {
        this.real_time = real_time;
    }

    public String getPre_date() {
        return pre_date;
    }

    public void setPre_date(String pre_date) {
        this.pre_date = pre_date;
    }

    public String getPre_time() {
        return pre_time;
    }

    public void setPre_time(String pre_time) {
        this.pre_time = pre_time;
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
