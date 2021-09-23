package com.xiaojumao.controller;

import com.xiaojumao.bean.*;
import com.xiaojumao.exception.DuplicateCNameException;
import com.xiaojumao.exception.DuplicateCPhoneException;
import com.xiaojumao.exception.DuplicateEmailException;
import com.xiaojumao.service.CourierService;
import com.xiaojumao.util.DateFormatUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author: whw
 * @Description:
 * @Date Created in 2021-07-02 21:51
 * @Modified By:
 */
@Controller
public class CourierContoller {

    @Autowired
    private CourierService courierService;

    @ResponseBody
    @RequestMapping(value = "courier/console",  method = RequestMethod.GET)
    public Result console(){
        // 调用service层方法
        Map<String, Integer> console = courierService.console();
        Result<Map<String, Integer>> result = new Result(200, "查询成功", console);
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "couriers", method = RequestMethod.GET)
    public BootStrapResultData findAll(HttpServletRequest req){
        // 1.接收参数
        boolean limit = true;
        if("1.".equals(req.getParameter("limit")))
            limit = false;
        String offset = req.getParameter("offset");
        String pageNumber = req.getParameter("pageNumber");
        // 2.调用service层方法
        // 获取快递员信息列表
        List<BootStrapTableCourier> bootStrapTableCourierList = new ArrayList<BootStrapTableCourier>();
        List<Courier> all = courierService.findAll(limit, Integer.parseInt(offset), Integer.parseInt(pageNumber));
        for (Courier c : all) {
            String strRegTime = DateFormatUtil.dateFormatToStr(c.getRegTime());
            String strLastTime = DateFormatUtil.dateFormatToStr(c.getLastTime());
            BootStrapTableCourier courier = new BootStrapTableCourier(c.getId().toString(), c.getcName(), c.getcPhone(), c.getPassword(), c.getSendNum(), c.getIdCard(), strRegTime, strLastTime, c.getEmail());
            bootStrapTableCourierList.add(courier);
        }
        // 查询快递员信息总条数
        Map<String, Integer> console = courierService.console();
        // 封装成bootstrap可识别的数据
        BootStrapResultData bootStrapResultData = new BootStrapResultData();
        bootStrapResultData.setRows(bootStrapTableCourierList);
        bootStrapResultData.setTotal(console.get("data_size"));
        // 返回数据
        return bootStrapResultData;
    }

    @ResponseBody
    @RequestMapping(value = "courier/{cPhone}", method = RequestMethod.GET)
    public Result findByCPhone(@PathVariable(value = "cPhone", required = false) String cPhone){
        // 调用servicec层方法
        Courier courier = courierService.findByCPhone(cPhone);
        // 根据结果返回数据
        if (courier == null) {
            // 查询失败
            return new Result(404, "手机号不存在", null);
        }
        return new Result(200, "查询成功", courier);
    }


    @ResponseBody
    @RequestMapping(value = "courier", method = RequestMethod.POST)
    public Result add(HttpServletRequest req){
        // 1.接收参数
        String cName = req.getParameter("cName");
        String cPhone = req.getParameter("cPhone");
        String idCard = req.getParameter("idCard");
        String password = req.getParameter("password");
        String email = req.getParameter("email");
        // 获取ip
        String ip = req.getRemoteAddr();
        Courier courier = new Courier(cName, cPhone, password, idCard, ip, email);
        // 2.调用servicec层方法
        boolean res = false;
        try {
            res = courierService.add(courier);
        } catch (DuplicateCNameException e) {
            // 名字重复
            return new Result(500, "名字重复", null);
        } catch (DuplicateCPhoneException e) {
            // 手机号重复
            return new Result(500, "手机号重复", null);
        }
        // 3.根据结果返回数据
        if(res){
            // 添加成功
            return new Result(201, "ok", null);
        }
        return new Result(500, "添加失败", null);
    }

    @ResponseBody
    @RequestMapping(value = "courier/{id}", method = RequestMethod.PUT)
    public Result update(@PathVariable(value = "id", required = false) Integer id, HttpServletRequest req){
        if (id == null)
            return new Result(400, "请先查询需要修改的快递员信息", null);
        String cName = req.getParameter("cName");
        String cPhone = req.getParameter("cPhone");
        String idCard = req.getParameter("idCard");
        String password = req.getParameter("password");
        String email = req.getParameter("email");
        Courier courier = new Courier(cName, cPhone, password, idCard, null, email);
        // 2.调用servicec层方法
        boolean res = false;
        try {
            res = courierService.update(id, courier);
        } catch (DuplicateCNameException e) {
            // 名字重复
            return new Result(400, "名字重复", null);
        } catch (DuplicateCPhoneException e) {
            // 手机号重复
            return new Result(400, "手机号重复", null);
        } catch (DuplicateEmailException e) {
            // 邮箱重复
            return new Result(400, "邮箱重复", null);
        }
        // 3.根据结果返回数据
        if(res){
            // 修改成功
            return new Result(200, "修改成功", null);
        }
        return new Result(500, "修改失败", null);
    }

    @ResponseBody
    @RequestMapping(value = "courier/{id}", method = RequestMethod.DELETE)
    public Result delete(@PathVariable(value = "id", required = false) Integer id){
        if (id == null)
            return new Result(400, "请先查询要删除的快递员信息", null);
        // 2.调用servicec层方法
        boolean res = courierService.delete(id);
        // 3.根据结果返回数据
        if(res){
            // 删除成功
            return new Result(204, "删除成功", null);
        }
        return new Result(500, "删除失败", null);
    }
}

























