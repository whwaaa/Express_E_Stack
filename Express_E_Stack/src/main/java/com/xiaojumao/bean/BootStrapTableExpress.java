package com.xiaojumao.bean;

/**
 * @Author: whw
 * @Description:
 * @Date Created in 2021-07-03 9:56
 * @Modified By:
 */
public class BootStrapTableExpress {
    private String id;
    private String number;
    private String userName;
    private String userPhone;
    private String company;
    private String code;
    private String strInTime;
    private String strOutTime;
    private String status;
    private String sysPhone;
    private String email;

    public BootStrapTableExpress() {
    }

    public BootStrapTableExpress(String id, String number, String userName, String userPhone, String company, String code, String strInTime, String strOutTime, String status, String sysPhone, String email) {
        this.id = id;
        this.number = number;
        this.userName = userName;
        this.userPhone = userPhone;
        this.company = company;
        this.code = code;
        this.strInTime = strInTime;
        this.strOutTime = strOutTime;
        this.status = status;
        this.sysPhone = sysPhone;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSysPhone() {
        return sysPhone;
    }

    public void setSysPhone(String sysPhone) {
        this.sysPhone = sysPhone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
