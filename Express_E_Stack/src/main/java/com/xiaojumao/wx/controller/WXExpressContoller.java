package com.xiaojumao.wx.controller;

import com.xiaojumao.bean.Express;
import com.xiaojumao.bean.LazyInfo;
import com.xiaojumao.bean.Result;
import com.xiaojumao.exception.DuplicateNumberException;
import com.xiaojumao.service.ExpressService;
import com.xiaojumao.util.*;
import com.xiaojumao.wx.bean.TableExpress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Stream;

/**
 * @Author: whw
 * @Description:
 * @Date Created in 2021-07-04 23:43
 * @Modified By:
 */
@Controller
@RequestMapping("wx")
public class WXExpressContoller {

    @Autowired
    private ExpressService service;

    @ResponseBody
    @RequestMapping(value = "expresses", method = RequestMethod.GET)
    public Result expressList(HttpServletRequest req){
        String token = req.getParameter("token");
        Result result = new Result();
        // 从session获取email
//        String email = GetSession.getUser(req).getEmail();
        String email = TokenUtils.getUser(token).getEmail();
        // 通过email查询快递信息
        List<Express> expressList = service.findByEmail(email);
        if(expressList == null)
            return new Result(400, "暂无快递信息", null);

        List<TableExpress> list2 = new ArrayList<>();
        for (Express e : expressList) {
            String code = e.getCode()==null ? "已取件" : e.getCode();
            String strInTime = DateFormatUtil.dateFormatToStr(e.getInTime());
            String strOutTime = DateFormatUtil.dateFormatToStr(e.getOutTime());
            String status = e.getStatus().equals(0) ? "待取件" : "已取件";
            TableExpress express = new TableExpress(code, e.getCompany(), e.getNumber(), e.getSysPhone(), strInTime, strOutTime,e.getInTime(), e.getOutTime(), status);
            list2.add(express);
        }
        if(list2.size() == 0){
            result.setCode(400);
        }else{
            // 集合排序
            Stream<TableExpress> expressStatus0 = list2.stream().filter(express -> {
                if (express.getStatus().equals("待取件")) {
                    return true;
                } else {
                    return false;
                }
            }).sorted((o1, o2) -> {
                long time1 = DateFormatUtil.toTime(o1.getStrInTime());
                long time2 = DateFormatUtil.toTime(o2.getStrInTime());
                if(time1 > time2){
                    return -1;
                }else if(time1 == time2){
                    return 0;
                }else{
                    return 1;
                }
            });
            Stream<TableExpress> expressStatus1 = list2.stream().filter(express -> {
                if (express.getStatus().equals("已取件")) {
                    return true;
                } else {
                    return false;
                }
            }).sorted((o1, o2) -> {
                long time1 = DateFormatUtil.toTime(o1.getStrOutTime());
                long time2 = DateFormatUtil.toTime(o2.getStrOutTime());
                if(time1 > time2){
                    return -1;
                }else if(time1 == time2){
                    return 0;
                }else{
                    return 1;
                }
            });
            Object[] objects0 = expressStatus0.toArray();
            Object[] objects1 = expressStatus1.toArray();
            Map data = new HashMap<>();
            data.put("status0",objects0);
            data.put("status1",objects1);
            result.setObj(data);
        }
        // 返回数据
        return result;
    }


    @ResponseBody
    @RequestMapping(value = "express", method = RequestMethod.POST)
    public Result addExpress(HttpServletRequest req, HttpServletResponse resp){
        String token = req.getParameter("token");
        // 接收参数
        String expressId = req.getParameter("expressId");
        String username = req.getParameter("username");
        String phone = req.getParameter("phone");
        String email = req.getParameter("email");
        String company = req.getParameter("company");
        Timestamp inTime = DateFormatUtil.dateFormatToTimestamp(new Date());
        Express express = new Express(expressId, username, phone, company, inTime, 0, TokenUtils.getSysPhone(token), email);
        // 向数据库中添加
        String code = null;
        try {
            code = service.insert(express);
        } catch (DuplicateKeyException | DuplicateNumberException e) {
            // 运单号重复
            return new Result(400, "录入失败,运单号重复", null);
        }
        // 发送取件码到邮箱
        EmailSend.send(email, code);
        // 向前端回复消息
        return new Result(201, "录入成功", null);
    }

    @ResponseBody
    @RequestMapping(value = "layboard.do", method = RequestMethod.GET)
    public Result layboard(HttpServletRequest req){
        // 获取查询时间
        Integer timeType = Integer.parseInt(req.getParameter("timeType"));
        // 查询所有快递
        List<LazyInfo> lazyboard = service.getLazyboard(timeType);
        // 返回数据
        return new Result(200, "ok", lazyboard);
    }

    @ResponseBody
    @RequestMapping(value = "express/{code}", method = RequestMethod.GET)
    public Result takeExpress(HttpServletRequest req, @PathVariable(value = "code", required = false) String code){
        // 1.添加收货时间 (dao层已处理 : 2.code设为null 3.status设为1)
        Timestamp outTime = DateFormatUtil.dateFormatToTimestamp(new Date());
        // 调用service方法更新
        boolean b = service.updateStatus(code, outTime);
        if(!b){
            return new Result(500, "出库失败", null);
        }
        return new Result(200, "出库成功", null);
    }
}
