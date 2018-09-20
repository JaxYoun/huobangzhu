package com.troy.keeper.hbz.app.web;

import com.troy.keeper.core.base.dto.ResponseDTO;
import com.troy.keeper.core.utils.JsonUtils;
import com.troy.keeper.hbz.app.dto.AuthCode;
import com.troy.keeper.hbz.consts.Const;
import org.springframework.beans.factory.annotation.Value;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

/**
 * Created by leecheng on 2017/10/25.
 */
public class AuthCodeFilter implements Filter {

    @Value("${security.url.auths}")
    private String securityUrlAuths;

    @Value("${security.url.logout}")
    private String securityLogout;

    @Value("${debug}")
    private Boolean deb;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        if (deb) {
            if (request.getSession().getAttribute(Const.AUTH_CODE_NAME) == null) {
                request.getSession().setAttribute(Const.AUTH_CODE_NAME, new AuthCode("1001", System.currentTimeMillis()));
            }
        }
        AuthCode authCode = (AuthCode) request.getSession().getAttribute(Const.AUTH_CODE_NAME);
        if (request.getRequestURI().replaceAll("/+", "/").equals(securityUrlAuths)) {
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
            response.setHeader("Access-Control-Max-Age", "3600");
            response.setHeader("Access-Control-Allow-Headers", "Access-Control-Allow-Headers, Origin, X-Requested-With, Content-Type, Accept,X-AUTH-TOKEN");
            response.setHeader("Access-Control-Allow-Credentials", "true");
            request.getSession().removeAttribute(Const.AUTH_CODE_NAME);
            String loginFor = request.getParameter("loginFor");
            if (loginFor == null || !Arrays.asList("Web", "App").contains(loginFor)) {
                ResponseDTO responseDTO = new ResponseDTO(Const.STATUS_ERROR, "无效登录方式，登录方式仅限App、Web!");
                response.setCharacterEncoding("UTF-8");
                response.setContentType("application/json;charset=utf-8");
                response.getWriter().print(JsonUtils.toJson(responseDTO));
            } else if (authCode == null) {
                chain.doFilter(request, response);
            } else if (request.getParameter(Const.AUTH_CODE_NAME) != null) {
                if (request.getParameter(Const.AUTH_CODE_NAME).equals(authCode.getAuthCode()) && authCode.getCreateTime() - System.currentTimeMillis() > -10L * 60L * 1000L) {
                    chain.doFilter(request, response);
                } else {
                    ResponseDTO responseDTO = new ResponseDTO(Const.STATUS_PRE_ERROR, "验证码错误!");
                    response.setCharacterEncoding("UTF-8");
                    response.setContentType("application/json;charset=utf-8");
                    response.getWriter().print(JsonUtils.toJson(responseDTO));
                }
            } else {
                ResponseDTO responseDTO = new ResponseDTO(Const.STATUS_PRE_ERROR, "验证码错误!");
                response.setCharacterEncoding("UTF-8");
                response.setContentType("application/json;charset=utf-8");
                response.getWriter().print(JsonUtils.toJson(responseDTO));
            }
        } else if (request.getRequestURI().replaceAll("/+", "/").equals(securityLogout)) {
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
            response.setHeader("Access-Control-Max-Age", "3600");
            response.setHeader("Access-Control-Allow-Headers", "Access-Control-Allow-Headers, Origin, X-Requested-With, Content-Type, Accept,X-AUTH-TOKEN");
            response.setHeader("Access-Control-Allow-Credentials", "true");
            chain.doFilter(request, response);
        } else {
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {

    }
}
