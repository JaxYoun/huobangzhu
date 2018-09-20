package com.troy.keeper.hbz.app.apps.auths;


import java.util.*;
import java.util.stream.Collectors;

import com.troy.keeper.hbz.app.security.SecurityAuthMetadataSource;
import com.troy.keeper.hbz.po.HbzAuth;
import com.troy.keeper.hbz.po.HbzUrl;
import com.troy.keeper.hbz.repository.HbzAuthRepository;
import com.troy.keeper.hbz.repository.HbzUrlRepository;
import com.troy.keeper.core.utils.JsonUtils;
import com.troy.keeper.monomer.security.domain.Authority;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;

/**
 * Created by leecheng on 2017/10/11.
 */
@Service
@javax.transaction.Transactional
public class SecurityAuthMetadataSourceImpl implements
        SecurityAuthMetadataSource {

    @Autowired
    private HbzAuthRepository hbzAuthRepository;

    private static Log log = LogFactory.getLog(SecurityAuthMetadataSource.class);

    @Autowired
    private HbzUrlRepository urlRepository;

    @Value("${debug}")
    private Boolean debug;

    private static Map<String, Collection<ConfigAttribute>> resourceMap = null;

    private AntPathMatcher antPathMatcher = new AntPathMatcher();

    public SecurityAuthMetadataSourceImpl() {
    }

    // 在Web服务器启动时，提取系统中的所有权限
    public void loadResource() {
        List<String> query = new ArrayList<>();
        log.debug("--------->加载关联");
        Iterator<HbzUrl> authorities = urlRepository.findAvilable().iterator();
        resourceMap = new HashMap<>();
        while (authorities.hasNext()) {
            HbzUrl urlAuthority = authorities.next();
            if (urlAuthority.getState() != null && urlAuthority.getState().equals("1")) {
                List<ConfigAttribute> list = urlAuthority.getAuths().stream().filter(au -> au.getState() != null && au.getState().equals("1") && au.getStatus().equals("1")).map(HbzAuth::getAuthName).map(SecurityConfig::new).collect(Collectors.toList());
                resourceMap.put(urlAuthority.getUrlPattern(), list);
            }
        }
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        log.debug("---------------->加载权限");
        List<HbzAuth> authorities = new ArrayList<>();
        Iterator<HbzAuth> authIterator = hbzAuthRepository.findAvilable().iterator();
        while (authIterator.hasNext()) {
            authorities.add(authIterator.next());
        }
        List<ConfigAttribute> auths = authorities.stream().map(HbzAuth::getAuthName).map(SecurityConfig::new).collect(Collectors.toList());
        return auths;
    }

    // 根据URL，找到相关的权限配置。
    @Override
    public Collection<ConfigAttribute> getAttributes(Object object)
            throws IllegalArgumentException {
        if (resourceMap == null) {
            loadResource();
        }
        List<ConfigAttribute> list = new ArrayList<>();
        String url = ((FilterInvocation) object).getRequestUrl();
        Set<String> urlPatterns = resourceMap.keySet();
        for (String urlPattern : urlPatterns) {
            if (antPathMatcher.match(urlPattern, url)) {
                list.addAll(resourceMap.get(urlPattern));
            }
        }
        log.debug("url:" + url + "\t au:" + JsonUtils.toJson(list));
        return list;
    }

    @Override
    public boolean supports(Class<?> arg0) {
        return true;
    }
}

