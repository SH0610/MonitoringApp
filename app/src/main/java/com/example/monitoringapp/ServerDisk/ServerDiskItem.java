package com.example.monitoringapp.ServerDisk;

public class ServerDiskItem {
    private String ip; // 아이피
    private String remark; // 비고
    private String driveName; // 드라이브명
    private String totalSize; // 총 용량
    private String freeSize; // 사용가능용량
    private String usagePercent; // 사용중비율
    private String date; // 수정일자
    private String time; // 수정시간

    public ServerDiskItem(String ip, String remark, String driveName, String totalSize, String freeSize, String usagePercent, String date, String time) {
        this.ip = ip;
        this.remark = remark;
        this.driveName = driveName;
        this.totalSize = totalSize;
        this.freeSize = freeSize;
        this.usagePercent = usagePercent;
        this.date = date;
        this.time = time;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getDriveName() {
        return driveName;
    }

    public void setDriveName(String driveName) {
        this.driveName = driveName;
    }

    public String getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(String totalSize) {
        this.totalSize = totalSize;
    }

    public String getFreeSize() {
        return freeSize;
    }

    public void setFreeSize(String freeSize) {
        this.freeSize = freeSize;
    }

    public String getUsagePercent() {
        return usagePercent;
    }

    public void setUsagePercent(String usagePercent) {
        this.usagePercent = usagePercent;
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
}
