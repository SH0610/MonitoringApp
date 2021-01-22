package com.example.monitoringapp.ErrorCatch.ErrorMain;

public class ErrorItem {
//    private String agcd; // 대리점코드
    private String agnm; // 대리점명
    private String svccd; // 서비스코드
    private String svcnm; // 서비스명
    private String seq; // seq
    private String ecd; // 에러코드
    private String err_msg; // 에러메시지
    private String errdt; // 에러발생일자
    private String errtm; // 에러발생시간
    private String comfg; // 처리여부 (1 : 처리완료, 0 : 미완료)
    private String com_msg; // 처리내용
    private String entdt; // 등록일자
    private String enttm; // 등록시간
    private String reportdt; // 최근보고일자
    private String reporttm; // 최근보고시간
    private String upddt; // 수정일자 (처리일자)
    private String updtm; // 수정시간 (처리시간)

    public ErrorItem(String agnm, String svccd, String svcnm, String seq, String ecd, String err_msg, String errdt, String errtm, String comfg, String com_msg, String entdt, String enttm, String reportdt, String reporttm, String upddt, String updtm) {
        this.agnm = agnm;
        this.svccd = svccd;
        this.svcnm = svcnm;
        this.seq = seq;
        this.ecd = ecd;
        this.err_msg = err_msg;
        this.errdt = errdt;
        this.errtm = errtm;
        this.comfg = comfg;
        this.com_msg = com_msg;
        this.entdt = entdt;
        this.enttm = enttm;
        this.reportdt = reportdt;
        this.reporttm = reporttm;
        this.upddt = upddt;
        this.updtm = updtm;
    }

    public String getAgnm() {
        return agnm;
    }

    public void setAgnm(String agnm) {
        this.agnm = agnm;
    }

    public String getSvccd() {
        return svccd;
    }

    public void setSvccd(String svccd) {
        this.svccd = svccd;
    }

    public String getSvcnm() {
        return svcnm;
    }

    public void setSvcnm(String svcnm) {
        this.svcnm = svcnm;
    }

    public String getSeq() {
        return seq;
    }

    public void setSeq(String seq) {
        this.seq = seq;
    }

    public String getEcd() {
        return ecd;
    }

    public void setEcd(String ecd) {
        this.ecd = ecd;
    }

    public String getErr_msg() {
        return err_msg;
    }

    public void setErr_msg(String err_msg) {
        this.err_msg = err_msg;
    }

    public String getErrdt() {
        return errdt;
    }

    public void setErrdt(String errdt) {
        this.errdt = errdt;
    }

    public String getErrtm() {
        return errtm;
    }

    public void setErrtm(String errtm) {
        this.errtm = errtm;
    }

    public String getComfg() {
        return comfg;
    }

    public void setComfg(String comfg) {
        this.comfg = comfg;
    }

    public String getCom_msg() {
        return com_msg;
    }

    public void setCom_msg(String com_msg) {
        this.com_msg = com_msg;
    }

    public String getEntdt() {
        return entdt;
    }

    public void setEntdt(String entdt) {
        this.entdt = entdt;
    }

    public String getEnttm() {
        return enttm;
    }

    public void setEnttm(String enttm) {
        this.enttm = enttm;
    }

    public String getReportdt() {
        return reportdt;
    }

    public void setReportdt(String reportdt) {
        this.reportdt = reportdt;
    }

    public String getReporttm() {
        return reporttm;
    }

    public void setReporttm(String reporttm) {
        this.reporttm = reporttm;
    }

    public String getUpddt() {
        return upddt;
    }

    public void setUpddt(String upddt) {
        this.upddt = upddt;
    }

    public String getUpdtm() {
        return updtm;
    }

    public void setUpdtm(String updtm) {
        this.updtm = updtm;
    }
}
