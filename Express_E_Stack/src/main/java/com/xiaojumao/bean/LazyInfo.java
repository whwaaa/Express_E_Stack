package com.xiaojumao.bean;

/**
 * @Author: whw
 * @Description:
 * @Date Created in 2021-07-05 13:46
 * @Modified By:
 */
public class LazyInfo {
    private String username;
    private Integer num;

    public LazyInfo() {
    }

    @Override
    public String toString() {
        return "LazyInfo{" +
                "username='" + username + '\'' +
                ", num=" + num +
                '}';
    }

    public LazyInfo(String username, Integer num) {
        this.username = username;
        this.num = num;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }
}
