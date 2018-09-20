package com.troy.keeper.hbz.app.rest.proxy;

import lombok.extern.apachecommons.CommonsLog;
import org.springframework.core.annotation.Order;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by leecheng on 2017/12/14.
 */
@Order(Integer.MIN_VALUE)
@WebFilter(urlPatterns = "/api/proxy", filterName = "proxy")
@CommonsLog
public class DelegateFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        log.debug("访问地址：" + httpServletRequest.getRequestURI());
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}
