package com.troy.keeper.hbz.app.security;

import com.troy.keeper.core.base.dto.ResponseDTO;
import com.troy.keeper.core.base.entity.Account;
import com.troy.keeper.core.utils.JsonUtils;
import com.troy.keeper.hbz.app.constant.MapSpec;
import com.troy.keeper.hbz.app.web.WebThreadHolder;
import com.troy.keeper.hbz.consts.Const;
import com.troy.keeper.hbz.dto.HbzEventDTO;
import com.troy.keeper.hbz.dto.HbzRoleDTO;
import com.troy.keeper.hbz.dto.HbzScoreDTO;
import com.troy.keeper.hbz.dto.HbzUserDTO;
import com.troy.keeper.hbz.helper.HttpHelper;
import com.troy.keeper.hbz.helper.subcomponent.LoginHelper;
import com.troy.keeper.hbz.service.HbzEventService;
import com.troy.keeper.hbz.service.HbzRoleService;
import com.troy.keeper.hbz.service.HbzScoreService;
import com.troy.keeper.hbz.service.HbzUserService;
import com.troy.keeper.hbz.type.Event;
import com.troy.keeper.hbz.type.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by leecheng on 2017/10/17.
 */
public class SecurityLoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Value("${session.header}")
    private String sessionTokenHeader;

    @Value("${security.parameter.user}")
    private String securityParameterUser;

    @Value("${security.parameter.pass}")
    private String securityParameterp;

    @Value("${session.header}")
    private String token;

    @Value("${debug}")
    private Boolean debug;

    @Value("${staticImagePrefix}")
    private String staticImagePrefix;

    @Autowired
    private HbzEventService hbzEventService;

    @Autowired
    HbzRoleService roles;

    @Autowired
    HbzScoreService hbzScoreService;

    @Autowired
    HbzUserService users;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        httpServletRequest.setCharacterEncoding("UTF-8");
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setHeader("Content-Type", "application/json;charset=utf-8");

        httpServletResponse.setHeader("Access-Control-Allow-Origin", "*");
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        httpServletResponse.setHeader("Access-Control-Max-Age", "3600");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", "x-requested-with,Authorization");
        httpServletResponse.setHeader("Access-Control-Allow-Credentials", "true");

        //登录类型业务
        String loginFor = httpServletRequest.getParameter("loginFor");
        String language = httpServletRequest.getParameter("language");
        httpServletRequest.getSession().setAttribute("language", language);
        httpServletRequest.getSession().setAttribute("loginFor", loginFor);
        LoginHelper.setLanguage(language);
        LoginHelper.setLoginFor(loginFor);

        httpServletResponse.setStatus(HttpServletResponse.SC_OK);
        Map<String, Object> res = new LinkedHashMap<>();
        res.put(sessionTokenHeader, httpServletRequest.getSession().getId());
        res.put("X-AUTH-USER", WebThreadHolder.currentUser());
        HbzUserDTO user = users.findByLogin(WebThreadHolder.currentUser());
        HbzScoreDTO score = hbzScoreService.attach(user.getId());
        res.put("complexScore", score.getScore());
        res.put("staticImagePrefix",staticImagePrefix);
        res.put("user", Optional.of(user).map(u -> {
            Map<String, Object> map = MapSpec.mapUser(u);
            //if (null != map.get("imageUrl")) {
            //    map.put("imageUrl", staticImagePrefix + map.get("imageUrl"));
            //} else {
            //    map.put("imageUrl", map.get("imageUrl"));
            //}
            return map;
        }).get());
        HbzRoleDTO rq = new HbzRoleDTO();
        rq.setStatus("1");
        rq.setUserId(user.getId());
        List<HbzRoleDTO> rl = roles.query(rq);

        List<Role> rs = rl.stream().map(HbzRoleDTO::getRole).collect(Collectors.toList());

        res.put("X-AUTH-ROLE", rs);


        ResponseDTO response = new ResponseDTO(Const.STATUS_OK, "已认证成功了，请更新会话令牌", res);
        HbzEventDTO e = new HbzEventDTO();
        e.setStatus(Const.STATUS_ENABLED);
        e.setDebug(debug);
        e.setEvent(Event.AUTH_SUCCESS_EVENT);
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
        e.setGrantedAuthorities(JsonUtils.toJson(((Account) WebThreadHolder.currentUserPrincipal()).getAuthorities()));
        e.setUsername(httpServletRequest.getParameter(securityParameterUser));
        hbzEventService.save(e);

        httpServletResponse.getWriter().print(JsonUtils.toJson(response));
    }
}
