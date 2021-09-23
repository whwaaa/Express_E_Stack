package com.xiaojumao.bean;

import java.sql.Timestamp;
import java.util.Objects;

/**
 * @Author: whw
 * @Description:
 * @Date Created in 2021-07-01 11:52
 * @Modified By:
 */
public class Express {
    private Integer id;
    private String number;
    private String userName;
    private String userPhone;
    private String company;
    private String code;
    private Timestamp inTime;
    private Timestamp outTime;
    private String strInTime;
    private String strOutTime;
    private Integer status;
    private String sysPhone;
    private String email;

    @Override
    public String toString() {
        return "Express{" +
                "id=" + id +
                ", number='" + number + '\'' +
                ", userName='" + userName + '\'' +
                ", userPhone='" + userPhone + '\'' +
                ", company='" + company + '\'' +
                ", code='" + code + '\'' +
                ", inTime=" + inTime +
                ", outTime=" + outTime +
                ", strInTime='" + strInTime + '\'' +
                ", strOutTime='" + strOutTime + '\'' +
                ", status=" + status +
                ", sysPhone='" + sysPhone + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Express express = (Express) o;
        return Objects.equals(id, express.id) && Objects.equals(email, express.email) && Objects.equals(number, express.number) && Objects.equals(userName, express.userName) && Objects.equals(userPhone, express.userPhone) && Objects.equals(company, express.company) && Objects.equals(code, express.code) && Objects.equals(inTime, express.inTime) && Objects.equals(outTime, express.outTime) && Objects.equals(strInTime, express.strInTime) && Objects.equals(strOutTime, express.strOutTime) && Objects.equals(status, express.status) && Objects.equals(sysPhone, express.sysPhone);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, number, userName, userPhone, company, code, inTime, outTime, strInTime, strOutTime, status, sysPhone, email);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getSysPhone() {
        return sysPhone;
    }

    public void setSysPhone(String sysPhone) {
        this.sysPhone = sysPhone;
    }

    public Express() {
    }

    public Express(String number, String company, String userName, String userPhone, Integer status, String email) {
        this.number = number;
        this.company = company;
        this.userName = userName;
        this.userPhone = userPhone;
        this.status = status;
        this.email = email;
    }


    public Express(String number, String userName, String userPhone, String company, Timestamp inTime, Integer status, String sysPhone, String email) {
        this.number = number;
        this.userName = userName;
        this.userPhone = userPhone;
        this.company = company;
        this.inTime = inTime;
        this.status = status;
        this.sysPhone = sysPhone;
        this.email = email;

    }

    public Express(Integer id, String number, String userName, String userPhone, String company, String code, Timestamp inTime, Timestamp outTime, Integer status, String sysPhone, String email) {
        this.id = id;
        this.number = number;
        this.userName = userName;
        this.userPhone = userPhone;
        this.company = company;
        this.code = code;
        this.inTime = inTime;
        this.outTime = outTime;
        this.status = status;
        this.sysPhone = sysPhone;
        this.email = email;
    }
}
