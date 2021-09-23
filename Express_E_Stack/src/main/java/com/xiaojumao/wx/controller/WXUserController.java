package com.xiaojumao.wx.controller;

import com.xiaojumao.bean.Courier;
import com.xiaojumao.bean.Message;
import com.xiaojumao.bean.Result;
import com.xiaojumao.exception.*;
import com.xiaojumao.service.CourierService;
import com.xiaojumao.service.UserService;
import com.xiaojumao.util.*;
import com.xiaojumao.wx.bean.User;
import com.xiaojumao.wx.util.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.util.Date;

/**
 * @Author: whw
 * @Description:
 * @Date Created in 2021-07-04 15:38
 * @Modified By:
 */
@Controller
@RequestMapping("wx")
public class WXUserController {

    @Autowired
    private UserService userService;

    @Autowired
    private CourierService courierService;

//    @ResponseBody
//    @RequestMapping(value = "setEmail.do", method = RequestMethod.GET)
//    public Result setEmail(HttpServletRequest req){
//        String email = req.getParameter("email");
//        String token = req.getParameter("token");
//        // email存入redis
//        User user = new User(null, null, email, null);
//        TokenUtils.setUser(token, user);
//        return new Result(200, "登陆成功", null);
//    }

    @ResponseBody
    @RequestMapping(value = "login", method = RequestMethod.POST)
    public Result loginByPassword(HttpServletRequest req){
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        com.xiaojumao.bean.User user = userService.checkEmailAndPassword(email, password);
        Courier courier = courierService.checkEmailAndPassword(email, password);
        if(user==null && courier==null)
            // 登录失败
            return new Result(400, "邮箱或密码错误", null);
        // 登录成功 -> 用户或快递员信息存入redis
        String token = TokenUtils.initToken();
        User user1 = new User();
        user1.setEmail(email);
        if(TokenUtils.setUser(token, user1))
            return new Result(200, "登陆成功", token);
        return new Result(500, "用户或快递员信息存入redis失败", null);
    }

    @ResponseBody
    @RequestMapping(value = "sendVerCode.do")
    public Result sendVerCode(HttpServletRequest req){
        // 获取参数
        String email = req.getParameter("email");
        // 生成token
        String token = TokenUtils.initToken();
        // 生成验证码
        String varCode = TokenUtils.initVerCode(token);
        // 发送验证码
        EmailSend.sendLoginVer(email,varCode);
        // 回复前端
        return new Result(200, "发送成功", token);
    }

    @ResponseBody
    @RequestMapping(value = "binding.do")
    public Result binding(HttpServletRequest req, HttpServletResponse resp){
        String token = req.getParameter("token");
        // 获取参数
        String phone = req.getParameter("phone");
        String email = req.getParameter("email");
        String code = req.getParameter("code");
//        String sysCode = GetSession.getVerCode(req, email);

        if( !TokenUtils.verVerCode(token, code) ){
//            // 邮箱还未接收验证码
//            return new Result(-2, "邮箱还未接收验证码", null);
//        }else if(!sysCode.equals(code)){
//            // 验证码错误
            return new Result(-1, "验证码错误", null);
        }
        // 验证成功
        // 查询数据库, 若邮箱不存在 -> 注册
        Courier courier1 = courierService.findByEmail(email);
        com.xiaojumao.bean.User user1 = userService.findByEmail(email);
        if(courier1==null && user1==null){
            // 账号不存在 -> 注册新用户
            // 存入数据库
            com.xiaojumao.bean.User regUser = new com.xiaojumao.bean.User();
            regUser.setEmail(email);
            regUser.setUserPhone(phone);
            regUser.setRegTime(DateFormatUtil.dateFormatToTimestamp(new Date()));
            regUser.setLastTime(DateFormatUtil.dateFormatToTimestamp(new Date()));
            regUser.setLastIp(req.getRemoteAddr());
            try {
                userService.add(regUser);
            } catch (DuplicateUserNameException e) {
                e.printStackTrace();
            } catch (DuplicateUserPhoneException e) {
                return new Result(-3, "手机号重复", null);
            } catch (DuplicateEmailException e) {
                e.printStackTrace();
            }
        }
        // 用户或快递员信息存入session
        User user = new User();
        user.setPhone(phone);
        user.setEmail(email);
//        GetSession.setUser(req, user);
        TokenUtils.setUser(token, user);
        return new Result();
    }

