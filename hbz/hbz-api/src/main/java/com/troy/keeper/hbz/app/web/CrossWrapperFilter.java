package com.troy.keeper.hbz.app.web;

import org.springframework.beans.factory.annotation.Value;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by leecheng on 2018/1/11.
 */
public class CrossWrapperFilter implements Filter {

    @Value("${security.url.logout}")
    private String securityLogout;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain c) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        if (req.getRequestURI().replaceAll("/+", "/").equals(securityLogout)) {
            res.setHeader("Access-Control-Allow-Origin", "*");
            res.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
            res.setHeader("Access-Control-Max-Age", "3600");
            res.setHeader("Access-Control-Allow-Headers", "Access-Control-Allow-Headers, Origin, X-Requested-With, Content-Type, Accept,X-AUTH-TOKEN");
            res.setHeader("Access-Control-Allow-Credentials", "true");
            c.doFilter(req, res);
        } else {
            c.doFilter(req, res);
        }
    }

    @Override
    public void destroy() {

    }
}
