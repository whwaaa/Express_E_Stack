package com.xiaojumao.wx.bean;

/**
 * @Author: whw
 * @Description:
 * @Date Created in 2021-07-04 16:01
 * @Modified By:
 */
public class User {
    private String name;
    private String phone;
    private String email;
    private String password;
    private String code;
    private boolean isUser;
    private String QRCOde;

    public User(String name, String phone, String email, String password) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.password = password;
    }

    public User() {
    }

    public String getQRCOde() {
        return QRCOde;
    }

    public void setQRCOde(String QRCOde) {
        this.QRCOde = QRCOde;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isUser() {
        return isUser;
    }

    public void setUser(boolean user) {
        isUser = user;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
