package com.xiaojumao.util;

import com.google.gson.Gson;
import com.xiaojumao.service.RedisService;
import com.xiaojumao.wx.bean.User;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.UUID;

/**
 * Express_E_Stack_SSM
 *
 * @author wuhanwei
 * @version 1.0
 * @date 2021/9/22
 */

public class TokenUtils {

    private static RedisService redisService;

    static{
        if(redisService == null){
            ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("application_redis.xml");
            redisService = (RedisService) applicationContext.getBean("redisService");
        }
    }

    /**
     * 生成的token有效期设置半小时,存入hset中
     * @return: 生成成功返回token,失败返回null
     */
    public static String initToken(){
        String token = UUID.randomUUID().toString().replace("-", "");
        if(redisService.hset(token, "token", token, Long.parseLong("1800")))
            return token;
        else
            return null;
    }

    /**
     * 判断是否存在token
     * @param token
     * @return 存在:true 不存在:false
     */
    public static boolean isToken(String token){
        return redisService.hHasKey(token, "token");
    }

    /**
     * 生成验证码存入String中,设置60秒过期
     * @param token
     * @return 生成成功:verCode 生成失败:null
     */
    public static String initVerCode(String token){
        String verCode = RandomUtil.getCode();
        if(redisService.set("verCode_"+token, verCode, Long.parseLong("300")))
            return verCode;
        return null;
    }

    /**
     * 验证验证码是正确和在有效期
     * @param token
     * @param verCode
     * @return 正确且有效:true 不正确或失效:false
     */
    public static boolean verVerCode(String token, String verCode){
        String oldVerCode = (String) redisService.get("verCode_" + token);
        if(verCode.equals(oldVerCode))
            return true;
        return false;
    }


    /**
     * 设置管理员名称
     * @param token
     * @param adminUserName
     * @return
     */
    public static boolean setAdminName(String token, String adminUserName){
        return redisService.hset(token, "adminUserName", adminUserName, Long.parseLong("1800"));
    }

    /**
     * 获取管理员的名称
     * @param token
     * @return
     */
    public static String getAdminName(String token){
        return (String) redisService.hget(token, "adminUserName");
    }

    /**
     * 设置用户名
     * @param token
     * @param username
     */
    public static boolean setUserName(String token, String username){
        return redisService.hset(token, "username", username, Long.parseLong("1800"));
    }

    /**
     * 获取用户名
     * @param token
     * @return
     */
    public static String getUserName(String token){
        return (String) redisService.hget(token, "userName");
    }

    public static boolean setSysPhone(String token, String sysPhone) {
        return redisService.hset(token, "sysPhone", sysPhone, Long.parseLong("1800"));
    }

    public static String getSysPhone(String token) {
        return (String) redisService.hget(token, "sysPhone");
    }

    /**
     * 存入用户信息
     * @param token
     * @param user
     * @return
     */
    public static boolean setUser(String token, User user){
        Gson gson = new Gson();
        String userStr = gson.toJson(user);
        return redisService.hset(token, "user", userStr, Long.parseLong("1800"));
    }

    /**
     * 获取用户信息
     * @param token
     * @return
     */
    public static User getUser(String token){
        Gson gson = new Gson();
        String userStr = (String) redisService.hget(token, "user");
        return gson.fromJson(userStr, User.class);
    }

    public static void destroy(String token){
        redisService.del(token);
    }
}
