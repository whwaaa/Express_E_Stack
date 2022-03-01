package com.xiaojumao.controller;

import com.xiaojumao.bean.*;
import com.xiaojumao.exception.DuplicateNumberException;
import com.xiaojumao.service.ExpressService;
import com.xiaojumao.util.DateFormatUtil;
import com.xiaojumao.util.EmailSend;
import com.xiaojumao.util.TokenUtils;
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
 * @Date Created in 2021-07-01 16:34
 * @Modified By:
 */
@Controller
public class ExpressController {

    @Autowired
    private ExpressService service;

    @ResponseBody
    @RequestMapping(value = "express/console", method = RequestMethod.GET)
    public Result console(){
        // 1.调用service方法处理
        List<Map<String, Integer>> console = service.console();
        // 2.根据service返回结果给ajax返回
        if(console!=null && console.size()>0){
            return new Result(200,"查询成功",console);
        }else{
            return new Result(400, "查询失败", null);
        }
    }

    @ResponseBody
    @RequestMapping(value = "expresses", method = RequestMethod.GET)
    public Object findAll(HttpServletRequest req){
        // 1.接收参数
        boolean limit = true;
        if("1".equals(req.getParameter("limit")))
            limit = false;
        Integer offset = Integer.parseInt(req.getParameter("offset"));
        Integer pageNumber = Integer.parseInt(req.getParameter("pageNumber"));
        if(offset==null || pageNumber==null){
            return new Result(400, "分页查询错误", null);
        }
        // 2.调用service层方法
        // 查询数据列表
        List<Express> all = service.findAll(limit, offset, pageNumber);
        List<BootStrapTableExpress> bootStrapTableExpressList = new ArrayList<BootStrapTableExpress>();
        for (Express e : all) {
            String code = e.getCode()==null ? "已取件" : e.getCode();
            String strInTime = DateFormatUtil.dateFormatToStr(e.getInTime());
            String strOutTime = DateFormatUtil.dateFormatToStr(e.getOutTime());
            String status = e.getStatus().equals(0) ? "待取件" : "已取件";
            BootStrapTableExpress bootStrapTableExpress = new BootStrapTableExpress(e.getId().toString(), e.getNumber(), e.getUserName(), e.getUserPhone(), e.getCompany(), code, strInTime, strOutTime, status, e.getSysPhone(), e.getEmail());
            bootStrapTableExpressList.add(bootStrapTableExpress);
        }
        List<Map<String, Integer>> console = service.console();
        Integer data1_size = console.get(0).get("data1_size");
        // 将集合封装为 bootstrap-table识别的格式
        BootStrapResultData bootStrapResultData = new BootStrapResultData(bootStrapTableExpressList, data1_size);
        return bootStrapResultData;
    }

    @ResponseBody
    @RequestMapping(value = "express", method = RequestMethod.POST)
    public Result insert(HttpServletRequest req){
        String token = req.getParameter("token");
        // 1.接收参数
        String number = req.getParameter("number");
        String company = req.getParameter("company");
        String userName = req.getParameter("userName");
        String userPhone = req.getParameter("userPhone");
        String email = req.getParameter("email");
        int status = 0;
        Timestamp inTime = DateFormatUtil.dateFormatToTimestamp(new Date());
        Express e = new Express(number, userName, userPhone, company, inTime, status, TokenUtils.getSysPhone(token), email);
        // 2.调用service层方法
        String code = null;
        try {
            code = service.insert(e);
        } catch (DuplicateNumberException duplicateNumberException) {
            // 运单号重复
            return new Result(400, "录入失败,单号重复", null);
        }
        // 录入成功 -> 发送取件码到邮件
        EmailSend.send(email, code);
        // 响应前端
        return new Result(201, "录入成功", null);
    }

    @ResponseBody
    @RequestMapping(value = "express/{number}",  method = RequestMethod.GET)
    public Result findByNumber(@PathVariable(value = "number", required = false) String number){
        // 调用service方法
        Express express = service.findByNumber(number);
        // 根据结果返回数据
        if (express != null) {
            // 找到快递信息
            return new Result(200, "查找成功", express);
        }else{
            // 运单号查找不到快递信息
            return new Result(400, "单号不存在", null);
        }
    }

    @ResponseBody
    @RequestMapping(value = "express/{id}",  method = RequestMethod.PUT)
    public Result update(@PathVariable(value = "id", required = false) Integer id, HttpServletRequest req){
        if (id == null)
            return new Result(400, "请先查询需要修改的信息", null);
        String number = req.getParameter("number");
        String company = req.getParameter("company");
        String userName = req.getParameter("userName");
        String userPhone = req.getParameter("userPhone");
        String status = req.getParameter("status");
        String email = req.getParameter("email");
        Express e = new Express(number, company, userName, userPhone, Integer.parseInt(status), email);
        // 2.调用service方法
        Result result = null;
        if(status.equals("1")){
            // 修改为已取件
            e.setCode(null);
            e.setOutTime(DateFormatUtil.dateFormatToTimestamp(new Date()));
            if(service.update(id, e)) {
                result = new Result(200, "修改成功", null);
            }else{
                result = new Result(400, "快递单号重复", null);
            }
        }else{
            Express oldExpress = service.findById(id);
            // 修改为未取件,删除重新添加
            service.delete(id);
            e.setInTime(DateFormatUtil.dateFormatToTimestamp(new Date()));
            String code = null;
            try {
                code = service.insert(e);
            } catch (DuplicateNumberException duplicateNumberException) {
                // 修改后的运单号重复,恢复旧的数据
                try {
                    code = service.insert(oldExpress);
                } catch (DuplicateNumberException numberException) {
                    numberException.printStackTrace();
                }
                // 回复前端错误信息
                return new Result(400, "修改失败,单号重复", null);
            }
            // 修改成功,获取新的收货,重新发送邮件
            if(code != null){
                EmailSend.send(email, code);
            }
            result = new Result(200, "修改成功", null);
        }
        // 3.返回结果
        return result;

    }

    @ResponseBody
    @RequestMapping(value = "express/{id}",  method = RequestMethod.DELETE)
    public Result delete(@PathVariable(value = "id", required = false) Integer id){
        if (id == null)
            return new Result(400, "请先查询待删除的快递信息", null);
        // 2.都用service层方法
        boolean res = service.delete(id);
        // 3.根据结果返回数据
        if(res)
            return new Result(204, "删除成功", null);
        else
            return new Result(400, "删除失败", null);
    }

}
























