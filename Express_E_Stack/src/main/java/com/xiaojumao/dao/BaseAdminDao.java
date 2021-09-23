package com.xiaojumao.dao;

import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Timestamp;

/**
 * @Author: whw
 * @Description: 用于定义eadmin表格的规范
 * @Date Created in 2021-06-30 22:42
 * @Modified By:
 */
public interface BaseAdminDao {

    /**
     * 根据用户名修改登录时间和登录ip
     * @param username 用户名
     * @param date 时间
     * @param ip ip地址
     */
    public Integer updateLoginTimeAndIp(String username, Timestamp date, String ip);

    /**
     * 管理员根据账号密码登录
     * @param username 用户名
     * @param password 密码
     * @return 登陆成功返回true,失败返回false
     */
    public boolean login(String username, String password);

}
