package com.xiaojumao.controller;

import com.xiaojumao.bean.Result;
import com.xiaojumao.service.AdminService;
import com.xiaojumao.util.DateFormatUtil;
import com.xiaojumao.util.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * @Author: whw
 * @Description:
 * @Date Created in 2021-06-30 23:41
 * @Modified By:
 */
@Controller
@RequestMapping("admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @ResponseBody
    @RequestMapping(value = "login.do")
    public Result login(HttpServletRequest request){
        // 1.接收参数
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        // 2.调用Service层方法
        boolean login = adminService.login(username, password);
        // 3.根据结果返回数据
        if(login){
            // 登陆成功 ->
            // 1.登录信息存入redis
            String token = TokenUtils.initToken();
            boolean b = TokenUtils.setAdminName(token, username);
            if(token!=null && b){
                // 2.修改登录时间和ip
                Date date = new Date();
                String remoteAddr = request.getRemoteAddr();
                adminService.updateLoginTimeAndIp(username, DateFormatUtil.dateFormatToTimestamp(date), remoteAddr);
//             TODO : 暂时设置管理员的录入人手机号为固定值
//            GetSession.setSysPhone(request, "18888888888");
                // 封装数据
                return new Result(200, "登陆成功", token);
            }
        }
        // 失败
        return new Result<>(401, "用户名或密码错误", null);
    }
}
