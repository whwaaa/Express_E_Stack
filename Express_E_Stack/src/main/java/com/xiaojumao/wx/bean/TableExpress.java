package com.xiaojumao.wx.bean;

import java.sql.Timestamp;

/**
 * @Author: whw
 * @Description:
 * @Date Created in 2021-07-04 23:17
 * @Modified By:
 */
public class TableExpress {
    private String code;
    private String company;
    private String number;
    private String sysPhone;
    private String strInTime;
    private String strOutTime;
    private Timestamp inTime;
    private Timestamp outTime;
    private String status;

    public TableExpress(String code, String company, String number, String sysPhone, String strInTime, String strOutTime, Timestamp inTime, Timestamp outTime, String status) {
        this.code = code;
        this.company = company;
        this.number = number;
        this.sysPhone = sysPhone;
        this.strInTime = strInTime;
        this.strOutTime = strOutTime;
        this.inTime = inTime;
        this.outTime = outTime;
        this.status = status;
    }

    public Timestamp getInTime() {
        return inTime;
    }

    public void setInTime(Timestamp inTime) {
        this.inTime = inTime;
    }

    public Timestamp getOutTime() {
        return outTime;
    }

    public void setOutTime(Timestamp outTime) {
        this.outTime = outTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public TableExpress() {
    }

    public String getStrInTime() {
        return strInTime;
    }

    public void setStrInTime(String strInTime) {
        this.strInTime = strInTime;
    }

    public String getStrOutTime() {
        return strOutTime;
    }

    public void setStrOutTime(String strOutTime) {
        this.strOutTime = strOutTime;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getSysPhone() {
        return sysPhone;
    }

    public void setSysPhone(String sysPhone) {
        this.sysPhone = sysPhone;
    }
}
