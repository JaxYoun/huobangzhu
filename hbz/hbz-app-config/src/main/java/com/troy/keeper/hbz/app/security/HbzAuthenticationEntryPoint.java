package com.troy.keeper.hbz.app.security;

import com.troy.keeper.core.utils.JsonUtils;
import com.troy.keeper.hbz.consts.Const;
import com.troy.keeper.hbz.dto.HbzEventDTO;
import com.troy.keeper.hbz.helper.HttpHelper;
import com.troy.keeper.hbz.service.HbzEventService;
import com.troy.keeper.hbz.type.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by leecheng on 2017/10/18.
 */
public class HbzAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Value("${security.parameter.user}")
    private String securityParameterUser;


    @Value("${security.parameter.pass}")
    private String securityParameterp;

    @Value("${debug}")
    private Boolean debug;

    @Autowired
    private HbzEventService hbzEventService;

    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        httpServletRequest.setCharacterEncoding("UTF-8");
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setContentType("application/json;charset=utf-8");
        httpServletResponse.setStatus(HttpServletResponse.SC_OK);
        Map<String, Object> resp = new HashMap<>();
        resp.put("code", Const.STATUS_UN_AUTHENCATIIONED);
        resp.put("msg", "当前令牌未认证，请更新令牌，重新再次认证");
        resp.put("data", "请提交用户参数到登录接口，参数名：j_user、j_pass");
        HbzEventDTO ev = new HbzEventDTO();
        ev.setStatus(Const.STATUS_ENABLED);
        ev.setDebug(debug);
        ev.setEvent(Event.UN_AUTH);
        ev.setEventTime(System.currentTimeMillis());
        Map<String, Object> header = new HashMap<>();
        Enumeration<String> hm = httpServletRequest.getHeaderNames();
        while (hm.hasMoreElements()) {
            String headername = hm.nextElement();
            header.put(headername, httpServletRequest.getHeader(headername));
        }
        ev.setEventUrl(httpServletRequest.getRequestURL().toString());
        ev.setRequestHeader(JsonUtils.toJson(header));
        ev.setPort(httpServletRequest.getRemotePort());
        ev.setIp(HttpHelper.getRemoteHost(httpServletRequest));
        ev.setEventParameter("[]");
        ev.setUsername("--未认证--");
        hbzEventService.save(ev);
        httpServletResponse.getWriter().print(JsonUtils.toJson(resp));
    }
}
