package com.xiaojumao.bean;

/**
 * @Author: whw
 * @Description:
 * @Date Created in 2021-07-03 15:39
 * @Modified By:
 */
public class BootStrapTableUser {
    private String id;
    private String userName;
    private String userPhone;
    private String password;
    private String idCard;
    private String strRegTime;
    private String strLastTime;
    private String email;

    public BootStrapTableUser() {
    }

    public BootStrapTableUser(String id, String userName, String userPhone, String password, String idCard, String strRegTime, String strLastTime, String email) {
        this.id = id;
        this.userName = userName;
        this.userPhone = userPhone;
        this.password = password;
        this.idCard = idCard;
        this.strRegTime = strRegTime;
        this.strLastTime = strLastTime;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
