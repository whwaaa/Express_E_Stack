package com.xiaojumao.exception;

import com.xiaojumao.bean.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalExceptionHandler  {

    @ExceptionHandler(NotLoginException.class)
    @ResponseBody
    public Result noLogin(NotLoginException ex){
        System.out.println(ex.getMessage());
        return new Result(401, "登录信息过期", ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result allErr(Exception ex){
        System.out.println("有异常");
        System.out.println("msg : " + ex.getMessage());
        return new Result(500, null, ex.getMessage());
    }

}