    @ResponseBody
    @RequestMapping(value = "getRole.do", method = RequestMethod.GET)
    public Result getRole(HttpServletRequest req){
        String token = req.getParameter("token");
        String ip = req.getRemoteAddr();
        Timestamp lastTime = DateFormatUtil.dateFormatToTimestamp(new Date());
        User user = TokenUtils.getUser(token);
        if(user == null)
            // 非正常进入页面
            return new Result(401, "请先登录", null);
        // 查询数据库判断用户身份
        Courier courier = courierService.findByEmail(user.getEmail());
        com.xiaojumao.bean.User user1 = userService.findByEmail(user.getEmail());
        if(courier != null){
            // 快递员身份登录 -> 快递员信息存入session
//            GetSession.setUserName(req, courier.getcName());
            TokenUtils.setUserName(token,courier.getcName());
            user.setUser(false);
            user.setName(courier.getcName());
//            GetSession.setUser(req, user);
            TokenUtils.setUser(token, user);
            //设置快递员录入人手机号
//            GetSession.setSysPhone(req, courier.getcPhone());
            TokenUtils.setSysPhone(token, courier.getcPhone());
            // 更新登录ip / 登录时间
            courier.setLastIp(ip);
            courier.setLastTime(lastTime);
            try {
                courierService.update(courier.getId(),courier);
            } catch (Exception e) {
                // 手机号/邮箱不会重复
            }
            return new Result(1, "快递员登陆", null);
        }else{  // (user1 != null)
            // 用户身份登录 -> 用户身份存入session
//            GetSession.setUserName(req, user1.getUserName());

            user.setUser(true);
            user.setName(user1.getUserName());
//            GetSession.setUser(req, user);
            TokenUtils.setUser(token, user);
            // 更新登录ip / 登录时间
            user1.setLastIp(ip);
            user1.setLastTime(lastTime);
            try {
                userService.update(user1.getId(), user1);
            } catch (Exception e) {
                // 手机号/邮箱不会重复
            }
            return new Result(0, "用户登录", null);
        }

    }

    @ResponseBody
    @RequestMapping(value = "InfoModifySendCode.do", method = RequestMethod.POST)
    public Result InfoModifySendCode(HttpServletRequest req){
        String token = req.getParameter("token");
        // 获取邮箱
        String email = req.getParameter("email");
        // redis中生成验证码
        String varCode = TokenUtils.initVerCode(token);
        // 发送验证码
        EmailSend.sendUpdateVer(email, varCode);
        return new Result(200, "发送成功", null);
    }

    @ResponseBody
    @RequestMapping(value = "InfoModifyUpdate.do", method = RequestMethod.PUT)
    public Result InfoModifyUpdate(HttpServletRequest req){
        String token = req.getParameter("token");
        String password = req.getParameter("password");
        String username = req.getParameter("username");
        String email = req.getParameter("email");
        String phone = req.getParameter("phone");
        String code = req.getParameter("code");
        if(!TokenUtils.verVerCode(token, code))
            return new Result(400, "验证码错误", null);
        // 获取当前用户信息
        User oldUser = TokenUtils.getUser(token);
        if(oldUser.isUser()){
            com.xiaojumao.bean.User user = userService.findByEmail(oldUser.getEmail());
            // 修改信息
            user.setUserName(username);
            user.setEmail(email);
            user.setUserPhone(phone);
            user.setPassword(password);
            try {
                userService.update(oldUser.getEmail(), user);
            } catch (DuplicateUserNameException e) {
                // 用户名已取消唯一索引,不会产生异常
            } catch (DuplicateUserPhoneException e) {
                // 手机号重复
                return new Result(400, "手机号重复", null);
            } catch (DuplicateEmailException e) {
                // 邮箱重复
                return new Result(400, "邮箱重复", null);
            }
        }else{
            // 修改快递员
            Courier courier = courierService.findByEmail(oldUser.getEmail());
            courier.setcName(username);
            courier.setEmail(email);
            courier.setcPhone(phone);
            courier.setPassword(password);
            try {
                courierService.update(oldUser.getEmail(), courier);
            } catch (DuplicateCNameException e) {
                // 用户名已取消唯一索引,不会产生异常
            } catch (DuplicateCPhoneException e) {
                // 手机号重复
                return new Result(400, "手机号重复", null);
            } catch (DuplicateEmailException e) {
                // 邮箱重复
                return new Result(400, "邮箱重复", null);
            }
        }
        // 完善信息存入session
        oldUser.setName(username);
        oldUser.setPhone(phone);
        oldUser.setPassword(password);
        oldUser.setEmail(email);
        TokenUtils.setUser(token, oldUser);
        return new Result(200, "修改成功", null);
    }


    @ResponseBody
    @RequestMapping(value = "wxIdCardUserInfoModifyInit.do")
    public Result wxIdCardUserInfoModifyInit(HttpServletRequest req){
        String token = req.getParameter("token");
        // 返回用户姓名
        return new Result(200, "ok", TokenUtils.getUser(token));
    }

    @ResponseBody
    @RequestMapping(value = "userInfoIsComplete.do")
    public Result userInfoIsComplete(HttpServletRequest req){
        String token = req.getParameter("token");
        // session中获取用户email
//        User user = GetSession.getUser(req);
        User user = TokenUtils.getUser(token);
        String email = user.getEmail();
        // 数据库查询user信息 -> 存入session
        if(user.isUser()){
            com.xiaojumao.bean.User u = userService.findByEmail(email);
            user.setPhone(u.getUserPhone());
            user.setName(u.getUserName());
            user.setPassword(u.getPassword());
        }else{
            Courier c = courierService.findByEmail(email);
            user.setPhone(c.getcPhone());
            user.setName(c.getcName());
            user.setPassword(c.getPassword());
            // 快递员 -> 设置录入人手机号
//            GetSession.setSysPhone(req, c.getcPhone());
            TokenUtils.setSysPhone(token, c.getcPhone());
        }
        // 返回数据
        return new Result(200, "ok", user);
    }

    @ResponseBody
    @RequestMapping(value = "logout.do", method = RequestMethod.POST)
    public Result logout(HttpServletRequest req){
        String token = req.getParameter("token");
        // 销毁redis
        TokenUtils.destroy(token);
        // 给前端回复消息
        return new Result(200, "退出成功", null);
    }
}

















