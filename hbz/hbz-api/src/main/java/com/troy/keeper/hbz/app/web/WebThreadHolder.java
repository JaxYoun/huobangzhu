package com.troy.keeper.hbz.app.web;

import com.troy.keeper.core.base.entity.Account;
import com.troy.keeper.hbz.po.HbzSession;
import com.troy.keeper.hbz.sys.ApplicationContextHolder;
import com.troy.keeper.hbz.sys.ExtSessionRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by leecheng on 2017/10/16.
 */
public class WebThreadHolder {

    private static final ThreadLocal<HttpServletRequest> request = new ThreadLocal<>();
    private static final ThreadLocal<HttpServletResponse> response = new ThreadLocal<>();

    public static void setRequest(HttpServletRequest request) {
        WebThreadHolder.request.set(request);
    }

    public static void setResponse(HttpServletResponse response) {
        WebThreadHolder.response.set(response);
    }

    public static HttpServletRequest getCurrentRequest() {
        return request.get();
    }

    public static HttpServletResponse getCurrentResponse() {
        return response.get();
    }

    public static String currentUser() {
        Object user = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (user instanceof Account) {
            return ((Account) user).getUsername();
        } else if (user instanceof String) {
            return (String) user;
        } else {
            return "";
        }
    }

    public static boolean isAuthenticated() {
        SecurityContext sc = SecurityContextHolder.getContext();
        Authentication auth = sc.getAuthentication();
        if (auth != null) {
            return Account.class.isInstance(auth.getPrincipal());
        } else {
            return false;
        }
    }

    public static String getLanguage() {
        String sessionId = getCurrentRequest().getSession().getId();
        HbzSession session = ApplicationContextHolder.getService(ExtSessionRepository.class).findSession(sessionId);
        if (session != null) {
            return session.getLanguage();
        } else {
            throw new IllegalStateException("非法Session状态");
        }
    }

    public static <T> T currentUserPrincipal() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        if (securityContext != null) {
            Authentication authentication = securityContext.getAuthentication();
            if (authentication != null) {
                Object obj = authentication.getPrincipal();
                if (obj != null) {
                    return (T) obj;
                }
            }
        }
        return null;
    }
}
