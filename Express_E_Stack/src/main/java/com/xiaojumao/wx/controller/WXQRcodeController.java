package com.xiaojumao.wx.controller;

import com.xiaojumao.bean.BootStrapTableExpress;
import com.xiaojumao.bean.Express;
import com.xiaojumao.bean.Message;
import com.xiaojumao.bean.Result;
import com.xiaojumao.service.ExpressService;
import com.xiaojumao.util.DateFormatUtil;
import com.xiaojumao.util.GetSession;
import com.xiaojumao.util.ResultHandle;
import com.xiaojumao.util.TokenUtils;
import com.xiaojumao.wx.bean.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * @Author: whw
 * @Description:
 * @Date Created in 2021-07-05 11:59
 * @Modified By:
 */
@Controller
@RequestMapping("wx")
public class WXQRcodeController {

    @Autowired
    private ExpressService service;

    @ResponseBody
    @RequestMapping(value = "createQRCode.do")
    public Result createQRCode(HttpServletRequest req){
        String token = req.getParameter("token");
        // 判断显示那种类型二维码 express | user
        String type = req.getParameter("type");
        User user = TokenUtils.getUser(token);
        // qrcode存入session
        if("express".equals(type)){
            String code = req.getParameter("code");
            user.setQRCOde("code:" + code);
        }else{ // user
            user.setQRCOde("user:" + user.getEmail());
        }
        TokenUtils.setUser(token, user);
        return new Result();
    }

    
    @ResponseBody
    @RequestMapping(value = "getQRCode.do")
    public Result getQRCode(HttpServletRequest req){
        String token = req.getParameter("token");
        return new Result(200, "null", TokenUtils.getUser(token));
    }

    @ResponseBody
    @RequestMapping(value = "findExpressByCode.do")
    public String findExpressByCode(HttpServletRequest req, HttpServletResponse resp){
        // 获取取货码
        String code = req.getParameter("code");
        // 通过取货码查询快递信息
        Express e = service.findByCode(code);
        if(e == null){
            return ResultHandle.toJSON(new Message(-1, "取货码查询不到"));
        }
        String tablecode = e.getCode()==null ? "已取件" : e.getCode();
        String strInTime = DateFormatUtil.dateFormatToStr(e.getInTime());
        String strOutTime = DateFormatUtil.dateFormatToStr(e.getOutTime());
        String status = e.getStatus()==0 ? "未取件" : "已取件";
        BootStrapTableExpress express = new BootStrapTableExpress(e.getId().toString(), e.getNumber(), e.getUserName(), e.getUserPhone(), e.getCompany(), tablecode, strInTime, strOutTime, status, strInTime, e.getEmail());
        return ResultHandle.toJSON(new Message(0, "查询成功", express));
    }

    @ResponseBody
    @RequestMapping(value = "findExpressByEmail.do")
    public String findExpressByEmail(HttpServletRequest req, HttpServletResponse resp){
        // 获取邮箱
        String email = req.getParameter("email");
        // 通过邮箱查询快递信息
        List<Express> expressList = service.findByEmail(email);
        List<BootStrapTableExpress> tableExpressList = new ArrayList<>();
        for (Express e : expressList) {
            String code = e.getCode()==null ? "已取件" : e.getCode();
            String strInTime = DateFormatUtil.dateFormatToStr(e.getInTime());
            String strOutTime = DateFormatUtil.dateFormatToStr(e.getOutTime());
            String status = e.getStatus()==0 ? "未取件" : "已取件";
            BootStrapTableExpress bte = new BootStrapTableExpress(e.getId().toString(), e.getNumber(), e.getUserName(), e.getUserPhone(), e.getCompany(), code, strInTime, strOutTime, status, strInTime, e.getEmail());
            tableExpressList.add(bte);
        }
        Stream<BootStrapTableExpress> expressStream = tableExpressList.stream().filter(e -> {
            if ("已取件".equals(e.getStatus())) {
                return false;
            }else{
                return true;
            }
        }).sorted((o1, o2) -> {
            long time1 = DateFormatUtil.toTime(o1.getStrInTime());
            long time2 = DateFormatUtil.toTime(o2.getStrInTime());
            return (int) (time1 - time2);
        });
        Object[] data = expressStream.toArray();
        if(data.length == 0){
            return ResultHandle.toJSON(new Message(-1, "没有未取快递诶"));
        }

        return ResultHandle.toJSON(new Message(0, "查询成功", data));
    }

}
