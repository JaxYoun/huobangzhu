package com.troy.keeper.hbz.app.security;


import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;

public interface SecurityAuthMetadataSource extends
        FilterInvocationSecurityMetadataSource {

    void loadResource();

}