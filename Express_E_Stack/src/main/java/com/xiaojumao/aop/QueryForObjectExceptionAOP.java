package com.xiaojumao.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class QueryForObjectExceptionAOP {

    @Pointcut("execution(* com.xiaojumao.service.AdminService.login(..)) || " +
            "execution(* com.xiaojumao.service.CourierService.findByEmail(..)) || " +
            "execution(* com.xiaojumao.service.CourierService.findByCPhone(..)) || " +
            "execution(* com.xiaojumao.service.CourierService.checkEmailAndPassword(..)) || " +
            "execution(* com.xiaojumao.service.ExpressService.findByNumber(..)) || " +
            "execution(* com.xiaojumao.service.ExpressService.findByCode(..)) || " +
            "execution(* com.xiaojumao.service.ExpressService.findById(..)) || " +
            "execution(* com.xiaojumao.service.UserService.findByEmail(..)) || " +
            "execution(* com.xiaojumao.service.UserService.findByUserPhone(..)) || " +
            "execution(* com.xiaojumao.service.UserService.add(..)) || " +
            "execution(* com.xiaojumao.service.UserService.findAll(..)) || " +
            "execution(* com.xiaojumao.service.UserService.checkEmailAndPassword(..))"
    )
    private void queryForObjectMethods(){}

    public void before(){
    }

    public void afterReturning(){

    }

    @AfterThrowing(value = "queryForObjectMethods()", throwing = "ex")
    public void afterThrowing(JoinPoint jp, Throwable ex){
        String message = ex.getMessage();
        System.out.println("queryForObject发生异常 ->");
        System.out.println("异常发生的位置：" + jp.getSignature().toString());
        System.out.println("详细信息：" + message);
    }

    public void after(){

    }

    public void around(){

    }
}
