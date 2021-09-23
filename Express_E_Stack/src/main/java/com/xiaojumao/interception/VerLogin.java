package com.xiaojumao.interception;

import com.xiaojumao.exception.NotLoginException;
import com.xiaojumao.util.TokenUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class VerLogin implements HandlerInterceptor {

    // 执行时间: controller执行之前
    // 执行场景: 登陆验证
    // 返回参数: true:表示放行,继续执行controller, false表示拦截,不执行controller
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        StringBuffer requestURL = request.getRequestURL();
        String requestURI = request.getRequestURI();
        if(requestURI.equals("/wx/sendVerCode.do") ||
                requestURI.equals("/wx/binding.do") ||
                requestURI.equals("/wx/login.do") ||
                requestURI.equals("/admin/login.do"))
            return true;
        String token = request.getParameter("token");
        if( token==null || !TokenUtils.isToken(token) )    // 验证失败未登录抛出异常
            throw new NotLoginException("还未登录");
        return true;
    }

    // 执行时间: 紧跟controller之后执行, ModelAndView返回之前
    // 执行场景: 记录日志, ip, 时间
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {}

    // 执行时间: 控制器执行后, ModelAndView返回之后
    // 执行场景: 全局资源的一些操作
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {}
}
