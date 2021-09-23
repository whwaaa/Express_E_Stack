package com.xiaojumao.bean;

import java.sql.Timestamp;
import java.util.Objects;

/**
 * @Author: whw
 * @Description:
 * @Date Created in 2021-07-02 20:15
 * @Modified By:
 */
public class Courier {
    private Integer id;
    private String cName;
    private String cPhone;
    private String password;
    private String sendNum;
    private String idCard;
    private Timestamp regTime;
    private Timestamp lastTime;
    private String lastIp;
    private String strRegTime;
    private String strLastTime;
    private String email;

    @Override
    public String toString() {
        return "Courier{" +
                "id=" + id +
                ", cName='" + cName + '\'' +
                ", cPhone='" + cPhone + '\'' +
                ", password='" + password + '\'' +
                ", sendNum='" + sendNum + '\'' +
                ", idCard='" + idCard + '\'' +
                ", regTime=" + regTime +
                ", lastTime=" + lastTime +
                ", lastIp='" + lastIp + '\'' +
                ", strRegTime='" + strRegTime + '\'' +
                ", strLastTime='" + strLastTime + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Courier courier = (Courier) o;
        return Objects.equals(id, courier.id) && Objects.equals(cName, courier.cName) && Objects.equals(cPhone, courier.cPhone) && Objects.equals(password, courier.password) && Objects.equals(sendNum, courier.sendNum) && Objects.equals(idCard, courier.idCard) && Objects.equals(regTime, courier.regTime) && Objects.equals(lastTime, courier.lastTime) && Objects.equals(lastIp, courier.lastIp) && Objects.equals(strRegTime, courier.strRegTime) && Objects.equals(strLastTime, courier.strLastTime) && Objects.equals(email, courier.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, cName, cPhone, password, sendNum, idCard, regTime, lastTime, lastIp, strRegTime, strLastTime, email);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getcName() {
        return cName;
    }

    public void setcName(String cName) {
        this.cName = cName;
    }

    public String getcPhone() {
        return cPhone;
    }

    public void setcPhone(String cPhone) {
        this.cPhone = cPhone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSendNum() {
        return sendNum;
    }

    public void setSendNum(String sendNum) {
        this.sendNum = sendNum;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public Timestamp getRegTime() {
        return regTime;
    }

    public void setRegTime(Timestamp regTime) {
        this.regTime = regTime;
    }

    public Timestamp getLastTime() {
        return lastTime;
    }

    public void setLastTime(Timestamp lastTime) {
        this.lastTime = lastTime;
    }

    public String getLastIp() {
        return lastIp;
    }

    public void setLastIp(String lastIp) {
        this.lastIp = lastIp;
    }

    public String getStrRegTime() {
        return strRegTime;
    }

    public void setStrRegTime(String strRegTime) {
        this.strRegTime = strRegTime;
    }

    public String getStrLastTime() {
        return strLastTime;
    }

    public void setStrLastTime(String strLastTime) {
        this.strLastTime = strLastTime;
    }

    public Courier() {
    }

    public Courier(String cName, String cPhone, String password, String idCard, String lastIp, String email) {
        this.cName = cName;
        this.cPhone = cPhone;
        this.password = password;
        this.idCard = idCard;
        this.lastIp = lastIp;
        this.email = email;
    }

    public Courier(Integer id, String cName, String cPhone, String password, String sendNum, String idCard, Timestamp regTime, Timestamp lastTime, String lastIp, String strRegTime, String strLastTime, String email) {
        this.id = id;
        this.cName = cName;
        this.cPhone = cPhone;
        this.password = password;
        this.sendNum = sendNum;
        this.idCard = idCard;
        this.regTime = regTime;
        this.lastTime = lastTime;
        this.lastIp = lastIp;
        this.strRegTime = strRegTime;
        this.strLastTime = strLastTime;
        this.email = email;
    }
}
