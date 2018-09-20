package com.troy.keeper.hbz.app.sys;

import com.google.gson.Gson;
import com.troy.keeper.core.base.entity.Account;
import com.troy.keeper.core.base.entity.BaseAuditingEntity;
import com.troy.keeper.hbz.app.web.WebThreadHolder;
import com.troy.keeper.hbz.consts.Const;
import com.troy.keeper.hbz.dto.HbzEventDTO;
import com.troy.keeper.hbz.helper.HttpHelper;
import com.troy.keeper.hbz.repository.HbzUserRepository;
import com.troy.keeper.hbz.service.HbzEventService;
import com.troy.keeper.hbz.type.Event;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * Created by leecheng on 2017/10/16.
 */
@Component
@Aspect
public class SecureAspect {

    private static final Log log = LogFactory.getLog(SecureAspect.class);

    private final static Gson gson = new Gson();

    @Autowired
    private HbzEventService hbzEventService;

    @Value("${debug}")
    private Boolean debug;

    @Autowired
    private HbzUserRepository hbzUserRepository;

    @Pointcut("execution(* com.troy.keeper..*.save(..)) && (within(com.troy.keeper.core.base.repository.BaseRepository) || target(com.troy.keeper.core.base.repository.BaseRepository))")
    public void allRepository() {
    }

    /**
     * 拦截所有方法
     */
    @Pointcut("execution(* com.troy.keeper..*.*(..)) && (@within(org.springframework.stereotype.Controller) || @within(org.springframework.web.bind.annotation.RestController) || @annotation(com.troy.keeper.hbz.sys.annotation.Event))")
    public void allLogMethod() {
    }

    /**
     * 记录创建人
     */
    @Before("allRepository()")
    public void doSecurity(JoinPoint joinPoint) {

        Object authUserPrincial = WebThreadHolder.currentUserPrincipal();
        Long userid = 0L;
        if (authUserPrincial instanceof Account) {
            userid = ((Account) authUserPrincial).getAccountId();
        }

        Object[] os = joinPoint.getArgs();
        for (Object o : os) {
            if (o instanceof BaseAuditingEntity) {
                BaseAuditingEntity e = (BaseAuditingEntity) o;
                if (e.getId() != null) {
                    e.setLastUpdatedDate(new Date().getTime());
                    e.setLastUpdatedBy(userid);
                } else {
                    e.setCreatedDate(new Date().getTime());
                    e.setLastUpdatedDate(new Date().getTime());
                    e.setCreatedBy(userid);
                    e.setLastUpdatedBy(userid);
                }
            }
            if (o instanceof Iterable) {
                Iterable it = (Iterable) o;
                Iterator iterator = it.iterator();
                while (iterator.hasNext()) {
                    Object x = iterator.next();
                    if (x instanceof BaseAuditingEntity) {
                        BaseAuditingEntity e = (BaseAuditingEntity) x;
                        if (e.getId() != null) {
                            e.setLastUpdatedDate(new Date().getTime());
                            e.setLastUpdatedBy(userid);
                        } else {
                            e.setCreatedDate(new Date().getTime());
                            e.setLastUpdatedDate(new Date().getTime());
                            e.setCreatedBy(userid);
                            e.setLastUpdatedBy(userid);
                        }
                    }
                }
            }
        }
    }


    /**
     * 处理所有方法
     */
    @Before("allLogMethod()")
    public void invoke(JoinPoint joinPoint) {
        List<Object> aa = new ArrayList<>();
        Object[] args = joinPoint.getArgs();
        if (args != null) {
            for (Object a : args) {
                if (a != null && !(a instanceof HttpServletRequest) && !(a instanceof HttpServletResponse) && !(a instanceof HttpSession) && !(a instanceof Model) && !MultipartFile.class.isInstance(a)) {
                    aa.add(a);
                }
            }
        }
        log.debug("-------------------------------------------------");
        if (WebThreadHolder.getCurrentRequest() != null) {
            log.debug("URL:" + WebThreadHolder.getCurrentRequest().getRequestURL());
        }
        Object auth = WebThreadHolder.currentUserPrincipal();
        String username;
        String grantedAuthorities;
        if (auth instanceof Account) {
            username = ((Account) auth).getUsername();
            grantedAuthorities = gson.toJson(((Account) auth).getAuthorities());
            log.debug("认证：" + username);
            log.debug("权限：" + grantedAuthorities);
        } else {
            grantedAuthorities = null;
            username = null;
            log.debug("认证：" + "未认证");
        }
        log.debug("参数：" + gson.toJson(aa));

        HbzEventDTO e = new HbzEventDTO();
        e.setStatus(Const.STATUS_ENABLED);
        e.setDebug(debug);
        e.setEvent(Event.URI_REQUEST);
        e.setEventParameter(gson.toJson(aa));
        e.setEventTime(System.currentTimeMillis());
        if (WebThreadHolder.getCurrentRequest() != null) {
            Map<String, Object> header = new HashMap<>();
            Enumeration<String> hm = WebThreadHolder.getCurrentRequest().getHeaderNames();
            while (hm.hasMoreElements()) {
                String headername = hm.nextElement();
                header.put(headername, WebThreadHolder.getCurrentRequest().getHeader(headername));
            }
            e.setEventUrl(WebThreadHolder.getCurrentRequest().getRequestURL().toString());
            e.setRequestHeader(gson.toJson(header));
            e.setPort(WebThreadHolder.getCurrentRequest().getRemotePort());
            e.setIp(HttpHelper.getRemoteHost(WebThreadHolder.getCurrentRequest()));
        }
        e.setGrantedAuthorities(grantedAuthorities);
        e.setUsername(username);
        hbzEventService.save(e);
    }
}
