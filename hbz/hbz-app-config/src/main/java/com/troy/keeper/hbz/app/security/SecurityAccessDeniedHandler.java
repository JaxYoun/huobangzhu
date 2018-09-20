package com.troy.keeper.hbz.app.security;

import com.troy.keeper.core.base.dto.ResponseDTO;
import com.troy.keeper.core.utils.JsonUtils;
import com.troy.keeper.hbz.app.web.WebThreadHolder;
import com.troy.keeper.hbz.consts.Const;
import com.troy.keeper.hbz.dto.HbzEventDTO;
import com.troy.keeper.hbz.helper.HttpHelper;
import com.troy.keeper.hbz.service.HbzEventService;
import com.troy.keeper.hbz.type.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lee on 2017/10/17.
 */
public class SecurityAccessDeniedHandler implements AccessDeniedHandler {

    @Value("${security.parameter.user}")
    private String securityParameterUser;

    @Value("${security.parameter.pass}")
    private String securityParameterp;

    @Value("${debug}")
    private Boolean debug;

    @Autowired
    private HbzEventService hbzEventService;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException, ServletException {
        request.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Type", "application/json;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        ResponseDTO responseData = new ResponseDTO(Const.STATUS_AUTH_REJECT, "权限不足", WebThreadHolder.currentUser());

        HbzEventDTO ev = new HbzEventDTO();
        ev.setStatus(Const.STATUS_ENABLED);
        ev.setDebug(debug);
        ev.setEvent(Event.AUTH_DENIED);
        ev.setEventTime(System.currentTimeMillis());
        Map<String, Object> header = new HashMap<>();
        Enumeration<String> hm = request.getHeaderNames();
        while (hm.hasMoreElements()) {
            String headername = hm.nextElement();
            header.put(headername, request.getHeader(headername));
        }
        ev.setEventUrl(request.getRequestURL().toString());
        ev.setRequestHeader(JsonUtils.toJson(header));
        ev.setPort(request.getRemotePort());
        ev.setIp(HttpHelper.getRemoteHost(request));
        ev.setUsername(WebThreadHolder.currentUser());
        hbzEventService.save(ev);

        response.getWriter().print(JsonUtils.toJson(responseData));
    }
}
