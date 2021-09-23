package com.xiaojumao.filter;


import com.xiaojumao.util.GetSession;
import com.xiaojumao.util.TokenUtils;

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
//@WebFilter({"/admin/index.html","/admin/views/*","/express/*","/courier/*","/user/*"})
public class AdminAccessControlFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
//        HttpServletRequest req = (HttpServletRequest) servletRequest;
//        HttpServletResponse resp = (HttpServletResponse) servletResponse;
//        String userName = GetSession.getAdminName(req);
//        filterChain.doFilter(servletRequest, servletResponse);
//        if (userName != null) {
//            // 登录过,放行
//            filterChain.doFilter(servletRequest, servletResponse);
//        }else{
//            resp.sendError(404, "很遗憾,进入此页面的权限不足!");
//            return;
//        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}
