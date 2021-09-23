package com.xiaojumao.dao.imp;
import com.xiaojumao.dao.BaseAdminDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;

@Repository
public class AdminDaoMysql extends JdbcDaoSupport implements BaseAdminDao {

    @Autowired
    private void setMyJdbcTemplate(JdbcTemplate jdbcTemplate){
        super.setJdbcTemplate(jdbcTemplate);
    }


    // 根据用户名修改登录时间和登录ip
    private static final String UPDATE_LOGINTIME_IP = "UPDATE EADMIN SET LOGINTIME=?,LOGINIP=? WHERE USERNAME=?";
    // 管理员根据账号密码登录
    private static final String QUERY_USERNAME_PASSWORD = "SELECT COUNT(`id`) FROM `eadmin` WHERE `username`=? AND `password`=?";


    /**
     * 根据用户名修改登录时间和登录ip
     *
     * @param username 用户名
     * @param date     时间
     * @param ip       ip地址
     */
    @Override
    public Integer updateLoginTimeAndIp(String username, Timestamp date, String ip) {
        return this.getJdbcTemplate().update(UPDATE_LOGINTIME_IP, date, ip, username);
    }

    /**
     * 管理员根据账号密码登录
     *
     * @param username 用户名
     * @param password 密码
     * @return 登陆成功返回true, 失败返回false
     */
    @Override
    public boolean login(String username, String password) {
        Integer integer = this.getJdbcTemplate().queryForObject(QUERY_USERNAME_PASSWORD, new Object[]{username, password}, Integer.class);
        if(integer > 0)
            return true;
        return false;
    }
}
