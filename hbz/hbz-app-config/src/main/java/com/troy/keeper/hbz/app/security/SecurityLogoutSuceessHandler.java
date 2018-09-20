package com.troy.keeper.hbz.app.security;

import com.troy.keeper.core.base.dto.ResponseDTO;
import com.troy.keeper.core.utils.JsonUtils;
import com.troy.keeper.hbz.consts.Const;
import com.troy.keeper.hbz.dto.HbzEventDTO;
import com.troy.keeper.hbz.helper.HttpHelper;
import com.troy.keeper.hbz.service.HbzEventService;
import com.troy.keeper.hbz.type.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by leecheng on 2017/10/17.
 */
public class SecurityLogoutSuceessHandler implements LogoutSuccessHandler {

    public static final String NAME = "hbz_logout_success_handler";

    @Value("${security.parameter.user}")
    private String securityParameterUser;

    @Value("${security.parameter.pass}")
    private String securityParameterp;

    @Value("${debug}")
    private Boolean debug;

    @Autowired
    private HbzEventService hbzEventService;

    @Override
    public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        httpServletRequest.setCharacterEncoding("UTF-8");
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setHeader("Content-Type", "application/json;charset=utf-8");
        httpServletResponse.setStatus(HttpServletResponse.SC_OK);
        ResponseDTO response = new ResponseDTO(Const.STATUS_OK, "已注销", null);
        HbzEventDTO e = new HbzEventDTO();
        e.setStatus(Const.STATUS_ENABLED);
        e.setDebug(debug);
        e.setEvent(Event.LOGOUT_SUCCESS);
        e.setEventParameter(JsonUtils.toJson(Arrays.asList(httpServletRequest.getParameter(securityParameterUser), httpServletRequest.getParameter(securityParameterp))));
        e.setEventTime(System.currentTimeMillis());
        Map<String, Object> header = new HashMap<>();
        Enumeration<String> hm = httpServletRequest.getHeaderNames();
        while (hm.hasMoreElements()) {
            String headername = hm.nextElement();
            header.put(headername, httpServletRequest.getHeader(headername));
        }
        e.setEventUrl(httpServletRequest.getRequestURL().toString());
        e.setRequestHeader(JsonUtils.toJson(header));
        e.setPort(httpServletRequest.getRemotePort());
        e.setIp(HttpHelper.getRemoteHost(httpServletRequest));
        e.setGrantedAuthorities(null);
        hbzEventService.save(e);

        httpServletResponse.getWriter().print(JsonUtils.toJson(response));
    }
}
