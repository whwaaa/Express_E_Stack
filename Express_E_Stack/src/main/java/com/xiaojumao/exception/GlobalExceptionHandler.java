package com.xiaojumao.exception;

import com.alibaba.fastjson.JSON;
import com.xiaojumao.bean.Result;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Component
public class GlobalExceptionHandler implements HandlerExceptionResolver {

//    @ExceptionHandler(NotLoginException.class)
//    @ResponseBody
//    public Result noLogin(NotLoginException ex){
//        System.out.println(ex.getMessage());
//        return new Result(401, "登录信息过期", ex.getMessage());
//    }
//
//    @ExceptionHandler(Exception.class)
//    @ResponseBody
//    public Result allErr(Exception ex){
//        System.out.println("有异常");
//        System.out.println("msg : " + ex.getMessage());
//        return new Result(500, null, ex.getMessage());
//    }

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object o, Exception e) {
        ModelAndView mv = new ModelAndView();
        // 未登录异常
        Result result;
        if(e instanceof NotLoginException) {
            result = new Result(401, "未登录或登录信息过期", e.getMessage());
        }else{
            result = new Result(500, null, e.getMessage());
        }
        // response方式返回json数据
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json; charset=utf-8");
        PrintWriter writer = null;
        try {
            // 返回AjaxResultVo封装的异常信息
            String msg = JSON.toJSON(result).toString();
            writer = response.getWriter();
            writer.write(msg);
            writer.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            writer.close();
        }
        return mv;
    }
}
