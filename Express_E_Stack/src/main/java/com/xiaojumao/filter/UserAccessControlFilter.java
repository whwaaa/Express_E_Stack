package com.xiaojumao.filter;


import com.xiaojumao.util.GetSession;
import com.xiaojumao.util.TokenUtils;
import com.xiaojumao.wx.bean.User;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author: whw
 * @Description:
 * @Date Created in 2021-07-03 16:16
 * @Modified By:
 */
//@WebFilter({
//        "/addExpress.html",
//        "/expressAssist.html",
//        "/expressList.html",
//        "/lazyboard.html",
//        "/personQRcode.html",
//        "/pickExpress.html",
//        "/userCheckStart.html",
//        "/wxIdCardUserInfoModify.html",
//        "/wxUserhome.html",
//        "/index.html"
//})
public class UserAccessControlFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
//        HttpServletRequest req = (HttpServletRequest) servletRequest;
//        HttpServletResponse resp = (HttpServletResponse) servletResponse;
//        User user = GetSession.getUser(req);
//        if (user != null) {
//            // 登录过,放行
//            filterChain.doFilter(servletRequest, servletResponse);
//        }else{
//            resp.sendRedirect("login.html");
//            return;
//        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}