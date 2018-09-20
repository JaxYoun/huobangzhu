package com.troy.keeper.hbz.app.web;

import com.google.gson.Gson;
import com.troy.keeper.core.base.dto.ResponseDTO;
import com.troy.keeper.hbz.consts.Const;
import com.troy.keeper.hbz.dto.HbzUserDTO;
import com.troy.keeper.hbz.service.HbzUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by leecheng on 2018/1/29.
 */
public class CheckUserFilter implements Filter {

    @Value("${security.parameter.user}")
    private String securityParameterUser;

    @Value("${security.parameter.pass}")
    private String securityParameterPass;

    @Value("${security.url.auths}")
    private String securityUrlAuths;

    @Autowired
    HbzUserService hbzUserService;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        if (request.getRequestURI().replaceAll("/+", "/").equals(securityUrlAuths)) {
            String l = request.getParameter(securityParameterUser);
            if (l != null) {
                HbzUserDTO user = hbzUserService.findByLogin(l);
                if (user != null) {
                    if (user.getActivated()) {
                        chain.doFilter(servletRequest, servletResponse);
                    } else {
                        response.setContentType("application/json;charset=utf-8");
                        ResponseDTO responseDTO = new ResponseDTO(Const.STATUS_ERROR, "用户已禁用!");
                        response.getWriter().write(new Gson().toJson(responseDTO));
                    }
                } else {
                    response.setContentType("application/json;charset=utf-8");
                    ResponseDTO responseDTO = new ResponseDTO(Const.STATUS_ERROR, "该用户不存在!");
                    response.getWriter().write(new Gson().toJson(responseDTO));
                }
            } else {
                response.setContentType("application/json;charset=utf-8");
                ResponseDTO responseDTO = new ResponseDTO(Const.STATUS_ERROR, "该用户不存在!");
                response.getWriter().write(new Gson().toJson(responseDTO));
            }
        } else {
            chain.doFilter(servletRequest, servletResponse);
        }
    }

    @Override
    public void destroy() {

    }
}
