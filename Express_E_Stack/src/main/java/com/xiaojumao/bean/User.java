package com.xiaojumao.bean;

import java.sql.Timestamp;
import java.util.Objects;

/**
 * @Author: whw
 * @Description:
 * @Date Created in 2021-07-02 17:10
 * @Modified By:
 */
public class User {
    private Integer id;
    private String userName;
    private String userPhone;
    private String password;
    private String idCard;
    private Timestamp regTime;
    private Timestamp lastTime;
    private String lastIp;
    private String strRegTime;
    private String strLastTime;
    private String email;

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", userPhone='" + userPhone + '\'' +
                ", password='" + password + '\'' +
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
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(userName, user.userName) && Objects.equals(userPhone, user.userPhone) && Objects.equals(password, user.password) && Objects.equals(idCard, user.idCard) && Objects.equals(regTime, user.regTime) && Objects.equals(lastTime, user.lastTime) && Objects.equals(lastIp, user.lastIp) && Objects.equals(strRegTime, user.strRegTime) && Objects.equals(strLastTime, user.strLastTime) && Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userName, userPhone, password, idCard, regTime, lastTime, lastIp, strRegTime, strLastTime, email);
    }

    public User(String userName, String userPhone, String password, String idCard, Timestamp regTime, Timestamp lastTime, String lastIp, String email) {
        this.userName = userName;
        this.userPhone = userPhone;
        this.password = password;
        this.idCard = idCard;
        this.regTime = regTime;
        this.lastTime = lastTime;
        this.lastIp = lastIp;
        this.email = email;
    }

    public User(Integer id, String userName, String userPhone, String password, String idCard, Timestamp regTime, Timestamp lastTime, String lastIp, String strRegTime, String strLastTime, String email) {
        this.id = id;
        this.userName = userName;
        this.userPhone = userPhone;
        this.password = password;
        this.regTime = regTime;
        this.lastTime = lastTime;
        this.lastIp = lastIp;
        this.strRegTime = strRegTime;
        this.strLastTime = strLastTime;
        this.idCard = idCard;
        this.email = email;
    }

    public User() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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
}
