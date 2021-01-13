package com.example.monitoringapp.Login;

public class LoginInfoItem {
    private String bid;
    private String bnm;
//    private String department;
//    private String status;
//    private String mobile;
//    private String bauty;


    public LoginInfoItem(String bid, String bnm) {
        this.bid = bid;
        this.bnm = bnm;
    }

    public String getBid() {
        return bid;
    }

    public void setBid(String bid) {
        this.bid = bid;
    }

    public String getBnm() {
        return bnm;
    }

    public void setBnm(String bnm) {
        this.bnm = bnm;
    }
}
