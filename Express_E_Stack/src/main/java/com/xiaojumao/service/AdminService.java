package com.xiaojumao.service;

import com.xiaojumao.dao.imp.AdminDaoMysql;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;

/**
 * @Author: whw
 * @Description:
 * @Date Created in 2021-06-30 23:31
 * @Modified By:
 */
@Service
public class AdminService implements IAdminService{
    @Autowired(required = false)
    private AdminDaoMysql dao;

    /**
     * 更新登录时间与ip
     * @param username 用户名
     * @param date 时间
     * @param ip ip
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public void updateLoginTimeAndIp(String username, Timestamp date, String ip){
        dao.updateLoginTimeAndIp(username, date, ip);
    }

    /**
     * 根据账号密码登录
     * @param username 用户名
     * @param password 密码
     * @return 登录成功,失败返回false
     */
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public boolean login(String username, String password){
        return dao.login(username, password);
    }
}
