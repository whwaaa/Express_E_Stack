package com.xiaojumao.controller;

import com.xiaojumao.bean.*;
import com.xiaojumao.exception.DuplicateEmailException;
import com.xiaojumao.exception.DuplicateUserNameException;
import com.xiaojumao.exception.DuplicateUserPhoneException;
import com.xiaojumao.service.UserService;
import com.xiaojumao.util.DateFormatUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author: whw
 * @Description:
 * @Date Created in 2021-07-02 18:22
 * @Modified By:
 */
@Controller
public class UserController {

    @Autowired
    private UserService service;

    @ResponseBody
    @RequestMapping(value = "user/console", method = RequestMethod.GET)
    public Result console(HttpServletRequest req){
        // 调用service层方法
        Map<String, Integer> console = service.console();
        Result<Map<String, Integer>> result = new Result(200, "查询成功", console);
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "user", method = RequestMethod.GET)
    public BootStrapResultData findAll(HttpServletRequest req){
        // 获取参数
        boolean limit = true;
        if("1".equals(req.getParameter("limit")))
            limit = false;
        String offset = req.getParameter("offset");
        String pageSize = req.getParameter("pageNumber");
        // 获取用户信息列表
        List<BootStrapTableUser> userList = new ArrayList<BootStrapTableUser>();
        List<User> all = service.findAll(limit, Integer.parseInt(offset), Integer.parseInt(pageSize));
        for (User u : all) {
            String strRegTime = DateFormatUtil.dateFormatToStr(u.getRegTime());
            String strLastTime = DateFormatUtil.dateFormatToStr(u.getLastTime());
            BootStrapTableUser bootStrapTableUser = new BootStrapTableUser(u.getId().toString(), u.getUserName(), u.getUserPhone(), u.getPassword(), u.getIdCard(), strRegTime, strLastTime, u.getEmail());
            userList.add(bootStrapTableUser);
        }
        // 获取总用户信息条数
        Map<String, Integer> console = service.console();
        // 封装成bootstrap识别的数据格式
        BootStrapResultData bootStrapResultData = new BootStrapResultData();
        bootStrapResultData.setRows(userList);
        bootStrapResultData.setTotal(console.get("data_size"));
        // 根据结果返回数据
        return bootStrapResultData;
    }

    @ResponseBody
    @RequestMapping(value = "user/{userPhone}", method = RequestMethod.GET)
    public Result findByUserPhone(HttpServletRequest req, @PathVariable(value = "userPhone", required = false) String userPhone){
        // 调用service层方法
        User user = service.findByUserPhone(userPhone);
        // 根据结果返回数据
        if(user != null)
            // 查询到数据
            return new Result(200, "查询成功", user);
        return new Result(404, "该手机号查询不到", null);
    }

    @ResponseBody
    @RequestMapping(value = "user", method = RequestMethod.POST)
    public Result add(HttpServletRequest req){
        // 1.获取参数
        String userName = req.getParameter("userName");
        String userPhone = req.getParameter("userPhone");
        String idCard = req.getParameter("idCard");
        String password = req.getParameter("password");
        String email = req.getParameter("email");
        // 获取regTime 获取lastTime
        Timestamp timestamp = DateFormatUtil.dateFormatToTimestamp(new Date());
        // 获取ip
        String lastIp = req.getRemoteAddr();
        User user = new User(userName,userPhone,password,idCard,timestamp,timestamp,lastIp,email);
        // 2.调用service层方法
        boolean res = false;
        try {
            res = service.add(user);
        } catch (DuplicateUserNameException e) {
            return new Result(400, "用户名重复", null);
        } catch (DuplicateUserPhoneException e){
            return new Result(400, "手机号重复", null);
        } catch (DuplicateEmailException e){
            return new Result(400, "邮箱重复", null);
        }
        // 3.根据结果返回数据
        if(res)
            return new Result(201, "添加用户成功", null);
        return new Result(400, "添加失败", null);
    }

    @ResponseBody
    @RequestMapping(value = "user/{id}", method = RequestMethod.PUT)
    public Result update(HttpServletRequest req, @PathVariable(value = "id", required = false) Integer id){
        if (id == null)
            return new Result(400, "请先查询需要修改的用户信息", null);
        String userName = req.getParameter("userName");
        String userPhone = req.getParameter("userPhone");
        String idCard = req.getParameter("idCard");
        String password = req.getParameter("password");
        String email = req.getParameter("email");
        User user = new User(userName, userPhone, password, idCard, null, null, null, email);
        // 2.调用service层方法
        boolean res = false;
        try {
            res = service.update(id, user);
        } catch (DuplicateUserNameException e) {
            return new Result(400, "用户名重复", null);
        } catch (DuplicateUserPhoneException e) {
            return new Result(400, "手机号重复", null);
        } catch (DuplicateEmailException e) {
            return new Result(400, "邮箱重复", null);
        }
        // 3.根据结果返回数据
        if(res)
            return new Result(200, "修改成功", null);
        return new Result(400, "修改失败", null);
    }

    @ResponseBody
    @RequestMapping(value = "user/{id}", method = RequestMethod.DELETE)
    public Result delete(HttpServletRequest req, @PathVariable(value = "id", required = false) Integer id){
        if (id == null)
            return new Result(400, "请先查询需要删除的用户信息", null);
        // 2.调用service层方法
        boolean res = service.delete(id);
        // 3.根据结果返回数据
        if(res)
            return new Result(204, "删除成功", null);
        return new Result(500, "删除失败", null);
    }
}












