package com.example.monitoringapp.ServiceExecution.Model;

public class ServiceSearchResponse {
    private String AGCD; // 대리점코드
    private String AGNM; // 대리점이름
    private String SVCCD; // 서비스코드
    private String SVCNM; // 서비스명
    private String RESULT; // 결과
    private String UPDDT; // 수정일자
    private String UPDTM; // 수정시간

    public String getAGCD() {
        return AGCD;
    }

    public void setAGCD(String AGCD) {
        this.AGCD = AGCD;
    }

    public String getAGNM() {
        return AGNM;
    }

    public void setAGNM(String AGNM) {
        this.AGNM = AGNM;
    }

    public String getSVCCD() {
        return SVCCD;
    }

    public void setSVCCD(String SVCCD) {
        this.SVCCD = SVCCD;
    }

    public String getSVCNM() {
        return SVCNM;
    }

    public void setSVCNM(String SVCNM) {
        this.SVCNM = SVCNM;
    }

    public String getRESULT() {
        return RESULT;
    }

    public void setRESULT(String RESULT) {
        this.RESULT = RESULT;
    }

    public String getUPDDT() {
        return UPDDT;
    }

    public void setUPDDT(String UPDDT) {
        this.UPDDT = UPDDT;
    }

    public String getUPDTM() {
        return UPDTM;
    }

    public void setUPDTM(String UPDTM) {
        this.UPDTM = UPDTM;
    }
}
