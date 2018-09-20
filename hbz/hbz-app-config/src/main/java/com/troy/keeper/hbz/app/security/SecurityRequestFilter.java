package com.troy.keeper.hbz.app.security;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.troy.keeper.hbz.helper.StringHelper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by leecheng on 2017/10/18.
 */
public class SecurityRequestFilter implements Filter {

    @Value("${security.parameter.user}")
    private String securityParameterUser;

    @Value("${security.parameter.pass}")
    private String securityParameterPass;

    @Value("${security.url.login}")
    private String securityUrlLogin;

    @Value("${security.url.auths}")
    private String securityUrlAuths;


    @Override
    public void destroy() {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String contentType = request.getHeader("Content-Type");
        if (StringHelper.isNullOREmpty(contentType)) {
            chain.doFilter(servletRequest, servletResponse);
        } else if (contentType.trim().toUpperCase().contains("APPLICATION/JSON") && request.getRequestURI().replaceAll("/+", "/").equals(securityUrlAuths)) {
            if (StringHelper.notNullAndEmpty(request.getHeader("Content-Length"))) {
                InputStream inputStream = request.getInputStream();
                ByteArrayOutputStream bo = new ByteArrayOutputStream();
                int cnt = 0;
                byte[] buff = new byte[1024];
                while ((cnt = inputStream.read(buff)) != -1) {
                    bo.write(buff, 0, cnt);
                    bo.flush();
                }
                byte[] bodyData = bo.toByteArray();
                String body = new String(bodyData, "UTF-8");
                JSONObject jsonObject = JSON.parseObject(body);
                HttpServletRequest proxyHttpServletRequest = (HttpServletRequest) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class[]{HttpServletRequest.class}, new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        if (method.getName().equals("getInputStream")) {
                            return new DelegateServletInputStream(new ByteArrayInputStream(bodyData), request.getContentLength());
                        } else if (method.getName().equals("getParameter") && method.getParameterCount() == 1 && method.getParameterTypes()[0] == String.class) {
                            return jsonObject.getString((String) args[0]);
                        }
                        return method.invoke(request, args);
                    }
                });
                chain.doFilter(proxyHttpServletRequest, response);
            } else {
                chain.doFilter(request, response);
            }
        } else {
            chain.doFilter(servletRequest, servletResponse);
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }
}
