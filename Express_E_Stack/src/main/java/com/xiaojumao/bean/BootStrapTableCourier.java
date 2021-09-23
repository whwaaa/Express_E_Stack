package com.xiaojumao.bean;

/**
 * @Author: whw
 * @Description:
 * @Date Created in 2021-07-03 14:13
 * @Modified By:
 */
public class BootStrapTableCourier {
    private String id;
    private String cName;
    private String cPhone;
    private String email;
    private String password;
    private String sendNum;
    private String idCard;
    private String strRegTime;
    private String strLastTime;

    public BootStrapTableCourier(String id, String cName, String cPhone, String password, String sendNum, String idCard, String strRegTime, String strLastTime, String email) {
        this.id = id;
        this.cName = cName;
        this.cPhone = cPhone;
        this.password = password;
        this.sendNum = sendNum;
        this.idCard = idCard;
        this.strRegTime = strRegTime;
        this.strLastTime = strLastTime;
        this.email = email;
    }

    public BootStrapTableCourier() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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
}
