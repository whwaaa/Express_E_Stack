package com.xiaojumao.service;

import java.sql.Timestamp;

public interface IAdminService {
    /**
     * 更新登录时间与ip
     * @param username 用户名
     * @param date 时间
     * @param ip ip
     */
    public void updateLoginTimeAndIp(String username, Timestamp date, String ip);

    /**
     * 根据账号密码登录
     * @param username 用户名
     * @param password 密码
     * @return 登录成功,失败返回false
     */
    public boolean login(String username, String password);
}
